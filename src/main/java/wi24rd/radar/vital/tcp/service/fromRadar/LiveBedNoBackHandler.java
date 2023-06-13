package wi24rd.radar.vital.tcp.service.fromRadar;

import com.google.common.collect.Sets;

import wi24rd.radar.vital.tcp.handler.base.AbstractFromRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.handler.callback.RadarHandlerCallBack;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;

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
