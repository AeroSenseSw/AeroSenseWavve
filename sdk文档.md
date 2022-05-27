## 一，架构解析 

### 1，sdk启动流程图

https://www.processon.com/diagraming/6290877c5653bb788c7a3617

### 2，雷达服务器交互流程图

https://www.processon.com/diagraming/6290877c5653bb788c7a3617

```java

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 10:35
 * @modified By：
 * 呼吸低bpm报警 
 * 雷达主动报警，会调用到对应的协议处理器，然后调用雷达内置回调
 */
@Service
public class BreathLowBpmHandler extends AbstractFromRadarProtocolDataHandler {

    public BreathLowBpmHandler(RadarHandlerCallBack handlerCallBack) {
        super(handlerCallBack);
    }

    @Override
    public Object process(RadarProtocolData protocolData) {
        //处理数据，不须返回
        protocolData.setFunction(FunctionEnum.breathLowBpmAlert);
        handlerCallBack.callBack(protocolData);
        return null;
    }

    @Override
    public Set<FunctionEnum> interests() {
        return Sets.newHashSet(FunctionEnum.breathLowBpmAlert);
    }
}

```

​																										图1

```java

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 11:03
 * @modified By：
 * 雷达内置回调类
 * 用户需自定义回调实现 RadarHandlerCallBackForConsumer 加上@Service注解 ，后面有案例
 */
@Service
@Slf4j
public class RadarReportOrAlertCallBack implements RadarHandlerCallBack {

    @Autowired(required = false)
    RadarHandlerCallBackForConsumer radarHandlerCallBackForConsumer;

    @Override
    public void callBack(RadarProtocolData radarProtocolData) {
        if (radarHandlerCallBackForConsumer != null) {
            radarHandlerCallBackForConsumer.callBack(radarProtocolData);
        } else {
            throw new RuntimeException("No custom callback");
        }
    }
}

```

​																												图2

```java

/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:54
 * @modified By：
 * 获取呼吸BPM高阈值报警 ， 服务器给雷达发送命令，从而得到结果
 */
@Service
public class GetBreathBpmHeightThreshold extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        //给雷达发送命令，返回结果
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getBreathBpmHeightThreshold);
        //按照雷达协议读取相关数据
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }

}

```

​																												图3



```java

/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 11:30
 * @modified By：
 * 设置雷达参数，用户注入SetRadarParams，调用 process(GetSetRadarParamVo getRadarParamVo) 即可
 * 超时，或者失败就重试5次
 */
@Service
@Slf4j
public class SetRadarParams extends AbstractToRadarProtocolDataHandler {
    @Autowired
    private RadarTcpServer radarTcpServer;

    public ResponseResult process(GetSetRadarParamVo getRadarParamVo) {
        log.debug("set function {}", getRadarParamVo);
        ResponseResult check = RadarCheck.check(getRadarParamVo, radarTcpServer);
        if (check != null) {
            return check;
        }
        try {
            return doSet(getRadarParamVo);
        } catch (Exception e) {
            //可能超时
            for (int i = 0; i < 5; i++) {
                try {
                    return doSet(getRadarParamVo);
                } catch (RemotingException | InterruptedException ex) {
                    log.error("设置超时:{}，正在重试...", getRadarParamVo.getCode());
                }
            }
        }
        return new ResponseResult(ResponseCode.SERVER_ERROR);
    }
   .......
}

```

​																											图4



## 二，快速开始

### 1，引入sdk

```
clone mavve-radar-tcp-spring-boot-starter   git克隆sdk
idea 打开sdk ， 配置maven ， install ，在自己的项目中引入maven ， 启动类中扫描  com.timevary
```

​																							把sdk打maven坐标

![image-20220527164620739](img\image-20220527164620739.png)

​																								     引入sdk

![](img\image-20220527164650227.png)

​																									启动类扫描包

![image-20220527164747899](img\image-20220527164747899.png)



## 三，使用sdk提供的协议处理器

### 1，主动发送数据给雷达（get）

![image-20220527170025692](img\image-20220527170025692.png)

```
更多协议处理器请查看 com.timevary.radar.tcp.service.toRadar包
```

### 2，主动发送数据给雷达（set）

​	![image-20220527170643920](img\image-20220527170643920.png)



### 3，雷达发送给服务器

用户自定义回调处理器如下

```java
@Service
@Slf4j
public class RadarReportOrAlertCallBackConsumer implements RadarHandlerCallBackForConsumer {
    @Override
    public void callBack(RadarProtocolData radarProtocolData) {
        switch (radarProtocolData.getFunction()) {
            case createConnection:
                log.info("radar {} connected", radarProtocolData.getRadarId());
                return;
            case radarReport:
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
                try {
                    byteBuf.writeBytes(radarProtocolData.getData());
                    float breathBpm = byteBuf.readFloat();
                    float breathLine = byteBuf.readFloat();
                    float heartBpm = byteBuf.readFloat();
                    float heartLine = byteBuf.readFloat();
                    float target = byteBuf.readFloat();
                    float signalIntensity = byteBuf.readFloat();
                    log.info("breathBpm:{}", breathBpm);
                    log.info("breathLine:{}", breathLine);
                    log.info("heartBpm:{}", heartBpm);
                    log.info("heartLine:{}", heartLine);
                    log.info("target:{}", target);
                    log.info("signalIntensity:{}", signalIntensity);
                    break;
                } finally {
                    byteBuf.release();
                }
            case liveBedNoBack:
                log.info("离床未归");
                break;
            case longTimeNoTurnOver:
                log.info("长时间未翻身");
                break;
            case respiratoryArrest:
                log.info("呼吸骤停");
                break;
            case breathLowBpmAlert:
                log.info("低于呼吸BPM高阈值");
                break;
            case breathHeightBpmAlert:
                log.info("高于呼吸BPM低阈值");
                break;
            case cardiacArrest:
                log.info("心率骤停");
                break;
            case heartRateLowBpm:
                log.info("低于心率BPM低阈值");
                break;
            case heartRateHeightBpm:
                log.info("高于心率BPM高阈值");
                break;
            case rollOverOrSitAndCallThePolice:
                byte[] data = radarProtocolData.getData();
                ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();
                try {
                    buffer.writeBytes(data);
                    //0：翻身报警，1：坐立报警
                    int ints = buffer.readInt();
                    if (ints == 0) {
                        log.info("翻身报警");
                    } else {
                        log.info("坐立报警");
                    }
                } finally {
                    buffer.release();
                }
                break;
            case physicalActivityReportStatistics:
                //一次体动上报统计， 上报累积移动能量
                byte[] data1 = radarProtocolData.getData();
                ByteBuf buffer1 = PooledByteBufAllocator.DEFAULT.buffer();
                try {
                    buffer1.writeBytes(data1);
                    //当前体动的信号强度
                    float readFloat = buffer1.readFloat();
                    log.info("一次体动上报统计， 上报累积移动能量");
                } finally {
                    buffer1.release();
                }
                break;
            default:
                log.error("奇怪的雷达上报 , {}", radarProtocolData);
        }
    }
}
```



## 四，自定义协议处理器

### 1，不行，因为每个协议都只能有一个处理器，所以不能出现多个 

