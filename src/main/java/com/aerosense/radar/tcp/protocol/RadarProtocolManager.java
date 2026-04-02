
package com.aerosense.radar.tcp.protocol;

import com.alipay.remoting.ProtocolManager;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/3 16:23
 * @version: 1.0
 */
public class RadarProtocolManager {

    public static void initProtocols() {
        ProtocolManager.registerProtocol(new WavveRadarProtocol(), WavveRadarProtocol.PROTOCOL_CODE);
        ProtocolManager.registerProtocol(new WavveProRadarProtocol(), WavveProRadarProtocol.PROTOCOL_CODE);
    }
}
