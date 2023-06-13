package wi24rd.radar.vital.tcp.connection;

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

    public static String getRadarId(Connection connection){
        Object obj = connection.getAttribute(ATTR_RADAR_Id);
        return obj==null?null:obj.toString();
    }
    public static String getRadarVersion(Connection connection){
        Object obj = connection.getAttribute(ATTR_VERSION);
        return obj==null?null:obj.toString();
    }

}
