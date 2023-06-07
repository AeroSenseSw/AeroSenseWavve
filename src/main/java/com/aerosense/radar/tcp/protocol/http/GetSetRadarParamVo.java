package com.aerosense.radar.tcp.protocol.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 16:43
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSetRadarParamVo {

    private String radarId;
    private Integer code;
    private Object data;

}
