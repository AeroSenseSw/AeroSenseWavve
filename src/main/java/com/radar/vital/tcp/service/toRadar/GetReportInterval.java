package com.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

import com.radar.vital.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.radar.vital.tcp.protocol.FunctionEnum;



/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
 */
@Service
public class GetReportInterval extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getReportInterval);
        int readInt = byteBuf.readInt() * 50;
        byteBuf.release();
        return readInt;
    }

}