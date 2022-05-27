package com.timevary.radar.tcp.service.fromRadar.callback;

import com.timevary.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.timevary.radar.tcp.hander.callback.RadarHandlerCallBackForConsumer;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 11:03
 * @modified By：
 */
@Service
@Slf4j
public class RadarReportOrAlertCallBack implements RadarHandlerCallBack {

    @Autowired(required = false)
    RadarHandlerCallBackForConsumer radarHandlerCallBackForConsumer;

    @Override
    public void callBack(RadarProtocolData radarProtocolData) {
        if (radarHandlerCallBackForConsumer != null) {
            radarHandlerCallBackForConsumer.callBack(radarProtocolData);
        } else {
            throw new RuntimeException("No custom callback");
        }
    }
}
