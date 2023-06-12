package com.radar.radar.tcp.hander.callback;

import com.radar.radar.tcp.protocol.RadarProtocolData;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBack {
    void callBack(RadarProtocolData radarProtocolData);
}
