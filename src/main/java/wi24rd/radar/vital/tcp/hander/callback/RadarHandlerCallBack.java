package wi24rd.radar.vital.tcp.hander.callback;

import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBack {
    void callBack(RadarProtocolData radarProtocolData);
}
