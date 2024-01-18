package com.aerosense.radar.tcp.connection;

import com.alipay.remoting.Connection;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/12 10:53
 * @version: 1.0
 */
public class ConnectionUtil {

    public static final String ATTR_RADAR_Id = "radarId";
    public static final String ATTR_VERSION = "radarVersion";
    public static final String ATTR_TYPE = "type";

    public static String getRadarId(Connection connection){
        Object obj = connection.getAttribute(ATTR_RADAR_Id);
        return obj==null?null:obj.toString();
    }

    public static String getRadarVersion(Connection connection){
        Object obj = connection.getAttribute(ATTR_VERSION);
        return obj==null?null:obj.toString();
    }

    public static byte getRadarType(Connection connection){
        Object obj = connection.getAttribute(ATTR_TYPE);
        return obj==null?null: (byte) obj;
    }

    public static boolean bindIdAndVersion(Connection connection, String id, String version, Byte type) {
        Object obj = connection.setAttributeIfAbsent(ATTR_RADAR_Id, id);
        if(obj==null){
            connection.setAttribute(ATTR_VERSION, version);
            connection.setAttribute(ATTR_TYPE, type);
            return true;
        }else{
            connection.setAttribute(ATTR_VERSION, version);
        }
        return false;
    }
}
