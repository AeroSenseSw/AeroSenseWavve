package com.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

import com.radar.vital.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.radar.vital.tcp.protocol.FunctionEnum;

/**
 * @author ：ywb
 * @date ：Created in 2022/2/22 14:49
 * @modified By：
 */
@Service
public class GetTheStatusOfTheNumberOfMotionsCollected  extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getTheStatusOfTheNumberOfMotionsCollected);
        int readIntLE = byteBuf.readInt();
        byteBuf.release();
        return readIntLE;
    }

}