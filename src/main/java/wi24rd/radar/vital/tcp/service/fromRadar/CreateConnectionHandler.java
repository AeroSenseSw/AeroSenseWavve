package wi24rd.radar.vital.tcp.service.fromRadar;

import com.google.common.collect.Sets;

import wi24rd.radar.vital.tcp.hander.base.AbstractFromRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.hander.callback.RadarHandlerCallBack;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：ywb
 * @date ：Created in 2022/2/12 10:07
 * @modified By：
 */
@Service
public class CreateConnectionHandler extends AbstractFromRadarProtocolDataHandler {

    public CreateConnectionHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        protocolData.setFunction(FunctionEnum.createConnection);
        handlerCallBack.callBack(protocolData);
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
