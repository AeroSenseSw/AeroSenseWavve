package com.radar.vital.tcp.hander.base;

import com.alipay.remoting.exception.RemotingException;
import com.radar.vital.tcp.protocol.FunctionEnum;
import com.radar.vital.tcp.protocol.RadarProtocolData;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
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
