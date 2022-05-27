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
 * @date ：Created in 2022/2/22 14:39
 * @modified By：
 * 翻身报警/坐立报警
 */
@Service
public class RollOverOrSitAndCallThePoliceHandler extends AbstractFromRadarProtocolDataHandler {

    public RollOverOrSitAndCallThePoliceHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        //处理数据，不须返回
        protocolData.setFunction(FunctionEnum.rollOverOrSitAndCallThePolice);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.rollOverOrSitAndCallThePolice);
    }
}
