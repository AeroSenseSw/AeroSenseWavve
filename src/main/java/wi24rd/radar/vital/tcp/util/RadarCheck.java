package wi24rd.radar.vital.tcp.util;

import com.alipay.remoting.util.StringUtils;

import wi24rd.radar.vital.tcp.protocol.http.GetSetRadarParamVo;
import wi24rd.radar.vital.tcp.protocol.http.ResponseCode;
import wi24rd.radar.vital.tcp.protocol.http.ResponseResult;
import wi24rd.radar.vital.tcp.server.RadarTcpServer;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/17 14:58
 * @modified By：
 */
public class RadarCheck {

    public static ResponseResult check(GetSetRadarParamVo getRadarParamVo, RadarTcpServer radarTcpServer) {
        String radarId = getRadarParamVo.getRadarId();
        if (StringUtils.isBlank(radarId)) {
            return new ResponseResult(ResponseCode.RADAR_ERROR);
        }
        String radarAddress = radarTcpServer.getRadarAddress(radarId);
        if (StringUtils.isEmpty(radarAddress)) {
            return new ResponseResult(ResponseCode.RADAR_NOT_CONNECTED);
        }
        return null;
    }

}
