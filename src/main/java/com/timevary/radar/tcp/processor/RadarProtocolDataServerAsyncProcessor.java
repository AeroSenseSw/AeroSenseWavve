
package com.timevary.radar.tcp.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.timevary.radar.tcp.server.RadarTcpServer;
import com.timevary.radar.tcp.connection.ConnectionUtil;
import com.timevary.radar.tcp.connection.RadarAddressMap;
import com.timevary.radar.tcp.exception.RadarException;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandler;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandlerManager;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.util.ByteUtil;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Locale;

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
        boolean result = false;
        Connection connection = bizContext.getConnection();
        if (radarProtocolData.getFunction() == FunctionEnum.createConnection) {
            if (connection.getAttribute(ConnectionUtil.ATTR_RADAR_Id) == null) {
                String remoteAddress = bizContext.getRemoteAddress();
                if (remoteAddress == null || remoteAddress.length() == 0) {
                    log.warn("register radar failure, remote address parse to be null");
                    return false;
                }

                byte[] data = radarProtocolData.getData();
                String version = getHardwareVersion(Arrays.copyOfRange(data, 1, 5));
                byte[] idBytes = Arrays.copyOfRange(data, 5, data.length);
                String id = ByteBufUtil.hexDump(idBytes).toUpperCase(Locale.ROOT);

                connection.setAttributeIfAbsent(ConnectionUtil.ATTR_RADAR_Id, id);
                connection.setAttributeIfAbsent(ConnectionUtil.ATTR_VERSION, version);
                radarAddressMap.bindAddress(remoteAddress, id);
                log.info("register radar successful {} - {}", id, remoteAddress);
                result = true;
                radarProtocolData.setRadarId(id);
                radarProtocolData.setRadarVersion(version);
            }
        } else {
            //绑定数据
            if (StringUtils.isEmpty(ConnectionUtil.getRadarId(bizContext.getConnection()))) {
                return false;
            }
            radarProtocolData.setRadarId(ConnectionUtil.getRadarId(bizContext.getConnection()));
            radarProtocolData.setRadarVersion(ConnectionUtil.getRadarVersion(bizContext.getConnection()));
            result = true;
        }
        return result;
    }

    @Override
    public String interest() {
        return RadarProtocolData.class.getName();
    }

    @Override
    public boolean timeoutDiscard() {
        // true表示开启自动丢弃，false表示关闭自动丢弃，用户在之后的处理processor里，可自行决策
        return true;
    }

    /**
     * 获取雷达固件版本
     */
    public static String getHardwareVersion(byte[] bytes) {
        if (null == bytes) {
            return "unknown";
        }
        StringBuilder version = new StringBuilder();
        //将每个字节转换成Int
        byte[] temp = new byte[4];
        for (byte aByte : bytes) {
            temp[3] = aByte;
            //如果是个位数前补零
            String hexString = String.format("%02d", ByteUtil.byte4ToInt(temp));
            version.append(hexString).append(".");
        }
        version.deleteCharAt(version.length() - 1);
        return version.toString();
    }

}
