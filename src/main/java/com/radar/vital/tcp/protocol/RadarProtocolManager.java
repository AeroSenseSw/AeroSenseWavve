
package com.radar.vital.tcp.protocol;

import com.alipay.remoting.ProtocolManager;

/**
 *  
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/3 16:23
 * @version: 1.0
 */
public class RadarProtocolManager {
    /**
     * 初始化雷达协议
     */
    public static void initProtocols() {
        ProtocolManager.registerProtocol(new RadarProtocol(), RadarProtocol.PROTOCOL_CODE);
    }
}
