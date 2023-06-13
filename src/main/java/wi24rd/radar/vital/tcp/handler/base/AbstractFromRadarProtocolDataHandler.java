package wi24rd.radar.vital.tcp.handler.base;

import wi24rd.radar.vital.tcp.handler.callback.RadarHandlerCallBack;

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
