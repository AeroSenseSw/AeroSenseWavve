package com.radar.radar.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

import com.radar.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.radar.radar.tcp.protocol.FunctionEnum;


/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
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