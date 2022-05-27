
package com.timevary.radar.tcp.server;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.LifeCycleException;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.config.switches.GlobalSwitch;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.google.common.base.Strings;
import com.timevary.radar.tcp.config.RadarTcpServerProperties;
import com.timevary.radar.tcp.connection.DisconnectServerEventProcessor;
import com.timevary.radar.tcp.connection.RadarAddressHashMap;
import com.timevary.radar.tcp.connection.RadarAddressMap;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandler;
import com.timevary.radar.tcp.hander.base.RadarProtocolDataHandlerManager;
import com.timevary.radar.tcp.hander.callback.RadarHandlerCallBack;
import com.timevary.radar.tcp.processor.RadarProtocolDataServerAsyncProcessor;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.protocol.RadarProtocolManager;
import com.timevary.radar.tcp.serilazer.RadarSerializer;
import com.timevary.radar.tcp.serilazer.RadarSerializerManager;
import com.timevary.radar.tcp.service.fromRadar.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/3 16:23
 * @version: 1.0
 */
@Slf4j
public class RadarTcpServer extends RpcServer {
    private String serverAddress;
    private RadarAddressMap radarAddressMap;
    private RadarTcpServerProperties radarTcpServerProperties;


    static {
        //初始化注册雷达协议
        RadarProtocolManager.initProtocols();
        log.info("initialize radar protocol register successful.");
        //初始化注册雷达序列化器
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
        //同步停止服务器
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

    public static RadarTcpServer radarServerStarter( RadarProtocolDataHandler... handlers) {
        List<RadarProtocolDataHandler> radarProtocolDataHandlers = Arrays.asList(handlers);
        return radarServerStarter(radarProtocolDataHandlers);
    }

    public static RadarTcpServer radarServerStarter( RadarHandlerCallBack callBack) {
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
        return radarServerStarter(handlers);
    }

    public static RadarTcpServer radarServerStarter( List<RadarProtocolDataHandler> handlers) {
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
     * 采用雷达协议数据同步调用雷达
     * @param requestObj
     * @param timeoutMillis
     * @return
     * @throws RemotingException
     * @throws InterruptedException
     */
    public Object invokeSync(RadarProtocolData requestObj, int timeoutMillis)
            throws RemotingException, InterruptedException {
        InvokeContext invokeContext = new InvokeContext();
        return invokeSync(requestObj, invokeContext, timeoutMillis);
    }

    /**
     * 采用雷达协议数据同步调用雷达
     * @param requestObj
     * @param invokeContext
     * @param timeoutMillis
     * @return
     * @throws RemotingException
     * @throws InterruptedException
     */
    public Object invokeSync(RadarProtocolData requestObj, InvokeContext invokeContext, int timeoutMillis)
            throws RemotingException, InterruptedException {
        String radarAddress = getRadarAddress(requestObj.getRadarId());
        if(Strings.isNullOrEmpty(radarAddress)){
            log.error("失败");
            return null;
//            throw new Exception("radar ["+requestObj.getRadarId()+"] not connect in this server-"+getServerAddress());
        }
        invokeContext.put(InvokeContext.BOLT_CUSTOM_SERIALIZER, RadarSerializer.IDX_BYTE);
        return super.invokeSync(radarAddress, requestObj, invokeContext, timeoutMillis);
    }
}