package com.timevary.radar.tcp.hander.callback;

import com.timevary.radar.tcp.domain.dto.CallBackDto;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:46
 * @modified By：
 */
public interface RadarHandlerCallBackForConsumer {
    void callBack(CallBackDto callBackDto);
}
