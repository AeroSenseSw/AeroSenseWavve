package com.aerosense.radar.tcp.serilazer;

import com.alipay.remoting.serialization.Serializer;
import com.alipay.remoting.serialization.SerializerManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/4 11:30
 * @version: 1.0
 */
public class RadarSerializerManager {
    private static final AtomicBoolean INITED = new AtomicBoolean();
    static {
        initSerializer();
    }
    public static void initSerializer() {
        if (INITED.compareAndSet(false, true)) {
            Serializer serializer = SerializerManager.getSerializer(RadarSerializer.IDX);
            if (serializer == null) {
                SerializerManager.addSerializer(RadarSerializer.IDX, new RadarSerializer());
            }
        }
    }
}
