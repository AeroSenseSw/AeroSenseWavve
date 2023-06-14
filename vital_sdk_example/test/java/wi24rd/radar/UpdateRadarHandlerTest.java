package wi24rd.radar;

import com.alipay.remoting.exception.RemotingException;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import wi24rd.radar.Application;
import wi24rd.radar.vital.tcp.server.RadarTcpServer;
import wi24rd.radar.vital.tcp.service.toRadar.UpdateRadarHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Set;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class UpdateRadarHandlerTest {
    @Autowired
    UpdateRadarHandler updateRadarHandler;
    @Autowired
    RadarTcpServer radarTcpServer;

    @Test
    public void testUpdateRadar() throws InterruptedException, RemotingException, IOException {
        //String firmwareFilePath = "D:\\Radar.Tcp.A.Release.1.7.4.42.bin";
        String firmwareFilePath = "/home/w/Documents/vital.1.7.4.46.bin";
        Set<String> onlineRadarList = radarTcpServer.getOnlineRadarList();
        while (onlineRadarList.isEmpty()){
            log.info("Wait a radar connect server");
            Thread.sleep(2000);
        }
        Thread.sleep(2000);
        String radarId = Lists.newArrayList(onlineRadarList).get(0);
        log.info("Prepare update radar firmware {}", radarId);
        boolean processResult = updateRadarHandler.process(radarId, firmwareFilePath);
        Assertions.assertTrue(processResult,"Process update radar firmware fail");
    }
}