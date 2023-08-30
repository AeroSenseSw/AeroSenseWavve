package wi24rd.radar.handler;

import wi24rd.radar.vital.tcp.domain.dto.CallBackDto;
import wi24rd.radar.vital.tcp.domain.dto.ReportDto;
import wi24rd.radar.vital.tcp.handler.callback.RadarHandlerCallBackForConsumer;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import org.springframework.stereotype.Service;

/**
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
            if (callBackDto.getFunctionEnum() == FunctionEnum.HighBreathBPMAlert) {
                // HighBreathBPMAlert
            }
            // more protocol see wi24rd.radar.vital.tcp.protocol.FunctionEnum
            // do something
        }
    }
}
