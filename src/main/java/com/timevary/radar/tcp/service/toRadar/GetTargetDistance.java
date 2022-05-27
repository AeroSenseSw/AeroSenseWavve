package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.stereotype.Service;

import java.nio.Buffer;


/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
 * 获取设置工作目标距离
 */
@Service
public class GetTargetDistance extends AbstractToRadarProtocolDataHandler {

    public Float process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getTargetDistance);
        float readFloat = byteBuf.readFloat() * 100;
        byteBuf.release();
        return readFloat;
    }

}