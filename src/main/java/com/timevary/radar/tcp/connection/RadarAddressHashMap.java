
package com.timevary.radar.tcp.connection;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/6 17:19
 * @version: 1.0
 */
public class RadarAddressHashMap extends AbsractRadarAddressMap implements RadarAddressMap {
    private Map<String, String> radarIdAddressMap = new ConcurrentHashMap<>();

    public RadarAddressHashMap(String serverAddress) {
        super(serverAddress);
    }

    @Override
    public void bindAddress(String radarAddress, String radarId) {
        //9ac525d66003---127.0.0.1:51298
        radarIdAddressMap.put(radarId, radarAddress);
    }

    @Override
    public void unbindAddress(String radarId, String address) {
        radarIdAddressMap.remove(radarId, address);
    }

    @Override
    public String getRadarAddress(String radarId) {
        return radarIdAddressMap.get(radarId);
    }

    @Override
    public String getServerAddress(String radarId) {
        return getServerAddress();
    }

    @Override
    public Set<String> getOnlineRadarList() {
        return radarIdAddressMap.keySet();
    }

    @Override
    public long getOnlineRadarCount() {
        return radarIdAddressMap.size();
    }

    @Override
    public void clear() {
        radarIdAddressMap.clear();
    }
}
