package com.aerosense.radar.tcp.hander;

import com.aerosense.radar.tcp.connection.ConnectionUtil;
import com.aerosense.radar.tcp.domain.dto.RadarUpgradeFirmwareDto;
import com.aerosense.radar.tcp.exception.RadarNotConnectException;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.protocol.RadarTypeEnum;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import com.aerosense.radar.tcp.util.ByteUtil;
import com.aerosense.radar.tcp.util.RadarCRC16;
import com.alipay.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jia.wu
 * @version 1.0.0
 * @description: RadarFirmwareUpgradeHandler
 * @date 2024/1/11 11:52
 */
@Slf4j
@Component
public class RadarFirmwareUpgradeHandler {
    public static final int DEFAULT_RETRY_COUNT = 5;
    private final RadarTcpServer radarTcpServer;
    private static final int BLOCK_SIZE = 256;
    private static final int WAVVE_PRO_BLOCK_SIZE = 1000;
    private static final int TIMEOUT_MILLS = 60*1000;

    public RadarFirmwareUpgradeHandler(RadarTcpServer radarTcpServer) {
        this.radarTcpServer = radarTcpServer;
    }

    public RadarUpgradeFirmwareDto doUpgradeFirmware(RadarUpgradeFirmwareDto dto) throws RemotingException {
        if (ObjectUtils.isEmpty(dto.getRadarIds()) || ObjectUtils.isEmpty(dto.getFirmwareData())) {
            throw new RemotingException("Parameter error, please check whether the upgraded radar and firmware data are set correctly");
        }
        List<Byte> types = dto.getRadarIds().stream()
                .map(radarTcpServer::getRadarConnection)
                .filter(Objects::nonNull)
                .map(ConnectionUtil::getRadarType)
                .distinct().collect(Collectors.toList());
        if (types.size() != 1) {
            log.error("upgrade radar type must be equal 1 reality {}", types);
            if (types.size()>1){
                throw new RemotingException("radar list must is same type");
            }else{
                throw new RemotingException("radars not connect "+dto.getRadarIds());
            }
        }
        RadarTypeEnum radarTypeEnum = RadarTypeEnum.fromType(types.get(0));
        int blockSize = RadarTypeEnum.WAVVE_PRO==radarTypeEnum?WAVVE_PRO_BLOCK_SIZE:BLOCK_SIZE;
        byte[][] firmwareData = splitAndCrc16(radarTypeEnum, dto.getFirmwareData(), blockSize);
        int length = dto.getFirmwareData().length;
        List<String> radarIds = dto.getRadarIds().stream()
                .map(radarId -> doUpgrade(radarId, length, firmwareData))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        dto.setFirmwareData(null);
        dto.setRadarIds(radarIds);
        return dto;
    }

    /**
     *
     *
     * @param radarTypeEnum
     * @param firmwareData
     * @param size
     * @return
     */
    private byte[][] splitAndCrc16(RadarTypeEnum radarTypeEnum, byte[] firmwareData, int size) {
        int blockSize = (firmwareData.length / size) + (firmwareData.length % size > 0 ? 1 : 0);
        byte[][] splitData = new byte[blockSize][];
        for (int i = 0; i < blockSize; i++) {
            int blockIndex = i+1;
            int startIndex = i * size;
            int endIndex = blockIndex * size;
            if (endIndex > firmwareData.length) {
                endIndex = firmwareData.length;
            }
            byte[] blockData = Arrays.copyOfRange(firmwareData, startIndex, endIndex);
            int blockCrc16 = RadarCRC16.crc16BaseRadar(blockData);
            byte[] crc16Bytes = ByteUtil.shortToByteLittle((short) blockCrc16);
            boolean attachBlockIndex = radarTypeEnum == RadarTypeEnum.WAVVE_PRO;
            int bytesLength = attachBlockIndex?blockData.length+4+2:blockData.length+2;
            byte[] bytes = new byte[bytesLength];
            int copyStartIndex = 0;
            if(attachBlockIndex) {
                //index start from 0
                byte[] blockIndexBytes = ByteUtil.intToByteBig(blockIndex-1);
                System.arraycopy(blockIndexBytes, 0, bytes, copyStartIndex, 4);
                copyStartIndex+=4;
            }
            System.arraycopy(blockData, 0, bytes, copyStartIndex, blockData.length);
            copyStartIndex+=blockData.length;
            System.arraycopy(crc16Bytes, 0, bytes, copyStartIndex, 2);
            splitData[i] = bytes;
        }
        return splitData;
    }

    /**
     */
    private String doUpgrade(String radarId, int length, byte[][] firmwareData) {
        try {
            if (callRadar(radarId, ByteUtil.intToByteBig(length), FunctionEnum.notifyUpdate)) {
                log.error("start upgrade firmware failure - radarId: {} firmwareLength: {}", radarId, length);
                return null;
            }
            log.debug("start upgrade firmware successful - radarId: {} firmwareLength: {}", radarId, length);
            int blockSize = firmwareData.length;
            log.debug("start transport firmware content  - radarId: {} blockSize: {}", radarId, blockSize);
            for (int i = 0; i < blockSize; i++) {
                byte[] data = firmwareData[i];
                if (callRadar(radarId, data, FunctionEnum.issueFirmware)) {
                    log.error("transport firmware content failure - radarId: {} blockIndex: {}", radarId, i);
                    return null;
                }else{
                    log.debug("transport firmware content success - radarId: {} blockIndex: {}", radarId, i);
                }
            }
            log.debug("end transport firmware content successful - radarId: {}", radarId);
            if (callRadar(radarId, ByteUtil.intToByteBig(0), FunctionEnum.updateResult)) {
                log.error("end upgrade firmware failure  - radarId: {}", radarId);
                return null;
            }
            log.debug("end upgrade firmware successful  - radarId: {}", radarId);
            return radarId;
        } catch (Throwable e) {
            log.error("radar upgrade firmware error - radarId: {}", e, radarId);
        }
        return null;
    }

    /**
     */
    private boolean callRadar(String radarId, byte[] data, FunctionEnum functionEnum){
        int retryCount = 1, maxRetryCount = DEFAULT_RETRY_COUNT;
        RadarProtocolData radarProtocolData = RadarProtocolData.newEmptyInstance()
                .fillFunctionData(radarId, functionEnum, data);
        while (retryCount<=maxRetryCount) {
            try {
                Object obj = radarTcpServer.invokeSync(radarProtocolData, TIMEOUT_MILLS);
                RadarProtocolData retObj = (RadarProtocolData) Objects.requireNonNull(obj);
                return ByteUtil.bytes2IntBig(retObj.getData()) != 1;
            }catch (Exception e){
                if (e instanceof RadarNotConnectException){
                    return true;
                }
                log.error("upgrade radar firmware error, retry count: {} - {}", retryCount, functionEnum, e);
                if(retryCount==maxRetryCount){
                    return true;
                }
                retryCount++;
            }
        }
        return true;
    }
}
