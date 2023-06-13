package com.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

import com.radar.vital.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.radar.vital.tcp.protocol.FunctionEnum;

/**
 * @author ：ywb
 * @date ：Created in 2022/3/16 9:36
 * @modified By：
 */
@Service
public class GettingTheCardiacArrestAlarmTime  extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getCardiacArrestReportTime);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }
}
