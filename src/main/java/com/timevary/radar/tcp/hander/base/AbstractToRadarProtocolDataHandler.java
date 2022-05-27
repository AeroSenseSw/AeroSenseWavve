package com.timevary.radar.tcp.hander.base;

import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.RemotingException;
import com.timevary.radar.tcp.config.RequestTimeOut;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.serilazer.RadarSerializer;
import com.timevary.radar.tcp.server.RadarTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
@Service
public abstract class AbstractToRadarProtocolDataHandler implements RadarProtocolDataHandler {
    @Autowired
    private RadarTcpServer radarTcpServer;
    public final static InvokeContext invokeContext = new InvokeContext();

    static {
        invokeContext.put(InvokeContext.BOLT_CUSTOM_SERIALIZER, RadarSerializer.IDX_BYTE);
    }

    @Override
    public Object process(RadarProtocolData protocolData) throws RemotingException, InterruptedException {
        int timeOut = RequestTimeOut.TIME_OUT;
        if (protocolData.getFunction() == FunctionEnum.calibration) {
            timeOut = 10000 * 10;
        }
        return radarTcpServer.invokeSync(radarTcpServer.getRadarAddress(protocolData.getRadarId()),
                protocolData, invokeContext, timeOut);
    }

    @Override
    public Set<FunctionEnum> interests() {
        return null;
    }

    public ByteBuf processDo(String radarId, FunctionEnum functionEnum) throws Exception {
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        radarProtocolData.setFunction(functionEnum);
        radarProtocolData.setRadarId(radarId);
        RadarProtocolData radarProtocolData2 = (RadarProtocolData) process(radarProtocolData);
        if (radarProtocolData2.getData() == null) {
            throw new Exception(functionEnum.getFunction() + "is return null");
        }
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        byteBuf.writeBytes(radarProtocolData2.getData());
        return byteBuf;
    }

    abstract public Object process(String radarId) throws Exception;
}
