package com.aerosense.radar.tcp.protocol.http;

/**
 * @author ：ywb
 * @date ：Created in 2021/12/8 14:14
 * @modified By：
 */
public class ResponseResult {

    private Integer code;
    private String msg;
    private Object obj;

    public ResponseResult() {
    }

    public ResponseResult(ResponseCode responseCode, Object obj) {
        this.code = responseCode.code;
        this.msg = responseCode.msg;
        this.obj = obj;
    }

    public ResponseResult(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.msg = responseCode.msg;
    }

    public ResponseResult(int function, int msg) {
        this.code = function;
        this.msg = msg == 1 ? "成功" : "失败";
        this.obj = msg;
    }

    public ResponseResult(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
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

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
