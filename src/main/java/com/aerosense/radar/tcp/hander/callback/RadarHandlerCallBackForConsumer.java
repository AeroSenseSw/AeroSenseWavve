package com.aerosense.radar.tcp.hander.callback;

import com.aerosense.radar.tcp.domain.dto.CallBackDto;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBackForConsumer {
    void callBack(CallBackDto callBackDto);
}
