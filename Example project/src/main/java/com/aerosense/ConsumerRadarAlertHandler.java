package com.aerosense;

import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import org.springframework.stereotype.Service;

/**
 * @author ：tmevary
 * @date ：Created in 2022/11/29 15:58
 * Customize the processor to process radar data
 */
@Service
public class ConsumerRadarAlertHandler implements RadarHandlerCallBack {
    @Override
    public void callBack(RadarProtocolData radarProtocolData) {
        if (radarProtocolData.getFunction() == FunctionEnum.breathHeightBpmAlert) {
            // breathHeightBpmAlert
            // more protocol see com.aerosense.radar.tcp.protocol.FunctionEnum
        }
        //do something
    }
}
