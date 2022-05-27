package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;



/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
 * 获取版本号
 */
@Service
public class GetVersion extends AbstractToRadarProtocolDataHandler {
    public String process(String radarId) throws Exception {
//        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getVersion);
//        int readInt = byteBuf.readIntLE();
//        byteBuf.release();
//        return readInt + "";
        return null;
    }
}