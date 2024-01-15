
package com.aerosense.radar.tcp.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/6 11:19
 * @version: 1.0
 */
@Slf4j
public class StringClientAsyncProcessor extends AsyncUserProcessor<String> {

    @Override
    public void handleRequest(BizContext bizContext, AsyncContext asyncContext, String data) {
        if (log.isDebugEnabled()) {
            log.debug("response data: {}", data);
        }
    }

    @Override
    public String interest() {
        return String.class.getName();
    }
}
