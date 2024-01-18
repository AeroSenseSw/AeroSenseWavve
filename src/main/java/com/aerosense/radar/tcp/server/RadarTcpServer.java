
package com.aerosense.radar.tcp.server;

import com.aerosense.radar.tcp.config.RadarTcpServerProperties;
import com.aerosense.radar.tcp.connection.DisconnectServerEventProcessor;
import com.aerosense.radar.tcp.connection.RadarAddressHashMap;
import com.aerosense.radar.tcp.connection.RadarAddressMap;
import com.aerosense.radar.tcp.exception.RadarNotConnectException;
import com.aerosense.radar.tcp.hander.base.RadarProtocolDataHandler;
import com.aerosense.radar.tcp.hander.base.RadarProtocolDataHandlerManager;
import com.aerosense.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.aerosense.radar.tcp.processor.RadarProtocolDataServerAsyncProcessor;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.service.fromRadar.*;
import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.LifeCycleException;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.config.switches.GlobalSwitch;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcResponseFuture;
import com.alipay.remoting.rpc.RpcServer;
import com.google.common.base.Strings;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolManager;
import com.aerosense.radar.tcp.serilazer.RadarSerializer;
import com.aerosense.radar.tcp.serilazer.RadarSerializerManager;
import com.aerosense.radar.tcp.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/3 16:23
 * @version: 1.0
 */
@Slf4j
public class RadarTcpServer extends RpcServer {
    private String serverAddress;
    private RadarAddressMap radarAddressMap;
    private RadarTcpServerProperties radarTcpServerProperties;


    static {
        RadarProtocolManager.initProtocols();
        log.info("initialize radar protocol register successful.");
        RadarSerializerManager.initSerializer();
        log.info("initialize radar protocol serializer successful.");
    }

    private void initRadarProtocolDataProcessor() {
        this.registerUserProcessor(new RadarProtocolDataServerAsyncProcessor(radarAddressMap, this));
        log.info("init radar protocol data processor successful");
    }

    private void initServerConnectEventProcess() {
        this.addConnectionEventProcessor(ConnectionEventType.CLOSE, new DisconnectServerEventProcessor(radarAddressMap));
        log.info("add radar disconnect server event processor successful");
    }

    public RadarTcpServer() {
    }

    public RadarTcpServer(RadarAddressMap radarAddressMap, RadarTcpServerProperties properties) {
        super(properties.getHost(), properties.getPort(), true);
        this.radarAddressMap = radarAddressMap;
        this.serverAddress = radarAddressMap.getServerAddress();
        this.radarTcpServerProperties = properties;
        log.info("new instance radar tcp server successful");
    }

    public String getRadarAddress(String radarId) {
        return radarAddressMap.getRadarAddress(radarId);
    }

    public String getServerAddress(String radarId) {
        return radarAddressMap.getServerAddress(radarId);
    }

    public Set<String> getOnlineRadarList() {
        return radarAddressMap.getOnlineRadarList();
    }

    public long getOnlineRadarCount() {
        return radarAddressMap.getOnlineRadarCount();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    protected void doInit() throws LifeCycleException {
        log.info("initializing radar tcp server");
        System.setProperty(Configs.TCP_SERVER_IDLE, String.valueOf(radarTcpServerProperties.getIdleTimeout()));
        switches().turnOn(GlobalSwitch.SERVER_SYNC_STOP);
        initRadarProtocolDataProcessor();
        initServerConnectEventProcess();
        super.doInit();
    }

    @Override
    protected boolean doStop() throws LifeCycleException {
        log.info("stopping radar tcp server");
        boolean stopped = super.doStop();
        RadarProtocolDataHandlerManager.clear();
        log.info("radar protocol data handler clear successful");
        radarAddressMap.clear();
        log.info("radar address map clear successful");
        return stopped;
    }

    public void registerHandler(RadarProtocolDataHandler handler) {
        RadarProtocolDataHandlerManager.registerHandler(handler);
    }

    private void destroy() {
        log.info("destroying radar tcp server");
        if (this.isStarted()) {
            this.shutdown();
        }
    }

    public static RadarTcpServer radarServerStarter(RadarProtocolDataHandler... handlers) {
        List<RadarProtocolDataHandler> radarProtocolDataHandlers = Arrays.asList(handlers);
        return radarServerStarter(radarProtocolDataHandlers);
    }

    public static RadarTcpServer radarServerStarter(RadarHandlerCallBack callBack) {
        List<RadarProtocolDataHandler> handlers = new ArrayList<>();
        handlers.add(new BreathHeightBpmHandler(callBack));
        handlers.add(new BreathLowBpmHandler(callBack));
        handlers.add(new CardiacArrestHandler(callBack));
        handlers.add(new HeartRateHeightBpmHandler(callBack));
        handlers.add(new HeartRateLowBpmHandler(callBack));
        handlers.add(new LiveBedNoBackHandler(callBack));
        handlers.add(new LongTimeNoTurnOverHandler(callBack));
        handlers.add(new RadarReportHandler(callBack));
        handlers.add(new RespiratoryArrestHandler(callBack));
        handlers.add(new CreateConnectionHandler(callBack));
        handlers.add(new RollOverOrSitAndCallThePoliceHandler(callBack));
        handlers.add(new PhysicalActivityReportStatisticsHandler(callBack));
        handlers.add(new FallDetectHandler(callBack));
        return radarServerStarter(handlers);
    }

    public static RadarTcpServer radarServerStarter(List<RadarProtocolDataHandler> handlers) {
        System.setProperty("logging.path", "./logs/server");
        String serverAddress = "127.0.0.1:8899";
        RadarAddressMap radarAddressMap = new RadarAddressHashMap(serverAddress);
        RadarTcpServerProperties properties = new RadarTcpServerProperties();
        RadarTcpServer radarTcpServer = new RadarTcpServer(radarAddressMap, properties);
        for (RadarProtocolDataHandler handler : handlers) {
            radarTcpServer.registerHandler(handler);
        }
        log.info("radarTcpServer start at 8899");
        radarTcpServer.startup();
        return radarTcpServer;
    }

    /**
     */
    public Object invokeSync(RadarProtocolData requestObj, int timeoutMillis)
            throws RemotingException, InterruptedException {
        InvokeContext invokeContext = new InvokeContext();
        return invokeSync(requestObj, invokeContext, timeoutMillis);
    }

    /**
     */
    public Object invokeSync(RadarProtocolData requestObj, InvokeContext invokeContext, int timeoutMillis)
            throws RemotingException, InterruptedException {
        Connection radarConnection = getRadarConnectionThrow(requestObj.getRadarId());
        return invokeSyncWithConnection(radarConnection, requestObj, invokeContext, timeoutMillis);
    }

    public Object invokeSyncWithConnection(Connection connection, RadarProtocolData requestObj, int timeoutMillis)
            throws RemotingException, InterruptedException {
        InvokeContext invokeContext = new InvokeContext();
        return invokeSyncWithConnection(connection, requestObj, invokeContext, timeoutMillis);
    }

    public Object invokeSyncWithAddress(String address, RadarProtocolData requestObj, int timeoutMillis)
            throws RemotingException, InterruptedException {
        InvokeContext invokeContext = new InvokeContext();
        Connection connection = getConnection(address);
        return invokeSyncWithConnection(connection, requestObj, invokeContext, timeoutMillis);
    }

    public Object invokeSyncWithConnection(Connection connection, RadarProtocolData requestObj, InvokeContext invokeContext, int timeoutMillis)
            throws RemotingException, InterruptedException {
        invokeContext.put(InvokeContext.BOLT_CUSTOM_SERIALIZER, RadarSerializer.IDX_BYTE);
        return super.invokeSync(connection, requestObj, invokeContext, timeoutMillis);
    }

    /**
     */
    public void softRebootRadar(Connection connection) {
        log.debug("soft reboot the radar {}", connection.getRemoteAddress());
        RadarProtocolData rebootData = RadarProtocolData.newFunctionInstance(FunctionEnum.softReboot,
                ByteUtil.intToByteBig(0));
        try {
            invokeAsyncWithConnection(connection, rebootData);
        } catch (RemotingException e) {
            log.error("soft reboot radar error {}", e, connection.getRemoteAddress());
        }
    }


    /**
     */
    public RpcResponseFuture invokeAsyncWithConnection(Connection connection, RadarProtocolData requestObj)
            throws RemotingException {
        InvokeContext invokeContext = new InvokeContext();
        invokeContext.put(InvokeContext.BOLT_CUSTOM_SERIALIZER, RadarSerializer.IDX_BYTE);
        return invokeWithFuture(connection, requestObj, invokeContext, 3000 * 10);
    }

    public Connection getConnection(String address) {
        return getConnectionManager().get(address);
    }

    public Connection getRadarConnection(String radarId) {
        String radarAddress = getRadarAddress(radarId);
        if (Objects.nonNull(radarAddress)) {
            return getConnection(radarAddress);
        }
        return null;
    }

    public Connection getRadarConnectionThrow(String radarId) {
        Connection radarConnection = getRadarConnection(radarId);
        if (radarConnection == null) {
            throw new RadarNotConnectException("radar not connect "+radarId);
        }
        return radarConnection;
    }

    public boolean isRadarInThisServer(String radarId) {
        return getRadarConnection(radarId)!=null;
    }
}