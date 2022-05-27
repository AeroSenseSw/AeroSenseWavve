package com.timevary.radar.tcp.connection;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/12 10:33
 * @version: 1.0
 */
@Slf4j
public class DisconnectServerEventProcessor implements ConnectionEventProcessor {
    private final RadarAddressMap radarAddressMap;
    public DisconnectServerEventProcessor(RadarAddressMap radarAddressMap){
        this.radarAddressMap = radarAddressMap;
    }

    @Override
    public void onEvent(String address, Connection connection) {
        Object radarId = connection.getAttribute(ConnectionUtil.ATTR_RADAR_Id);
        if(radarId==null){
            log.warn("this connection is not from radar, address: {}", address);
            return;
        }
        radarAddressMap.unbindAddress(address, radarId.toString());
        log.info("unregister radar successful {}  -  {}", radarId, address);
    }
}
