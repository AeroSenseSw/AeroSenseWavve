package com.timevary.radar.tcp.connection;

import com.alipay.remoting.Connection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/12 10:53
 * @version: 1.0
 */
public class ConnectionUtil {

    public static final String ATTR_RADAR_Id = "radarId";

    public static String getRadarId(Connection connection){
        Object obj = connection.getAttribute(ATTR_RADAR_Id);
        return obj==null?null:obj.toString();
    }

}
