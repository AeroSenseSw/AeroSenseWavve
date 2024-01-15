package com.aerosense.radar.tcp.server;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/17 10:51
 * @version: 1.0
 */
public abstract class RadarTcpServerRegister {
    @Autowired
    RadarTcpServer radarTcpServer;

    @PostConstruct
    public void register(){
        registerToRadarTcpServer(radarTcpServer);
    }

    /**
     * 执行注册动作
     * @param radarTcpServer
     */
    protected abstract void registerToRadarTcpServer(RadarTcpServer radarTcpServer);

    protected RadarTcpServer getRadarTcpServer(){
        return radarTcpServer;
    }
}
