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
 * @date ：Created in 2022/2/12 10:07
 * @modified By：
 * 建立连接
 */
@Service
public class CreateConnectionHandler extends AbstractFromRadarProtocolDataHandler {

    public CreateConnectionHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        //处理数据，不须返回
        protocolData.setFunction(FunctionEnum.createConnection);
        handlerCallBack.callBack(protocolData);
        //大端返回
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        radarProtocolData.setFunction(FunctionEnum.createConnection);
        byte[] data = {1, 0};
        radarProtocolData.setData(data);
        return radarProtocolData;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.createConnection);
    }
}
