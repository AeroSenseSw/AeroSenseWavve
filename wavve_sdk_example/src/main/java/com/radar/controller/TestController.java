package com.radar.controller;

import com.radar.vital.tcp.protocol.FunctionEnum;
import com.radar.vital.tcp.protocol.RadarProtocolData;
import com.radar.vital.tcp.service.toRadar.GetReportInterval;
import com.radar.vital.tcp.service.toRadar.SetRadarParams;
import com.radar.vital.tcp.util.ByteUtil;
import com.alipay.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：radar
 * @date ：Created in 2022/11/29 16:19
 * @modified By：
 */
@RestController
public class TestController {

    @Autowired
    private SetRadarParams setRadarParams;
    @Autowired
    private GetReportInterval getReportInterval;

    @RequestMapping("/testGet")
    public Object testGet() throws Exception {
        return getReportInterval.process("13360111504B57313534033855");
    }

    @RequestMapping("/testSet")
    public Object testSet() throws RemotingException, InterruptedException {
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        radarProtocolData.setRadarId("13360111504B57313534033855");
        radarProtocolData.setFunction(FunctionEnum.setReportInterval);
        radarProtocolData.setData(ByteUtil.intToByteBig(100));
        return setRadarParams.process(radarProtocolData);
    }

}
