package com.radar.radar.tcp.service.toRadar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.radar.radar.tcp.config.RequestTimeOut;
import com.radar.radar.tcp.hander.base.AbstractToRadarProtocolDataHandler;
import com.radar.radar.tcp.protocol.FunctionEnum;
import com.radar.radar.tcp.protocol.RadarProtocolData;
import com.radar.radar.tcp.server.RadarTcpServer;


/**
 * @author ：ywb
 * @date ：Created in 2022/2/24 14:09
 * @modified By：
 */
@Service
@Slf4j
public class GetRadarIdHandler extends AbstractToRadarProtocolDataHandler {

    public String process(RadarTcpServer radarTcpServer, String addr) {
        try {
            log.info("get radar...");
            RadarProtocolData radarProtocolData = new RadarProtocolData();
            radarProtocolData.setFunction(FunctionEnum.getRadarId);
            final int timeOut = RequestTimeOut.TIME_OUT;
            RadarProtocolData radarProtocolData2 = (RadarProtocolData) radarTcpServer.invokeSync(addr,
                    radarProtocolData, invokeContext, timeOut);
            return radarProtocolData2.getRadarId();
        } catch (Exception e) {
            log.error("time out ...");
        }
        return null;
    }

    public String process(String radarId) throws Exception {
        return null;
    }

}
