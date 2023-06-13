
package wi24rd.radar.vital.tcp.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;

import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import wi24rd.radar.vital.tcp.connection.ConnectionUtil;
import wi24rd.radar.vital.tcp.connection.RadarAddressMap;
import wi24rd.radar.vital.tcp.exception.RadarException;
import wi24rd.radar.vital.tcp.handler.base.RadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.handler.base.RadarProtocolDataHandlerManager;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;
import wi24rd.radar.vital.tcp.server.RadarTcpServer;
import wi24rd.radar.vital.tcp.util.ByteUtil;

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
        radarProtocolData.setRadarId(ConnectionUtil.getRadarId(bizContext.getConnection()));
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
        return true;
    }

    /**
     */
    public static String getHardwareVersion(byte[] bytes) {
        if (null == bytes) {
            return "unknown";
        }
        StringBuilder version = new StringBuilder();
        byte[] temp = new byte[4];
        for (byte aByte : bytes) {
            temp[3] = aByte;
            String hexString = String.format("%02d", ByteUtil.byte4ToInt(temp));
            version.append(hexString).append(".");
        }
        version.deleteCharAt(version.length() - 1);
        return version.toString();
    }

}
