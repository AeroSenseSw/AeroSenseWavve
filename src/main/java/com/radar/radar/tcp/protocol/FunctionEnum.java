
package com.radar.radar.tcp.protocol;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/4 14:59
 * @version: 1.0
 * @update: ywb 2022/1/5 17:56
 */

public enum FunctionEnum {
    UNDEFINED(-1),

    /**
     */
    createConnection(0x0001),
    /**
     */
    radarReport(0x03e8),
    /**
     */
    liveBedNoBack(0x0406),
    /**
     */
    longTimeNoTurnOver(0x0409),
    /**
     */
    respiratoryArrest(0x03f0),
    /**
     */
    breathLowBpmAlert(0x03f3),
    /**
     */
    breathHeightBpmAlert(0x03f6),
    /**
     */
    cardiacArrest(0x03fb),
    /**
     */
    heartRateLowBpm(0x03fe),
    /**
     */
    heartRateHeightBpm(0x0401),
    /**
     */
//    getVersion(0x00),
    /**
     */
    setReportInterval(0x03e9),
    /**
     */
    getReportInterval(0x03ea),
    /**
     */
    setTargetDistance(0x03eb),
    /**
     */
    getTargetDistance(0x03ec),
    /**
     */
    calibration(0x03ed),
    /**
     */
    setRespiratoryArrestReportTime(0x03ee),
    /**
     */
    getRespiratoryArrestReportTime(0x03ef),
    /**
     */
    setBreathBpmLowThreshold(0x03f1),
    /**
     */
    getBreathBpmLowThreshold(0x03f2),
    /**
     */
    setBreathBpmHeightThreshold(0x03f4),
    /**
     */
    getBreathBpmHeightThreshold(0x03f5),
    /**
     */
    setBreathingBpmLowThresholdAlarmTime(0x03f7),
    /**
     */
    getBreathingBpmLowThresholdAlarmTime(0x03f8),
    /**
     */
    settingTheCardiacArrestAlarmTime(0x0402),
    /**
     */
    gettingTheCardiacArrestAlarmTime(0x0403),
    /**
     */
    setCardiacArrestReportTime(0x03f9),
    /**
     */
    getCardiacArrestReportTime(0x03fa),
    /**
     */
    setHeartRateLowThreshold(0x03fc),
    /**
     */
    getHeartRateLowThreshold(0x03fd),
    /**
     */
    setHeartRateHeightThreshold(0x03ff),
    /**
     * 、
     */
    getHeartRateHeightThreshold(0x0400),
    /**
     */
    setLiveBedNoBackReportTime(0x0404),
    /**
     */
    getLiveBedNoBackReportTime(0x0405),
    /**
     */
    setLongTimeNoTurnOverReportTime(0x0407),
    /**
     */
    getLongTimeNoTurnOverReportTime(0x0408),
    /**
     */
    turnOnTurnOver(0x040a),
    /**
     */
    getTurnOverTurnOnState(0x040b),

    /**
     */
    rollOverOrSitAndCallThePolice(0x040c),

    /**
     */
    bodyMovementCollection(0x040d),

    /**
     */
    getTheStatusOfTheNumberOfMotionsCollected(0x040e),

    /**
     */
    physicalActivityReportStatistics(0x040f),
    /**
     */
    getRadarId(0x0410),

    /**
     */
    softReboot(0x0411),

    zigbeeRelay(0x0413),

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


    public short getFunction() {
        return function;
    }
}
