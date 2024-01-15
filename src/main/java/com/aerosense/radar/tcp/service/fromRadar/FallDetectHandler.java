package com.aerosense.radar.tcp.service.fromRadar;

import com.aerosense.radar.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @description: Fall detect handler
 * @author jia.wu
 * @date 2024/1/11 16:51
 * @version 1.0.0
 */
@Service
public class FallDetectHandler extends AbstractFromRadarProtocolDataHandler {
    public FallDetectHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        handlerCallBack.callBack(protocolData);
        return protocolData.success();
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.fallDetect);
    }
}
