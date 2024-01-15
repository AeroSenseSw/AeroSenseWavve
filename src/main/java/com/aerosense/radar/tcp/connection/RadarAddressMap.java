
package com.aerosense.radar.tcp.connection;

import java.util.Set;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/6 17:15
 * @version: 1.0
 */
public interface RadarAddressMap {
    /**
     */
    void bindAddress(String radarAddress, String radarId);

    /**
     */
    void unbindAddress(String address, String radarId);

    /**
     */
    String getRadarAddress(String radarId);

    /**
     */
    String getServerAddress(String radarId);

    /**
     */
    Set<String> getOnlineRadarList();

    /**
     */
    long getOnlineRadarCount();

    /**
     */
    void clear();

    /**
     */
    String getServerAddress();

}
