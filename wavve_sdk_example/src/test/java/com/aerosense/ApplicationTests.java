package com.aerosense;

import com.aerosense.radar.tcp.connection.ConnectionUtil;
import com.aerosense.radar.tcp.domain.dto.BedLocationDto;
import com.aerosense.radar.tcp.domain.dto.DetectionAreaDto;
import com.aerosense.radar.tcp.domain.dto.RadarInstallModeDto;
import com.aerosense.radar.tcp.domain.dto.RadarUpgradeFirmwareDto;
import com.aerosense.radar.tcp.hander.ServerRequestRadarHandler;
import com.aerosense.radar.tcp.protocol.RadarTypeEnum;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import com.alipay.remoting.Connection;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

@SpringBootTest
class ApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ApplicationTests.class);
    @Autowired
    private ServerRequestRadarHandler serverRequestRadarHandler;
    @Autowired
    private RadarTcpServer radarTcpServer;

    @Test
    void contextLoads() {
    }

    @Test
    public void testRadarApplication() throws InterruptedException {
        while(radarTcpServer.getOnlineRadarCount()<=0){
            Thread.sleep(3000);
        }

        Set<String> onlineRadarList = radarTcpServer.getOnlineRadarList();
        onlineRadarList.forEach(this::testRadar);
    }

    private void testRadar(String radarId)  {
        Connection radarConnection = radarTcpServer.getRadarConnection(radarId);
        if (radarConnection!=null){
            RadarTypeEnum radarTypeEnum = RadarTypeEnum.fromType(ConnectionUtil.getRadarType(radarConnection));
            switch (radarTypeEnum){
                case WAVVE_PRO:{
                    testWavveProRadar(radarId);
                }break;
                case WAVVE:{
                    testWavveRadar(radarId);
                }break;
            }
        }

    }

    private void testWavveRadar(String radarId) {
        String firmareFilePath = "D:\\develop\\resources\\aerosense\\firmware\\wavve\\Aerosense_Wavve.Release.2.1.98.0.bin";
//        upgradeRadarFirmware(radarId, firmwareFilePath);

        debugGetSetWavveRadarParams(radarId);
    }

    private void testWavveProRadar(String radarId) {
        String firmwareFilePath = "D:\\develop\\resources\\aerosense\\firmware\\wavve_pro\\WavvePro.Release.0.3.0.0.bin";
//        upgradeRadarFirmware(radarId, firmwareFilePath);

        debugGetSetWavveProRadarParams(radarId);
        debugGetSetWavveRadarParams(radarId);
    }

    private void upgradeRadarFirmware(String radarId, String firewareFilePath) {
        try{
            FileInputStream in = new FileInputStream(firewareFilePath);
            RadarUpgradeFirmwareDto radarUpgradeFirmwareDto = new RadarUpgradeFirmwareDto();
            radarUpgradeFirmwareDto.setRadarIds(Lists.newArrayList(radarId));
            radarUpgradeFirmwareDto.setFirmwareData(StreamUtils.copyToByteArray(in));
            List<String> upgradeRadars = serverRequestRadarHandler.upgradeFirmware(radarUpgradeFirmwareDto);
            log.info("upgrade wavve radar firmware {}", upgradeRadars);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void debugGetSetWavveProRadarParams(String radarId) {
        try {
            String firmwareVersion = serverRequestRadarHandler.getFirmwareVersion(radarId);
            log.info("wavve pro radar id {} firmware version {}", radarId, firmwareVersion);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            RadarInstallModeDto radarInstallMode = serverRequestRadarHandler.getRadarInstallMode(radarId);
            log.info("wavve pro radar install mode {}", radarInstallMode);

            boolean b = serverRequestRadarHandler.setRadarInstallMode(radarInstallMode);
            log.info("set wavve pro radar install mode result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            BedLocationDto bedLocation = serverRequestRadarHandler.getBedLocation(radarId);
            log.info("wavve pro radar bed location {}", bedLocation);

            boolean b = serverRequestRadarHandler.setBedLocation(bedLocation);
            log.info("set wavve pro radar bed location result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            DetectionAreaDto detectionArea = serverRequestRadarHandler.getDetectionArea(radarId);
            log.info("wavve pro radar bed location {}", detectionArea);

            boolean b = serverRequestRadarHandler.setDetectionArea(detectionArea);
            log.info("set wavve pro radar detection area result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            int bufferTime = serverRequestRadarHandler.getBufferTime(radarId);
            log.info("wavve pro radar bufferTime {}", bufferTime);

            boolean b = serverRequestRadarHandler.setBufferTime(radarId, bufferTime);
            log.info("set wavve pro radar bufferTime result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void debugGetSetWavveRadarParams(String radarId) {
        try{
            int reportInterval = serverRequestRadarHandler.getReportInterval(radarId);
            log.info("wavve radar report interval {}", reportInterval);
            boolean b = serverRequestRadarHandler.setReportInterval(radarId, reportInterval);
            log.info("set wavve radar report interval result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int respiratoryArrestReportTime = serverRequestRadarHandler.getRespiratoryArrestReportTime(radarId);
            log.info("wavve radar respiratoryArrestReportTime {}", respiratoryArrestReportTime);
            boolean b = serverRequestRadarHandler.setRespiratoryArrestReportTime(radarId, respiratoryArrestReportTime);
            log.info("set wavve radar setRespiratoryArrestReportTime result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getBreathBpmLowThreshold(radarId);
            log.info("wavve radar getBreathBpmLowThreshold {}", value);
            boolean b = serverRequestRadarHandler.setBreathBpmLowThreshold(radarId, value);
            log.info("set wavve radar setBreathBpmLowThreshold result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getBreathBpmHighThreshold(radarId);
            log.info("wavve radar getBreathBpmHighThreshold {}", value);
            boolean b = serverRequestRadarHandler.setBreathBpmHighThreshold(radarId, value);
            log.info("set wavve radar setBreathBpmHighThreshold result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getBreathBpmThresholdAlarmTime(radarId);
            log.info("wavve radar getBreathBpmThresholdAlarmTime {}", value);
            boolean b = serverRequestRadarHandler.setBreathBpmThresholdAlarmTime(radarId, value);
            log.info("set wavve radar setBreathBpmThresholdAlarmTime result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            int value = serverRequestRadarHandler.getBreathBpmThresholdAlarmTime(radarId);
            log.info("wavve radar getBreathBpmThresholdAlarmTime {}", value);
            boolean b = serverRequestRadarHandler.setBreathBpmThresholdAlarmTime(radarId, value);
            log.info("set wavve radar setBreathBpmThresholdAlarmTime result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getHeartRateBPMThresholdTimer(radarId);
            log.info("wavve radar getHeartRateBPMThresholdTimer {}", value);
            boolean b = serverRequestRadarHandler.setHeartRateBPMThresholdTimer(radarId, value);
            log.info("set wavve radar setHeartRateBPMThresholdTimer result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getCardiacArrestReportTime(radarId);
            log.info("wavve radar getCardiacArrestReportTime {}", value);
            boolean b = serverRequestRadarHandler.setCardiacArrestReportTime(radarId, value);
            log.info("set wavve radar setCardiacArrestReportTime result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getHeartRateLowThreshold(radarId);
            log.info("wavve radar getHeartRateLowThreshold {}", value);
            boolean b = serverRequestRadarHandler.setHeartRateLowThreshold(radarId, value);
            log.info("set wavve radar setHeartRateLowThreshold result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            int value = serverRequestRadarHandler.getHeartRateHighThreshold(radarId);
            log.info("wavve radar getHeartRateHighThreshold {}", value);
            boolean b = serverRequestRadarHandler.setHeartRateHighThreshold(radarId, value);
            log.info("set wavve radar setHeartRateHighThreshold result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getBedExitAlertTimer(radarId);
            log.info("wavve radar getBedExitAlertTimer {}", value);
            boolean b = serverRequestRadarHandler.setBedExitAlertTimer(radarId, value);
            log.info("set wavve radar setBedExitAlertTimer result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getNoTurnOverAlertTimer(radarId);
            log.info("wavve radar getNoTurnOverAlertTimer {}", value);
            boolean b = serverRequestRadarHandler.setNoTurnOverAlertTimer(radarId, value);
            log.info("set wavve radar setNoTurnOverAlertTimer result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int value = serverRequestRadarHandler.getTurnOverAlertStatus(radarId);
            log.info("wavve radar getTurnOverAlertStatus {}", value);
            boolean b = serverRequestRadarHandler.setTurnOverAlertStatus(radarId, value);
            log.info("set wavve radar setTurnOverAlertStatus result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            int value = serverRequestRadarHandler.getBodyMovementCounterStatus(radarId);
            log.info("wavve radar getBodyMovementCounterStatus {}", value);
            boolean b = serverRequestRadarHandler.setBodyMovementCounterStatus(radarId, value);
            log.info("set wavve radar setBodyMovementCounterStatus result {}", b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
