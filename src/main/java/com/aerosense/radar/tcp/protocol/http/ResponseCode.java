package com.aerosense.radar.tcp.protocol.http;

/**
 * @author ：ywb
 * @date ：Created in 2021/12/8 14:15
 * @modified By：
 */
public enum ResponseCode {
    Success(200,"success"),
    Fail(201,"fail"),
    TIME_OUT(4001, "time out"),
    RADAR_DISCONNECT(4002, "Radar disconnected"),
    RADAR_ERROR(4003, "Radar format error"),
    RADAR_NOT_CONNECTED(4004, "Radar is not online"),
    GET_FUNCTION_ERROR(4005, "Failed to get current parameter"),
    SERVER_ERROR(4006, "Server unknown error, please try again!"),
    GET_ALL_PARAMS(5000, "Get all radar parameters"),
    ;

    Integer code;
    String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
