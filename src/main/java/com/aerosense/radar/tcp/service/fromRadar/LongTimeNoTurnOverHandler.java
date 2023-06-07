package com.aerosense.radar.tcp.service.fromRadar;

import com.google.common.collect.Sets;
import com.aerosense.radar.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:35
 * @modified By：
 */
@Service
public class LongTimeNoTurnOverHandler extends AbstractFromRadarProtocolDataHandler {
    public LongTimeNoTurnOverHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        protocolData.setFunction(FunctionEnum.longTimeNoTurnOver);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.longTimeNoTurnOver);
    }
}
