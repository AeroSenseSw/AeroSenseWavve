package com.aerosense.radar.tcp.service.toRadar;

import com.aerosense.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/3/16 9:34
 * @modified By：
 */
@Service
public class GetBreathingBpmLowThresholdAlarmTime extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getBreathBpmThresholdAlarmTime);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }
}
