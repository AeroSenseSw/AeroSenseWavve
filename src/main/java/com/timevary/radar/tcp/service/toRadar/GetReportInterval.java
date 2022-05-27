package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;



/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
 * 获取数据上报时间间隔
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