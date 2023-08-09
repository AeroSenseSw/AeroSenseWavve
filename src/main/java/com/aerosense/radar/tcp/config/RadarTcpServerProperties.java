package com.aerosense.radar.tcp.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/9 13:36
 * @version: 1.0
 */

@ConfigurationProperties(prefix = "radar.tcp")
@Data
public class RadarTcpServerProperties implements Serializable {
    /**enable radar tcp service*/
    private boolean enable;
    /**radar connection address and id mapping and relationship storage method：HashMap, RedisMap*/
    private String addressMap = "HashMap";
    /**tcp listening host address, default： 0.0.0.0*/
    private String host = "0.0.0.0";
    /**tcp listening port must be set to a port above 1024，default：8801*/
    private int port = 8899;
    /**default server idle timeout is 70 seconds*/
    private long idleTimeout = 90000;

    /**The IP prefix of the server you want to register when the host is not specified*/
    private List<String> preferredeNetworks = new ArrayList<>();
    /**The network card that you want to ignore when the host is not specified*/
    private List<String> ignoredInterfaces = new ArrayList<>();
    /**prefer host name*/
    private boolean preferHostnameOverIP;
    /**use only site local interface*/
    private boolean useOnlySiteLocalInterface;
}
