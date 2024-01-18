package com.aerosense.radar.tcp.config;


import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import com.aerosense.radar.tcp.connection.RadarAddressHashMap;
import com.aerosense.radar.tcp.connection.RadarAddressMap;
import com.aerosense.radar.tcp.service.fromRadar.*;
import com.aerosense.radar.tcp.util.InetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;


/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/9 11:58
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(value = {RadarTcpServerProperties.class})
@ConditionalOnProperty(prefix = "radar.tcp", name = "enable", havingValue = "true")
@Slf4j
public class RadarTcpServerAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {
    private final String serverAddress;
    private final RadarTcpServerProperties radarTcpServerProperties;

    public RadarTcpServerAutoConfiguration(RadarTcpServerProperties radarTcpServerProperties) {
        this.radarTcpServerProperties = radarTcpServerProperties;
        if (StringUtils.isEmpty(radarTcpServerProperties.getHost())) {
            InetUtils.setUseOnlySiteLocalInterface(radarTcpServerProperties.isUseOnlySiteLocalInterface());
            InetUtils.setPreferHostnameOverIP(radarTcpServerProperties.isPreferHostnameOverIP());
            InetUtils.addPreferredNetworks(radarTcpServerProperties.getPreferredeNetworks());
            InetUtils.addIgnoredInterfaces(radarTcpServerProperties.getIgnoredInterfaces());
            serverAddress = InetUtils.parseSelfIp() + ":" + radarTcpServerProperties.getPort();
        } else {
            serverAddress = radarTcpServerProperties.getHost() + ":" + radarTcpServerProperties.getPort();
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "radar.tcp", name = "addressMap", havingValue = "HashMap", matchIfMissing = true)
    public RadarAddressMap radarAddressHashMap() {
        return new RadarAddressHashMap(serverAddress);
    }

    @Bean(name = "radarTcpServer", destroyMethod = "destroy")
    public RadarTcpServer radarTcpServer(RadarAddressMap radarAddressMap,
                                         @Value("${logging.file.path:./logs}") String loggingPath) {
        System.setProperty("logging.path", loggingPath);
        System.out.println("using logging path: " + loggingPath);
        if (radarTcpServerProperties.getPort() < 1024) {
            throw new RuntimeException("port must > 1024");
        }
        return new RadarTcpServer(radarAddressMap, radarTcpServerProperties);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            RadarTcpServer radarTcpServer = contextRefreshedEvent.getApplicationContext().getBean(RadarTcpServer.class);
            if (!radarTcpServer.isStarted()) {
                RadarHandlerCallBack radarHandlerCallBack = contextRefreshedEvent.getApplicationContext().getBean(RadarHandlerCallBack.class);
                radarTcpServer.registerHandler(new BreathHeightBpmHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new BreathLowBpmHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new CardiacArrestHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new HeartRateHeightBpmHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new HeartRateLowBpmHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new LiveBedNoBackHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new LongTimeNoTurnOverHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new RadarReportHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new RespiratoryArrestHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new CreateConnectionHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new RollOverOrSitAndCallThePoliceHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new PhysicalActivityReportStatisticsHandler(radarHandlerCallBack));
                radarTcpServer.registerHandler(new FallDetectHandler(radarHandlerCallBack));

                radarTcpServer.startup();
                log.info("radar tcp server auto started on {}", radarTcpServer.getServerAddress());
            }
        }
    }
}
