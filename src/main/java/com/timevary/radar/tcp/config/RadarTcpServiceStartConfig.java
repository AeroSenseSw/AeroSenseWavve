//package com.timevary.radar.tcp.config;
//
//import com.timevary.radar.tcp.service.fromRadar.callback.RadarReportOrAlertCallBack;
//import com.timevary.radar.tcp.server.RadarTcpServer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author ：ywb
// * @date ：Created in 2022/1/10 10:18
// * @modified By：
// */
//@Configuration
//public class RadarTcpServiceStartConfig {
//
//    @Autowired
//    private RadarReportOrAlertCallBack radarReportOrAlertCallBack;
//
//    @Bean
//    public RadarTcpServer radarTcpServer() {
//        return RadarTcpServer.radarServerStarter(radarReportOrAlertCallBack);
//    }
//
//}
