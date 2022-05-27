package com.timevary.radar.tcp.hander.base;

import com.alipay.remoting.exception.RemotingException;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/11 18:00
 * @version: 1.0
 */
public interface RadarProtocolDataHandler {
    /**
     * 处理雷达协议数据
     * @param protocolData
     * @return
     */
    Object process(RadarProtocolData protocolData) throws RemotingException, InterruptedException;

    /**
     * 希望处理的函数列表
     * @return
     */
    Set<FunctionEnum> interests();
}
