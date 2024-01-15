package com.aerosense.radar.tcp.hander.base;

import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.RemotingException;
import com.aerosense.radar.tcp.config.RequestTimeOut;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.serilazer.RadarSerializer;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
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

    @Override
    public Object process(RadarProtocolData protocolData) throws RemotingException, InterruptedException {
        int timeOut = RequestTimeOut.TIME_OUT;
        if (protocolData.getFunction() == FunctionEnum.calibration) {
            timeOut = 10000 * 10;
        }
        return radarTcpServer.invokeSync(protocolData, timeOut);
    }

    @Override
    public Set<FunctionEnum> interests() {
        return null;
    }

    public ByteBuf processDo(String radarId, FunctionEnum functionEnum) throws Exception {
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        radarProtocolData.setFunction(functionEnum);
        radarProtocolData.setRadarId(radarId);
        RadarProtocolData radarProtocolDataRet = (RadarProtocolData) process(radarProtocolData);
        if (radarProtocolDataRet.getData() == null) {
            throw new Exception(functionEnum.getFunction() + "is return null");
        }
        return Unpooled.wrappedBuffer(radarProtocolDataRet.getData());
    }

    abstract public Object process(String radarId) throws Exception;
}
