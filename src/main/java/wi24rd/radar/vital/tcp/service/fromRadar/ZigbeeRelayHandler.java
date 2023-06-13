package wi24rd.radar.vital.tcp.service.fromRadar;

import java.util.Set;

import com.google.common.collect.Sets;

import wi24rd.radar.vital.tcp.handler.base.AbstractFromRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.handler.callback.RadarHandlerCallBack;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;

public class ZigbeeRelayHandler extends AbstractFromRadarProtocolDataHandler {

    public ZigbeeRelayHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        protocolData.setFunction(FunctionEnum.zigbeeRelay);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.zigbeeRelay);
    }
}
