
package com.timevary.radar.tcp.connection;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/6 17:15
 * @version: 1.0
 */
public interface RadarAddressMap {
    /**
     * 绑定雷达id到连接地址的映射关系，如果之前绑定过则覆盖绑定关系
     * @param radarAddress
     * @param radarId
     */
    void bindAddress(String radarAddress, String radarId);

    /**
     * 解绑雷达id到连接地址和服务器地址的映射关系
     * @param address
     * @param radarId
     */
    void unbindAddress(String address, String radarId);

    /**
     * 获取雷达id映射的连接地址
     * @param radarId
     * @return
     */
    String getRadarAddress(String radarId);

    /**
     * 获取雷达id映射的服务器连接地址
     * @param radarId
     * @return
     */
    String getServerAddress(String radarId);

    /**
     * 获取本服务器连接的在线雷达列表
     * @return
     */
    Set<String> getOnlineRadarList();

    /**
     * 获取本服务器连接的在线雷达数量
     * @return
     */
    long getOnlineRadarCount();

    /**
     * 清除
     */
    void clear();

    /**
     * 获取服务器的连接地址
     * @return
     */
    String getServerAddress();

}
