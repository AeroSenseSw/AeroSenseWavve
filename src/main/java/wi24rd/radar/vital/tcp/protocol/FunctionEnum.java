
package wi24rd.radar.vital.tcp.protocol;

import java.util.Arrays;



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
    NoHeartAlert(0x03fb),
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
    setWorkingDistance(0x03eb),
    /**
     */
    getWorkingDistance(0x03ec),
    /**
     */
    calibration(0x03ed),
    /**
     */
    setNoBreathAlertTimer(0x03ee),
    /**
     */
    getNoBreathAlertTimer(0x03ef),

    
    NoBreathAlert(0x03f0),
   

    setBreathBpmLowThreshold(0x03f1),
    /**
     */
    getBreathBpmLowThreshold(0x03f2),
    /**
     */
    LowBreathBPMAlert(0x03f3),

    setBreathBpmHighThreshold(0x03f4),
    /**
     */
    getBreathBpmHighThreshold(0x03f5),

    HighBreathBPMAlert(0x03f6),
    /**
     */
    setBreathBPMLowThresholdAlertTimer(0x03f7),
    
    getBreathBPMLowThresholdAlertTimer(0x03f8),
    
    setNoHeartAlertTimer(0x03f9),
    /**
     */
    getNoHeartAlertTimer(0x03fa),
    /**
     */
    setLowHeartBPMAlertThreshold(0x03fc),
    /**
     */
    getLowHeartBPMAlertThreshold(0x03fd),
    /**
     */
    setHighHeartBPMAlertThreshold(0x03ff),
    /**
     * 、
     */
    getHighHeartBPMAlertThreshold(0x0400),

    setHighLowHeartBPMAlertTimer(0x0402),
    /**
     */
    getHighLowHeartBPMAlertTimer(0x0403),

    setBedExitAlertTimer(0x0404),
    

    getBedExitAlertTimer(0x0405),
  
    
    liveBedNoBack(0x0406),
    /**
     */
    setLongTimeNoTurnOverAlert(0x0407),
    /**
     */
    getLongTimeNoTurnOverAlert(0x0408),

    longTimeNoTurnOver(0x0409),
    turnOnTurnOver(0x040a),
  
    getTurnOverTurnOnState(0x040b),

   
    rollOverOrSitAndCallThePolice(0x040c),

    
    TurnBodyMovementCounter(0x040d),

    /**
     */
    GetBodyMovementCounterStatus(0x040e),

    /**
     */
    physicalActivityReportStatistics(0x040f),
    /**
     */
    getRadarId(0x0410),

    /**
     */
    ResetRadar(0x0411),

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
