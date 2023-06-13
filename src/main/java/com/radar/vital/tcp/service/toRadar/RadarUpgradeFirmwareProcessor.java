package com.radar.vital.tcp.service.toRadar;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.radar.vital.tcp.protocol.FunctionEnum;
import com.radar.vital.tcp.protocol.RadarProtocolData;
import com.radar.vital.tcp.protocol.RadarUpgradeFirmwareDto;
import com.radar.vital.tcp.server.RadarTcpServer;
import com.radar.vital.tcp.util.ByteUtil;
import com.radar.vital.tcp.util.RadarCRC16;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jia.wu
 */
@Slf4j
@Service
public class RadarUpgradeFirmwareProcessor extends SyncUserProcessor<RadarUpgradeFirmwareDto> {
    public static final int DEFAULT_RETRY_COUNT = 5;
    private final RadarTcpServer radarTcpServer;
    private static final int BLOCK_SIZE = 256;
    private static final int TIMEOUT_MILLS = 60*1000;
    public RadarUpgradeFirmwareProcessor(RadarTcpServer radarTcpServer) {
        this.radarTcpServer = radarTcpServer;
    }


    @Override
    public Object handleRequest(BizContext bizContext, RadarUpgradeFirmwareDto dto) throws Exception {
        try {
            if (ObjectUtils.isEmpty(dto.getRadarIds()) || ObjectUtils.isEmpty(dto.getFirmwareData())) {
                throw new RemotingException("Parameter error, please check whether the upgraded radar and firmware data are set correctly");
            }
            byte[][] firmwareData = splitAndCrc16(dto.getFirmwareData(), BLOCK_SIZE);
            int length = dto.getFirmwareData().length;
            //返回升级成功的雷达id列表
            List<String> radarIds = dto.getRadarIds().stream()
                    .map(radarId -> doUpgrade(radarId, length, firmwareData))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            dto.setFirmwareData(null);
            dto.setRadarIds(radarIds);
        }catch (Exception e){
            log.error("radar upgrade firmware error", e);
        }
        return dto;
    }

    /**
     */
    private byte[][] splitAndCrc16(byte[] firmwareData, int size) {
        int blockSize = (firmwareData.length/size) + (firmwareData.length % size>0 ? 1 : 0);
        byte[][] splitData = new byte[blockSize][];
        for (int i = 0; i < blockSize; i++) {
            int endIndex = (i + 1) * size;
            if(endIndex > firmwareData.length){
                endIndex = firmwareData.length;
            }
            byte[] blockData = Arrays.copyOfRange(firmwareData, i * size, endIndex);
            int blockCrc16 = RadarCRC16.crc16BaseRadar(blockData);
            byte[] crc16Bytes = ByteUtil.shortToByteLittle((short) blockCrc16);
            byte[] otherCrc16Bytes = RadarCRC16.getCRC(blockData);
            log.debug("blockCrc16: {} - crc16Bytes: {} otherCrc16Bytes: {}",
                    blockCrc16, Arrays.toString(crc16Bytes), Arrays.toString(otherCrc16Bytes));
            int blockDataLength = blockData.length;
            byte[] bytes = new byte[blockDataLength + 2];
            System.arraycopy(blockData, 0, bytes, 0, blockDataLength);
            System.arraycopy(crc16Bytes, 0, bytes, blockDataLength, 2);
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
        int retryCount = DEFAULT_RETRY_COUNT;
        RadarProtocolData radarProtocolData = RadarProtocolData.newEmptyInstance()
                .fillFunctionData(radarId, functionEnum, data);
        while (retryCount-->0) {
            try {
                Object obj = radarTcpServer.invokeSync(radarProtocolData, TIMEOUT_MILLS);
                RadarProtocolData retObj = (RadarProtocolData) Objects.requireNonNull(obj);
                return ByteUtil.bytes2IntBig(retObj.getData()) != 1;
            }catch (Exception e){
                log.error("upgrade radar firmware error, retry count: {} - {}", retryCount, functionEnum, e);
                if(retryCount==0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String interest() {
        return RadarUpgradeFirmwareDto.class.getName();
    }

}