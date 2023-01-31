package com.aerosense.radar.tcp.hander.callback;

import com.aerosense.radar.tcp.protocol.RadarProtocolData;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBack {
    void callBack(RadarProtocolData radarProtocolData);
}
