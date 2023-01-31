package com.aerosense.radar.tcp.protocol.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/17 14:23
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RadarInfo {
    private Integer code;
    private Object obj;

    public RadarInfo(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.obj = responseCode.msg;
    }
}
