package com.timevary.radar.tcp.service.toRadar;

import com.timevary.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/2/22 14:49
 * @modified By：
 * 获取体动次数收集状态
 *      * 0：关闭算法监测
 *      * 1：开启算法监测
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