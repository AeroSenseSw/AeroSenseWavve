package com.radar.vital.tcp.service.fromRadar;

import com.google.common.collect.Sets;
import com.radar.vital.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import com.radar.vital.tcp.hander.callback.RadarHandlerCallBack;
import com.radar.vital.tcp.protocol.FunctionEnum;
import com.radar.vital.tcp.protocol.RadarProtocolData;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:35
 * @modified By：
 */
@Service
public class LiveBedNoBackHandler extends AbstractFromRadarProtocolDataHandler {
    public LiveBedNoBackHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        protocolData.setFunction(FunctionEnum.liveBedNoBack);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.liveBedNoBack);
    }
}
