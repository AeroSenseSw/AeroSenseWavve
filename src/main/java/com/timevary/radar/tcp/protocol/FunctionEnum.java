
package com.timevary.radar.tcp.protocol;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/4 14:59
 * @version: 1.0
 * @update: ywb 2022/1/5 17:56
 */

public enum FunctionEnum {
    UNDEFINED(-1),

    /**
     * 建立连接
     */
    createConnection(0x0001),
    /**
     * 雷达主动报告
     */
    radarReport(0x03e8),
    /**
     * 离床未归报警
     */
    liveBedNoBack(0x0406),
    /**
     * 长时间未翻身报警
     */
    longTimeNoTurnOver(0x0409),
    /**
     * 呼吸骤停报警
     */
    respiratoryArrest(0x03f0),
    /**
     * 呼吸低bpm报警
     */
    breathLowBpmAlert(0x03f3),
    /**
     * 呼吸高bpm报警
     */
    breathHeightBpmAlert(0x03f6),
    /**
     * 心率骤停报警
     */
    cardiacArrest(0x03fb),
    /**
     * 心率低bpm报警
     */
    heartRateLowBpm(0x03fe),
    /**
     * 心率高bpm报警
     */
    heartRateHeightBpm(0x0401),
    /**
     * 获取版本号
     */
//    getVersion(0x00),
    /**
     * 设置数据上报时间间隔
     */
    setReportInterval(0x03e9),
    /**
     * 获取数据上报时间间隔
     */
    getReportInterval(0x03ea),
    /**
     * 设置目标距离
     */
    setTargetDistance(0x03eb),
    /**
     * 获取工作目标距离
     */
    getTargetDistance(0x03ec),
    /**
     * 开启校准
     */
    calibration(0x03ed),
    /**
     * 设置呼吸骤停报警时间
     */
    setRespiratoryArrestReportTime(0x03ee),
    /**
     * 获取呼吸骤停报警时间
     */
    getRespiratoryArrestReportTime(0x03ef),
    /**
     * 设置呼吸BPM低阈值报警
     */
    setBreathBpmLowThreshold(0x03f1),
    /**
     * 获取呼吸BPM低阈值报警
     */
    getBreathBpmLowThreshold(0x03f2),
    /**
     * 设置呼吸BPM高阈值报警
     */
    setBreathBpmHeightThreshold(0x03f4),
    /**
     * 获取呼吸BPM高阈值报警
     */
    getBreathBpmHeightThreshold(0x03f5),
    /**
     * 设置呼吸BPM阈值报警持续时间 单位 秒  0：关闭算法监测   30~180：呼吸BPM阈值报警持续时间，默认60,
     */
    setBreathingBpmLowThresholdAlarmTime(0x03f7),
    /**
     * 获取呼吸BPM阈值报警持续时间 单位 秒
     */
    getBreathingBpmLowThresholdAlarmTime(0x03f8),
    /**
     * 设置心率BPM阈值报警持续时间  单位 秒 0：关闭算法监测 30~180：：心率BPM阈值报警持续时间，默认60
     */
    settingTheCardiacArrestAlarmTime(0x0402),
    /**
     * 获取心率BPM阈值报警持续时间 单位 秒
     */
    gettingTheCardiacArrestAlarmTime(0x0403),
    /**
     * 设置心率骤停报警时间
     */
    setCardiacArrestReportTime(0x03f9),
    /**
     * 获取心率骤停报警时间
     */
    getCardiacArrestReportTime(0x03fa),
    /**
     * 设置心率BPM低阈值报警
     */
    setHeartRateLowThreshold(0x03fc),
    /**
     * 获取心率BPM低阈值报警
     */
    getHeartRateLowThreshold(0x03fd),
    /**
     * 设置心率BPM高阈值报警
     */
    setHeartRateHeightThreshold(0x03ff),
    /**
     * 、
     * 获取心率BPM高阈值报警
     */
    getHeartRateHeightThreshold(0x0400),
    /**
     * 设置离床未归报警时间
     */
    setLiveBedNoBackReportTime(0x0404),
    /**
     * 获取离床未归报警时间
     */
    getLiveBedNoBackReportTime(0x0405),
    /**
     * 设置长时间未翻身报警时间
     */
    setLongTimeNoTurnOverReportTime(0x0407),
    /**
     * 获取长时间未翻身报警时间
     */
    getLongTimeNoTurnOverReportTime(0x0408),
    /**
     * 开启翻身报警
     * 0：关闭算法监测
     * 1：开启翻身算法报警监测
     * 2：开启坐立报警算法监测
     * 3：开启翻身、坐立报警监测
     */
    turnOnTurnOver(0x040a),
    /**
     * 获取翻身报警算法状态
     * 0：关闭算法监测
     * 1：开启翻身算法报警监测
     * 2：开启坐立报警算法监测
     * 3：开启翻身、坐立报警监测
     */
    getTurnOverTurnOnState(0x040b),

    /**
     * 翻身报警/坐立报警
     */
    rollOverOrSitAndCallThePolice(0x040c),

    /**
     * 开启/关闭体动次数收集  ，需校准
     * 0：关闭算法监测
     * 1：开启算法监测
     */
    bodyMovementCollection(0x040d),

    /**
     * 获取体动次数收集状态
     * 0：关闭算法监测
     * 1：开启算法监测
     */
    getTheStatusOfTheNumberOfMotionsCollected(0x040e),

    /**
     * 一次体动上报统计， 上报累积移动能量
     */
    physicalActivityReportStatistics(0x040f),
    /**
     * 获取雷达id
     */
    getRadarId(0x0410),

    /**
     * 软重启
     */
    softReboot(0x0411),

    /**
     * 通知升级，下发固件（固件内容 + crc校验）240b,是否升级成功
     */
    notifyUpdate(0x0021), issueFirmware(0x0022), updateResult(0x0023);

    private final short function;

    FunctionEnum(int function) {
        this.function = (short) function;
    }

    public static FunctionEnum from(short function) {
        return Arrays.stream(FunctionEnum.values())
                .filter(f -> f.getFunction() == function)
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String fromForCache(short function) {
        FunctionEnum from = from(function);
        switch (from) {
            case setReportInterval:
                return getReportInterval.name();
            case setTargetDistance:
                return getTargetDistance.name();
            case setRespiratoryArrestReportTime:
                return getRespiratoryArrestReportTime.name();
            case setBreathBpmLowThreshold:
                return getBreathBpmLowThreshold.name();
            case setBreathBpmHeightThreshold:
                return getBreathBpmHeightThreshold.name();
            case setBreathingBpmLowThresholdAlarmTime:
                return getBreathingBpmLowThresholdAlarmTime.name();
            case setCardiacArrestReportTime:
                return getCardiacArrestReportTime.name();
            case setHeartRateLowThreshold:
                return getHeartRateLowThreshold.name();
            case setHeartRateHeightThreshold:
                return getHeartRateHeightThreshold.name();
            case settingTheCardiacArrestAlarmTime:
                return gettingTheCardiacArrestAlarmTime.name();
            case setLiveBedNoBackReportTime:
                return getLiveBedNoBackReportTime.name();
            case setLongTimeNoTurnOverReportTime:
                return getLongTimeNoTurnOverReportTime.name();
            case turnOnTurnOver:
                return getTurnOverTurnOnState.name();
            case bodyMovementCollection:
                return getTheStatusOfTheNumberOfMotionsCollected.name();
        }
        return null;
    }

    public short getFunction() {
        return function;
    }
}
