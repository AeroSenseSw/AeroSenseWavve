package com.radar.handler;

import com.radar.radar.tcp.domain.dto.CallBackDto;
import com.radar.radar.tcp.domain.dto.ReportDto;
import com.radar.radar.tcp.hander.callback.RadarHandlerCallBackForConsumer;
import com.radar.radar.tcp.protocol.FunctionEnum;
import org.springframework.stereotype.Service;

/**
 * @author ：radar
 * @date ：Created in 2022/11/29 15:58
 * Customize the processor to process radar data
 */
@Service
public class ConsumerRadarHandler implements RadarHandlerCallBackForConsumer {

    @Override
    public void callBack(CallBackDto callBackDto) {
        if (callBackDto.getFunctionEnum() == FunctionEnum.radarReport) {
            // heart rate data
            ReportDto reportDto = (ReportDto) callBackDto.getData();
            // do something
        } else if (callBackDto.getFunctionEnum() == FunctionEnum.physicalActivityReportStatistics) {
            //body move data
            float value = (float) callBackDto.getData();
            //do something
        } else {
            //radar alert
            if (callBackDto.getFunctionEnum() == FunctionEnum.breathHeightBpmAlert) {
                // breathHeightBpmAlert
            }
            // more protocol see com.radar.radar.tcp.protocol.FunctionEnum
            // do something
        }
    }
}
