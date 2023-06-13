package com.radar.vital.tcp.service.fromRadar;

import java.util.Set;

import com.google.common.collect.Sets;
import com.radar.vital.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import com.radar.vital.tcp.hander.callback.RadarHandlerCallBack;
import com.radar.vital.tcp.protocol.FunctionEnum;
import com.radar.vital.tcp.protocol.RadarProtocolData;

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
