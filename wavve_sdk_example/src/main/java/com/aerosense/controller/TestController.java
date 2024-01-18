package com.aerosense.controller;

import com.aerosense.radar.tcp.domain.dto.RadarUpgradeFirmwareDto;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.service.toRadar.GetReportInterval;
import com.aerosense.radar.tcp.service.toRadar.SetRadarParams;
import com.aerosense.radar.tcp.util.ByteUtil;
import com.alipay.remoting.exception.RemotingException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aerosense.radar.tcp.hander.ServerRequestRadarHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ：aerosense
 * @date ：Created in 2022/11/29 16:19
 * @modified By：
 */
@RestController
public class TestController {

    @Autowired
    private ServerRequestRadarHandler serverRequestRadarHandler;

    @GetMapping("/testGet")
    public Object testGet() throws Exception {
        String radarId = "15796F49000500000002001500";
        return serverRequestRadarHandler.getReportInterval(radarId);
    }

    @GetMapping("/testSet")
    public Object testSet() throws RemotingException {
        String radarId = "15796F49000500000002001500";
        return serverRequestRadarHandler.setReportInterval(radarId, 200);
    }

    @GetMapping("/test-upgrade-firmware")
    public Object testUpgradeFirmware() throws RemotingException, IOException {
        String radarId = "15796F49000500000002001500";
        RadarUpgradeFirmwareDto dto = new RadarUpgradeFirmwareDto();
        dto.setRadarIds(Lists.newArrayList(radarId));
        String firmwareFilePath = "D:\\develop\\resources\\aerosense\\firmware\\wavve_pro\\WavvePro.Release.0.3.0.0.bin";
        dto.setFirmwareData(StreamUtils.copyToByteArray(new FileInputStream(firmwareFilePath)));
        return serverRequestRadarHandler.upgradeFirmware(dto);
    }

}
