package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:54
 * @modified By：
 * 获取翻身报警算法状态
 *      * 0：关闭算法监测
 *      * 1：开启翻身算法报警监测
 *      * 2：开启坐立报警算法监测
 *      * 3：开启翻身、坐立报警监测
 */
@Service
public class GetTurnOverTurnOnState extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getTurnOverTurnOnState);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }

}
