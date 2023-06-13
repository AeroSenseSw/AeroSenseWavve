package com.radar.vital.tcp.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/9 13:36
 * @version: 1.0
 */

@ConfigurationProperties(prefix = "radar.tcp")
@Data
public class RadarTcpServerProperties implements Serializable {
    /**是否开启雷达tcp服务*/
    private boolean enable;
    /**雷达连接地址与id映射关系存储方式：HashMap, RedisMap*/
    private String addressMap = "HashMap";
    /**tcp监听主机地址，默认： 0.0.0.0*/
    private String host = "0.0.0.0";
    /**tcp监听端口，必须设置1024以上端口，默认：8801*/
    private int port = 8899;
    /**默认服务器idle超时时间为70秒*/
    private long idleTimeout = 90000;

    /**未指定host情况下希望注册的服务器ip前缀*/
    private List<String> preferredeNetworks = new ArrayList<>();
    /**未指定host情况下希望忽略的网卡*/
    private List<String> ignoredInterfaces = new ArrayList<>();
    /**更喜欢主机名注册*/
    private boolean preferHostnameOverIP;
    /**仅使用站点本地接口*/
    private boolean useOnlySiteLocalInterface;
}
