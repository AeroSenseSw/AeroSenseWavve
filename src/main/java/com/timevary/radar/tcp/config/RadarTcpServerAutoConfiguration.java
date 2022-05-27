package com.timevary.radar.tcp.config;


import com.timevary.radar.tcp.server.RadarTcpServer;
import com.timevary.radar.tcp.connection.RadarAddressHashMap;
import com.timevary.radar.tcp.connection.RadarAddressMap;
import com.timevary.radar.tcp.util.InetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/9 11:58
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(value = {RadarTcpServerProperties.class})
@ConditionalOnProperty(prefix = "radar.tcp", name = "enable", havingValue = "true")
public class RadarTcpServerAutoConfiguration {
    private final String serverAddress;
    private final RadarTcpServerProperties radarTcpServerProperties;

    public RadarTcpServerAutoConfiguration(RadarTcpServerProperties radarTcpServerProperties) {
        this.radarTcpServerProperties = radarTcpServerProperties;
        if(StringUtils.isEmpty(radarTcpServerProperties.getHost())){
            InetUtils.setUseOnlySiteLocalInterface(radarTcpServerProperties.isUseOnlySiteLocalInterface());
            InetUtils.setPreferHostnameOverIP(radarTcpServerProperties.isPreferHostnameOverIP());
            InetUtils.addPreferredNetworks(radarTcpServerProperties.getPreferredeNetworks());
            InetUtils.addIgnoredInterfaces(radarTcpServerProperties.getIgnoredInterfaces());
            InetUtils.parseSelfIp();
            serverAddress = InetUtils.getSelfIP() + ":" + radarTcpServerProperties.getPort();
        }else{
            serverAddress = radarTcpServerProperties.getHost() + ":" + radarTcpServerProperties.getPort();
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "radar.tcp", name ="addressMap", havingValue = "HashMap", matchIfMissing = true)
    public RadarAddressMap radarAddressHashMap(){
        return new RadarAddressHashMap(serverAddress);
    }

    @Bean(name = "radarTcpServer", destroyMethod = "destroy")
    public RadarTcpServer radarTcpServer(RadarAddressMap radarAddressMap,
                                         @Value("${logging.file.path:./logs}")String loggingPath){
        System.setProperty("logging.path", loggingPath);
        System.out.println("using logging path: "+loggingPath);
        if(radarTcpServerProperties.getPort()<1024){
            throw new RuntimeException("port must > 1024");
        }
        return new RadarTcpServer(radarAddressMap, radarTcpServerProperties);
    }
}
