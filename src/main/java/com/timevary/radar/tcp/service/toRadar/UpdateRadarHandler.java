package com.timevary.radar.tcp.service.toRadar;

import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.RemotingException;
import com.timevary.radar.tcp.config.RequestTimeOut;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.protocol.http.ResponseCode;
import com.timevary.radar.tcp.protocol.http.ResponseResult;
import com.timevary.radar.tcp.serilazer.RadarSerializer;
import com.timevary.radar.tcp.server.RadarTcpServer;
import com.timevary.radar.tcp.util.ByteUtils;
import com.timevary.radar.tcp.util.CRC16;
import com.timevary.radar.tcp.util.ReadFirmware;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.*;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 13:37
 * @modified By：
 */
@Slf4j
@Service
public class UpdateRadarHandler {

    @Autowired
    private RadarTcpServer radarTcpServer;
    @Autowired
    private ReadFirmware readFirmware;

    private final static InvokeContext invokeContext = new InvokeContext();

    static {
        invokeContext.put(InvokeContext.BOLT_CUSTOM_SERIALIZER, RadarSerializer.IDX_BYTE);
    }

    public Object process(RadarProtocolData protocolData, String path) {
        try {
            boolean update = notifyUpdate(protocolData, path);
            System.out.println(update);
            return new ResponseResult(update ? ResponseCode.Success : ResponseCode.Fail);
        } catch (RemotingException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * data 为固件大小
     *
     * @param protocolData 参数
     */
    private boolean notifyUpdate(RadarProtocolData protocolData, String path) throws RemotingException, InterruptedException {
        RadarProtocolData radarProtocolData = (RadarProtocolData) radarTcpServer.invokeSync(radarTcpServer.getRadarAddress(protocolData.getRadarId()),
                protocolData, invokeContext, RequestTimeOut.TIME_OUT);
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        try {
            buf.writeBytes(radarProtocolData.getData());
            int success = buf.readInt();
            boolean notifyOk = radarProtocolData.getFunction() == FunctionEnum.notifyUpdate && success == 1;
            if (notifyOk) {
                return updateFrame(protocolData, path);
            }
        } finally {
            buf.release();
        }
        return false;
    }
    private boolean updateFrame(RadarProtocolData protocolData, String path) {
        File file = new File(path);
        return updateFrameFile(protocolData,file);
    }
    private boolean updateFrameFile(RadarProtocolData protocolData, File file) {
        protocolData.setFunction(FunctionEnum.issueFirmware);
        byte[] firmwareFile = readFirmware.readFile(file);
        int count = (int) Math.ceil(firmwareFile.length / 240f);
        final int frameLen = 240;
        int start = 0;
        int i = 0;
        byte[] bytes;
        byte[] sendBytes;
        RadarProtocolData radarProtocolData;
        try {
            while (i < count) {
                bytes = new byte[frameLen];
                if (firmwareFile.length - start >= 240) {
                    System.arraycopy(firmwareFile, start, bytes, 0, bytes.length);
                } else {
                    System.arraycopy(firmwareFile, start, bytes, 0, firmwareFile.length - start);
                }
                //写
                byte[] crcBytes = ByteUtils.hexStringToBytes(CRC16.getCRC16(bytes));
                sendBytes = new byte[bytes.length + crcBytes.length];
                //copy bytes
                System.arraycopy(bytes, 0, sendBytes, 0, bytes.length);
                //copy crcBytes
                System.arraycopy(crcBytes, 0, sendBytes, bytes.length, crcBytes.length);
                protocolData.setData(sendBytes);
                radarProtocolData = (RadarProtocolData) radarTcpServer.invokeSync(radarTcpServer.getRadarAddress(protocolData.getRadarId()),
                        protocolData, invokeContext, RequestTimeOut.TIME_OUT);

                if (radarProtocolData.getFunction() != FunctionEnum.issueFirmware || radarProtocolData.getData()[3] != 1) {
                    log.error("第 {} 段数据传输失败", i);
                    for (int j = 0; j < 3; j++) {
                        radarProtocolData = (RadarProtocolData) radarTcpServer.invokeSync(radarTcpServer.getRadarAddress(protocolData.getRadarId()),
                                protocolData, invokeContext, RequestTimeOut.TIME_OUT);
                        log.error("重试...");
                    }
                } else {
                    i++;
                    start += 240;

                }
            }
        } catch (RemotingException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return checkUpdate(protocolData);
    }
    private boolean checkUpdate(RadarProtocolData protocolData) {
        protocolData.setFunction(FunctionEnum.updateResult);
        protocolData.setData(new byte[4]);
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        try {
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(2);
                RadarProtocolData updateResult = (RadarProtocolData) radarTcpServer.invokeSync(radarTcpServer.getRadarAddress(protocolData.getRadarId()),
                        protocolData, invokeContext, RequestTimeOut.TIME_OUT);
                byteBuf.writeBytes(updateResult.getData());
                boolean b = byteBuf.readInt() == 1;
                if (b) {
                    return true;
                }
            }
        } catch (RemotingException | InterruptedException e) {
            log.error("检查升级是否成功时失败，{}", e.getMessage());
            return false;
        } finally {
            byteBuf.release();
        }
//        responseResult.setCode(Short.toUnsignedInt(FunctionEnum.updateResult.getFunction()));
//        responseResult.setMsg("检查升级是否成功时失败");
//        WebsocketSendMsg.sendMessage(responseResult);
        return false;
    }
}
