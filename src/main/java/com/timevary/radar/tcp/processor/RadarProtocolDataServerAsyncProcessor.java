
package com.timevary.radar.tcp.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.timevary.radar.tcp.server.RadarTcpServer;
import com.timevary.radar.tcp.connection.ConnectionUtil;
import com.timevary.radar.tcp.connection.RadarAddressMap;
import com.timevary.radar.tcp.exception.RadarException;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandler;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandlerManager;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.service.toRadar.GetRadarIdHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/6 10:41
 * @version: 1.0
 */
@Slf4j
public class RadarProtocolDataServerAsyncProcessor extends AsyncUserProcessor<RadarProtocolData> {

    private final RadarAddressMap radarAddressMap;
    private final RadarTcpServer radarTcpServer;
    private final GetRadarIdHandler getRadarIdHandler = new GetRadarIdHandler();

    public RadarProtocolDataServerAsyncProcessor(RadarAddressMap radarAddressMap,
                                                 RadarTcpServer radarTcpServer) {
        this.radarAddressMap = radarAddressMap;
        this.radarTcpServer = radarTcpServer;
    }

    @Override
    public void handleRequest(BizContext bizContext, AsyncContext asyncContext, RadarProtocolData radarProtocolData) {
        if (log.isDebugEnabled()) {
            log.debug("request: {}", radarProtocolData);
        }
        registerRadarOnFirstRadarReport(bizContext, radarProtocolData);
        RadarProtocolDataHandler handler = RadarProtocolDataHandlerManager.getHandler(radarProtocolData.getFunction());
        if (handler == null) {
            if (log.isWarnEnabled()) {
                log.warn("no handler response: null {} {}", radarProtocolData.getRadarId(),
                        radarProtocolData.getFunction());
            }
            asyncContext.sendResponse(null);
            return;
        }
        try {
            Object result = handler.process(radarProtocolData);
            if (log.isDebugEnabled()) {
                log.debug("response: {} - {} - {}", result,
                        radarProtocolData.getRadarId(), radarProtocolData.getFunction());
            }
            asyncContext.sendResponse(result);
        } catch (Exception e) {
            log.error("handler process happen exception", e);
            asyncContext.sendResponse(new RadarException(e));
        }
    }

    private void registerRadarOnFirstRadarReport(BizContext bizContext, RadarProtocolData radarProtocolData) {
        Connection connection = bizContext.getConnection();
        if (radarProtocolData.getFunction() == FunctionEnum.createConnection
                || radarProtocolData.getFunction() == FunctionEnum.radarReport) {
            if (connection.getAttribute(ConnectionUtil.ATTR_RADAR_Id) == null) {
                String remoteAddress = bizContext.getRemoteAddress();
                if (remoteAddress == null || remoteAddress.length() == 0) {
                    log.warn("register radar failure, remote address parse to be null");
                    return;
                }
                if (radarProtocolData.getRadarId() == null || "".equals(radarProtocolData.getRadarId())) {
                    //当是雷达上报的时候主动去获取雷达id
                    String radarId = getRadarIdHandler.process(radarTcpServer, remoteAddress);
                    radarProtocolData.setRadarId(radarId);
                }
                connection.setAttributeIfAbsent(ConnectionUtil.ATTR_RADAR_Id, radarProtocolData.getRadarId());
                radarAddressMap.bindAddress(remoteAddress, radarProtocolData.getRadarId());
                log.info("register radar successful {} - {}", radarProtocolData.getRadarId(), remoteAddress);
            }
        }
    }

    @Override
    public String interest() {
        return RadarProtocolData.class.getName();
    }

    @Override
    public boolean timeoutDiscard() {
        return true;// true表示开启自动丢弃，false表示关闭自动丢弃，用户在之后的处理processor里，可自行决策
    }
}
