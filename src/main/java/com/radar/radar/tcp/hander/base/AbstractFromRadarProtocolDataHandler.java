package com.radar.radar.tcp.hander.base;

import com.radar.radar.tcp.hander.callback.RadarHandlerCallBack;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public abstract class AbstractFromRadarProtocolDataHandler implements RadarProtocolDataHandler {

    public RadarHandlerCallBack handlerCallBack;

    public AbstractFromRadarProtocolDataHandler(RadarHandlerCallBack handlerCallBack) {
        this.handlerCallBack = handlerCallBack;
    }
}
