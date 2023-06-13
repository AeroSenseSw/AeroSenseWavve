package wi24rd.radar.vital.tcp.handler.base;

import com.alipay.remoting.exception.RemotingException;

import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;

import java.util.Set;

/**
 *  
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/11 18:00
 * @version: 1.0
 */
public interface RadarProtocolDataHandler {
    /**
     */
    Object process(RadarProtocolData protocolData) throws RemotingException, InterruptedException;

    /**
     */
    Set<FunctionEnum> interests();
}
