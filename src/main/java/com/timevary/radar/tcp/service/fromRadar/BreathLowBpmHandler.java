package com.timevary.radar.tcp.service.fromRadar;

import com.google.common.collect.Sets;
import com.timevary.radar.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import com.timevary.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:35
 * @modified By：
 * 呼吸低bpm报警
 */
@Service
public class BreathLowBpmHandler extends AbstractFromRadarProtocolDataHandler {

    public BreathLowBpmHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        //处理数据，不须返回
        protocolData.setFunction(FunctionEnum.breathLowBpmAlert);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.breathLowBpmAlert);
    }
}
