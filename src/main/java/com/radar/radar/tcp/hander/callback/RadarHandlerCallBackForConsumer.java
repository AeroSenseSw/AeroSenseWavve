package com.radar.radar.tcp.hander.callback;

import com.radar.radar.tcp.domain.dto.CallBackDto;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBackForConsumer {
    void callBack(CallBackDto callBackDto);
}
