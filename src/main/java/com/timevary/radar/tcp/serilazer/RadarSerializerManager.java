
package com.timevary.radar.tcp.serilazer;

import com.alipay.remoting.serialization.SerializerManager;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/4 11:30
 * @version: 1.0
 */
public class RadarSerializerManager {
    public static void initSerializer() {
        SerializerManager.addSerializer(RadarSerializer.IDX, new RadarSerializer());
    }
}
