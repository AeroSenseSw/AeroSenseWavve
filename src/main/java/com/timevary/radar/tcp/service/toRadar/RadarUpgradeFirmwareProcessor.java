package com.timevary.radar.tcp.service.toRadar;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.protocol.RadarUpgradeFirmwareDto;
import com.timevary.radar.tcp.server.RadarTcpServer;
import com.timevary.radar.tcp.util.ByteUtil;
import com.timevary.radar.tcp.util.RadarCRC16;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 雷达固件升级处理器
 *
 * @author jia.wu
 */
@Slf4j
@Service
public class RadarUpgradeFirmwareProcessor extends SyncUserProcessor<RadarUpgradeFirmwareDto> {
    public static final int DEFAULT_RETRY_COUNT = 5;
    private final RadarTcpServer radarTcpServer;
    /**
     * 每次传输升级数据块大小
     */
    private static final int BLOCK_SIZE = 230;
    private static final int TIMEOUT_MILLS = 60*1000;
    public RadarUpgradeFirmwareProcessor(RadarTcpServer radarTcpServer) {
        this.radarTcpServer = radarTcpServer;
    }


    @Override
    public Object handleRequest(BizContext bizContext, RadarUpgradeFirmwareDto dto) throws Exception {
        try {
            if (ObjectUtils.isEmpty(dto.getRadarIds()) || ObjectUtils.isEmpty(dto.getFirmwareData())) {
                throw new RemotingException("参数错误，请检查升级雷达和固件数据是否正确设置");
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
//            if(e instanceof RadarServerException){
//                throw (RadarServerException)e;
//            }
//            throw new RadarServerException(e);
        }
        return dto;
    }

    /**
     * 按照size指定长度分割字节数组，并附加crc16校验
     *
     * @param firmwareData
     * @param size
     * @return
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
     * 执行升级
     * @param radarId
     * @param length
     * @param firmwareData
     * @return
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
            log.error("radar upgrade firmware error - radarId: {}" , radarId);
        }
        return null;
    }

    /**
     * 调用雷达，失败返回true，成功返回false
     * @param radarId
     * @param data
     * @param functionEnum
     * @return
     * @throws RemotingException
     * @throws InterruptedException
     */
    private boolean callRadar(String radarId, byte[] data, FunctionEnum functionEnum) throws RemotingException {
        int retryCount = DEFAULT_RETRY_COUNT;
        while (retryCount-->0) {
            try {
                RadarProtocolData radarProtocolData = RadarProtocolData
                        .builder()
                        .data(data)
                        .radarId(radarId)
                        .function(functionEnum)
                        .build();
                Object obj = radarTcpServer.invokeSync(radarProtocolData, TIMEOUT_MILLS);
                RadarProtocolData retObj = (RadarProtocolData) Objects.requireNonNull(obj);
                return ByteUtil.bytes2IntBig(retObj.getData()) == 0;
//            }catch (RadarNotInThisServerException| RadarNotConnectException e){
//                log.error("upgrade radar firmware error, connection closed cancel retry count: {}", retryCount, e);
//                return true;
            }catch (Exception e){
                log.error("upgrade radar firmware error, retry count: {}", retryCount, e);
            }
        }
        return false;
    }

    @Override
    public String interest() {
        return RadarUpgradeFirmwareDto.class.getName();
    }

}