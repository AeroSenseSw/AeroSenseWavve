package com.aerosense.radar.tcp.util;

import com.alipay.remoting.util.StringUtils;
import com.aerosense.radar.tcp.protocol.http.GetSetRadarParamVo;
import com.aerosense.radar.tcp.protocol.http.ResponseCode;
import com.aerosense.radar.tcp.protocol.http.ResponseResult;
import com.aerosense.radar.tcp.server.RadarTcpServer;

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
