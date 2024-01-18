
package com.aerosense.radar.tcp.processor;

import com.aerosense.radar.tcp.connection.ConnectionUtil;
import com.aerosense.radar.tcp.connection.RadarAddressMap;
import com.aerosense.radar.tcp.hander.base.RadarProtocolDataHandler;
import com.aerosense.radar.tcp.hander.base.RadarProtocolDataHandlerManager;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.protocol.RadarProtocolUtil;
import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import com.aerosense.radar.tcp.exception.RadarException;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.util.ByteUtil;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Locale;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/6 10:41
 * @version: 1.0
 */
@Slf4j
public class RadarProtocolDataServerAsyncProcessor extends AsyncUserProcessor<RadarProtocolData> {

    private final RadarAddressMap radarAddressMap;
    private final RadarTcpServer radarTcpServer;

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
        boolean registerResult = registerOrBindRadar(bizContext, radarProtocolData);
        if (!registerResult) {
            radarTcpServer.softRebootRadar(bizContext.getConnection());
            return;
        }
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

    private boolean registerOrBindRadar(BizContext bizContext, RadarProtocolData radarProtocolData) {
        Connection connection = bizContext.getConnection();
        if (radarProtocolData.getFunction() == FunctionEnum.createConnection) {
            String remoteAddress = bizContext.getRemoteAddress();
            if (remoteAddress == null || remoteAddress.length() == 0) {
                log.warn("register radar failure, remote address parse to be null");
                return false;
            }
            byte[] data = radarProtocolData.getData();
            byte type = data[0];
            String version = RadarProtocolUtil.getHardwareVersion(Arrays.copyOfRange(data, 1, 5));
            byte[] idBytes = Arrays.copyOfRange(data, 5, data.length);
            String id = ByteBufUtil.hexDump(idBytes).toUpperCase(Locale.ROOT);
            boolean bind = ConnectionUtil.bindIdAndVersion(connection, id, version, type);
            if (bind) {
                radarAddressMap.bindAddress(remoteAddress, id);
                log.info("register radar successful {} - {}", id, remoteAddress);
            }
        }
        //绑定数据
        if (StringUtils.isEmpty(ConnectionUtil.getRadarId(bizContext.getConnection()))) {
            return false;
        }
        radarProtocolData.setRadarId(ConnectionUtil.getRadarId(bizContext.getConnection()));
        radarProtocolData.setRadarVersion(ConnectionUtil.getRadarVersion(bizContext.getConnection()));
        return true;
    }

    @Override
    public String interest() {
        return RadarProtocolData.class.getName();
    }

    @Override
    public boolean timeoutDiscard() {
        return true;
    }

}
