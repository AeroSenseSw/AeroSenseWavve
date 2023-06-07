package com.aerosense.radar.tcp.service.toRadar;

import com.aerosense.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;



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