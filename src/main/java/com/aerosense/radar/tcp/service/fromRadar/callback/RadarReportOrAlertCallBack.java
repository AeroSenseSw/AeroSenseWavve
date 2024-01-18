package com.aerosense.radar.tcp.service.fromRadar.callback;

import com.aerosense.radar.tcp.domain.dto.CallBackDto;
import com.aerosense.radar.tcp.domain.dto.FallDetectDto;
import com.aerosense.radar.tcp.domain.dto.ReportDto;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBackForConsumer;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
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
            CallBackDto callBackDto = new CallBackDto();
            callBackDto.setRadarId(radarProtocolData.getRadarId());
            callBackDto.setFunctionEnum(radarProtocolData.getFunction());
            callBackDto.setRadarVersion(radarProtocolData.getRadarVersion());
            ByteBuf byteBuf = Unpooled.wrappedBuffer(radarProtocolData.getData());
            try {
                switch (radarProtocolData.getFunction()) {
                    case radarReport:
                        float breathBpm = byteBuf.readFloat();
                        float breathLine = byteBuf.readFloat();
                        float heartBpm = byteBuf.readFloat();
                        float heartLine = byteBuf.readFloat();
                        float distance = byteBuf.readFloat();
                        float signalIntensity = byteBuf.readFloat();
                        int state = byteBuf.readInt();
                        callBackDto.setData(new ReportDto(
                                breathBpm, breathLine, heartBpm, heartLine,
                                distance, signalIntensity, state
                        ));
                        break;
                    case physicalActivityReportStatistics:
                        float readFloat = byteBuf.readFloat();
                        callBackDto.setData(readFloat);
                        break;
                    case fallDetect:
                        //fall position x,y
                        float x = byteBuf.readFloat();
                        float y = byteBuf.readFloat();
                        callBackDto.setData(new FallDetectDto(x, y));
                        break;
                    default:
                        int readInt = byteBuf.readInt();
                        callBackDto.setData(readInt);
                        break;
                }
            } finally {
                byteBuf.release();
            }
            radarHandlerCallBackForConsumer.callBack(callBackDto);
        } else {
            throw new RuntimeException("No custom callback");
        }
    }
}
