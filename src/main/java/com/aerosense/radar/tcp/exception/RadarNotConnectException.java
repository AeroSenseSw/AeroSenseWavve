package com.aerosense.radar.tcp.exception;
/**
 * @description: Radar not connect
 * @author jia.wu
 * @date 2024/1/11 11:09
 * @version 1.0.0
 */
public class RadarNotConnectException extends RadarException {
    public RadarNotConnectException() {
    }

    public RadarNotConnectException(String message) {
        super(message);
    }

    public RadarNotConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RadarNotConnectException(Throwable cause) {
        super(cause);
    }

    public RadarNotConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
