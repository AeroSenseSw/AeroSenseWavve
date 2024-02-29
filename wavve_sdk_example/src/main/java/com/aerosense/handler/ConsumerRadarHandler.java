package com.aerosense.handler;

import com.aerosense.radar.tcp.domain.dto.CallBackDto;
import com.aerosense.radar.tcp.domain.dto.ReportDto;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBackForConsumer;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author ：aerosense
 * @date ：Created in 2022/11/29 15:58
 * Customize the processor to process radar data
 */
@Service
public class ConsumerRadarHandler implements RadarHandlerCallBackForConsumer {
    private static final Logger log = LoggerFactory.getLogger(ConsumerRadarHandler.class);
    @Override
    public void callBack(CallBackDto callBackDto) {
        log.info("callback do {}", callBackDto);
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
            if (callBackDto.getFunctionEnum() == FunctionEnum.breathHighBpmAlert) {
                // breathHighBpmAlert
            }
            // more protocol see com.aerosense.radar.tcp.protocol.FunctionEnum
            // do something
        }
    }
}
