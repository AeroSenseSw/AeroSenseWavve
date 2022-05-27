package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/2/17 17:09
 * @modified By：
 * 获取心率BPM阈值报警持续时间 单位 秒
 */
@Service
public class GettingTheCardiacArrestAlarmTimeHandler extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.gettingTheCardiacArrestAlarmTime);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }

}
