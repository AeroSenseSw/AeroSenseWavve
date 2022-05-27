## 一，架构解析

### 1，sdk启动流程图

![image-20220527162202930](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527162202930.png)

### 2，雷达服务器交互流程图

![image-20220527163137967](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163137967.png)

![image-20220527163200872](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163200872.png)

​																											图1

![image-20220527163236624](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163236624.png)

​																												图2

![image-20220527163756431](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163756431.png)

![image-20220527163820861](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163820861.png)

​																												图3



![image-20220527163942475](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527163942475.png)

​																											图4



## 二，快速开始

### 1，引入sdk

```
clone mavve-radar-tcp-spring-boot-starter   git克隆sdk
idea 打开sdk ， 配置maven ， install ，在自己的项目中引入maven ， 启动类中扫描  com.timevary
```

![image-20220527164620739](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527164620739.png)![image-20220527164650227](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527164650227.png)![image-20220527164747899](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527164747899.png)



## 三，使用sdk提供的协议处理器

### 1，主动发送数据给雷达（get）

![image-20220527170025692](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527170025692.png)

```
更多协议处理器请查看 com.timevary.radar.tcp.service.toRadar包
```

### 2，主动发送数据给雷达（set）

​	![image-20220527170643920](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220527170643920.png)



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

