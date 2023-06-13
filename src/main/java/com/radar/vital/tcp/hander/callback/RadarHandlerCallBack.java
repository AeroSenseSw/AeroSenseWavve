package com.radar.vital.tcp.hander.callback;

import com.radar.vital.tcp.protocol.RadarProtocolData;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBack {
    void callBack(RadarProtocolData radarProtocolData);
}
