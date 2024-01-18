package com.aerosense.radar.tcp.hander;

import com.aerosense.radar.tcp.config.RequestTimeOut;
import com.aerosense.radar.tcp.domain.dto.*;
import com.aerosense.radar.tcp.domain.enums.InstallMode;
import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocolConsts;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.aerosense.radar.tcp.protocol.RadarProtocolUtil;
import com.aerosense.radar.tcp.server.RadarTcpServer;
import com.aerosense.radar.tcp.util.ByteUtil;
import com.alipay.remoting.exception.RemotingException;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @description: server request radar handler
 * @author jia.wu
 * @date 2024/1/12 16:12
 * @version 1.0.0
 */
@Slf4j
@Component
public class ServerRequestRadarHandler {

    private RadarTcpServer radarTcpServer;
    private RadarFirmwareUpgradeHandler radarFirmwareUpgradeHandler;

    public ServerRequestRadarHandler(RadarTcpServer radarTcpServer,
                                     RadarFirmwareUpgradeHandler radarFirmwareUpgradeHandler) {
        this.radarTcpServer = radarTcpServer;
        this.radarFirmwareUpgradeHandler = radarFirmwareUpgradeHandler;
    }

    public RadarProtocolData invokeToRadarByAddress(String address, RadarProtocolData requestObj)
            throws RemotingException {
        try {
            return (RadarProtocolData) radarTcpServer.invokeSyncWithAddress(address, requestObj,
                    RequestTimeOut.TIME_OUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public RadarProtocolData invokeToRadar(RadarProtocolData requestObj)
            throws RemotingException {
        return invokeToRadar(requestObj, RequestTimeOut.TIME_OUT);
    }

    /**
     * @param requestObj
     * @param invokeTimeoutMills
     * @return
     * @throws RemotingException
     */
    public RadarProtocolData invokeToRadar(RadarProtocolData requestObj, int invokeTimeoutMills)
            throws RemotingException {
        try {
            Object obj = radarTcpServer.invokeSync(requestObj, invokeTimeoutMills);
            if (obj != null && obj instanceof Exception) {
                throw new RemotingException(((Exception) obj).getMessage());
            }
            return (RadarProtocolData) obj;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param radarId
     * @return
     *      report time interval, time unit 50ms
     * @throws RemotingException
     */
    public int getReportInterval(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getReportInterval, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }

    /**
     *
     * @param radarId
     * @param reportInterval
     *      report time interval, time unit 50ms
     * @return
     * @throws RemotingException
     */
    public boolean setReportInterval(String radarId, int reportInterval) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setReportInterval, ByteUtil.intToByteBig(reportInterval));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }


    /**
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public float getTargetDistance(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getTargetDistance, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.byte4ToFloat(retObj.getData());
    }

    /**
     *
     * @param dto
     * @return
     * @throws RemotingException
     */
    public boolean setTargetDistance(SetTargetDistanceDto dto) throws RemotingException {
        float targetDistance = dto.getInstallHeight();
        if(targetDistance < 0){
            throw new IllegalArgumentException("targetDistance value invalid : " + targetDistance);
        }
        if (dto.getInstallMode() == InstallMode.HEADBOARD) {
            targetDistance = dto.getInstallHeight() / RadarProtocolConsts.SIN_60;
            targetDistance = Math.round(targetDistance * 100) / 100F;
        }
        if(targetDistance > 3f){
            throw new IllegalArgumentException("targetDistance value invalid : " + targetDistance);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(dto.getRadarId(),
                FunctionEnum.setTargetDistance, ByteUtil.floatToByte4(targetDistance));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public boolean calibration(String radarId) throws RemotingException {
        int calibrationTimeout = 15000;
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.calibration, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData, calibrationTimeout);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the NoBreathAlert threshold (unit: s).
     * 0：turn off the function.
     * 30~86400：the time threshold to trigger the NoBreathAlert.
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getRespiratoryArrestReportTime(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getRespiratoryArrestReportTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }

    /**
     * To set the NoBreathAlert threshold (unit: s).
     * 0：turn off the function.
     * 30~86400：the time threshold to trigger the NoBreathAlert.
     *
     * @param radarId
     * @param respiratoryArrestReportTime
     * @return
     * @throws RemotingException
     */
    public boolean setRespiratoryArrestReportTime(String radarId, int respiratoryArrestReportTime) throws RemotingException {
        if (respiratoryArrestReportTime != 0 && (respiratoryArrestReportTime<30 || respiratoryArrestReportTime>86400)) {
            throw new IllegalArgumentException("respiratoryArrestReportTime value invalid : " + respiratoryArrestReportTime);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setRespiratoryArrestReportTime, ByteUtil.intToByteBig(respiratoryArrestReportTime));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the LowBreath(BPM)Alert threshold (unit: s).
     * 0：Turn off detection
     * 1-1024：BreathBpmLowThreshold
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBreathBpmLowThreshold(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBreathBpmLowThreshold, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the LowBreath(BPM)Alert threshold (unit: s).
     * 0：turn off the function.
     * 1-1024：the  LowBreath(BPM)Alert threshold.
     *
     * @param radarId
     * @param lowThresholdValue
     * @return
     * @throws RemotingException
     */
    public boolean setBreathBpmLowThreshold(String radarId, int lowThresholdValue)
            throws RemotingException {
        if (lowThresholdValue < 0 || lowThresholdValue>1024) {
            throw new IllegalArgumentException("lowThresholdValue value invalid : " + lowThresholdValue);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBreathBpmLowThreshold, ByteUtil.intToByteBig(lowThresholdValue));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * To set the HighBreath(BPM)Alert threshold (unit: s).
     * 0：turn off the function.
     * N：the  HighBreath(BPM)Alert threshold.
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBreathBpmHighThreshold(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBreathBpmHighThreshold, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * Command to read the HighBreath(BPM)Alert threshold (unit: s).
     * 0：turn off the function.
     * N：the  HighBreath(BPM)Alert threshold.
     * @param radarId
     * @param highThresholdValue
     * @return
     * @throws RemotingException
     */
    public boolean setBreathBpmHighThreshold(String radarId, int highThresholdValue)
            throws RemotingException {
        if (highThresholdValue < 0 || highThresholdValue>1024) {
            throw new IllegalArgumentException("highThresholdValue value invalid : " + highThresholdValue);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBreathBpmHighThreshold, ByteUtil.intToByteBig(highThresholdValue));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the time interval to trigger the High/Low Breath(BPM) Alert (unit: s).
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBreathBpmThresholdAlarmTime(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBreathBpmThresholdAlarmTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the time interval to trigger the High/Low Breath(BPM) Alert (unit: s).
     * Please note that the High/Low Breath(BPM) Alert will only be triggered when the detected respiratory rate is consistently higher/lower than the set value.
     * 30~86400：the time interval to trigger the High/Low Breath(BPM) Alert (default setting: 120).
     *
     * @param radarId
     * @param breathBpmThresholdAlarmTime
     * @return
     * @throws RemotingException
     */
    public boolean setBreathBpmThresholdAlarmTime(String radarId, int breathBpmThresholdAlarmTime)
            throws RemotingException {
        if (breathBpmThresholdAlarmTime != 0 && (breathBpmThresholdAlarmTime < 30 || breathBpmThresholdAlarmTime > 86400)) {
            throw new IllegalArgumentException("breathBpmThresholdAlarmTime value invalid : " + breathBpmThresholdAlarmTime);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBreathBpmThresholdAlarmTime, ByteUtil.intToByteBig(breathBpmThresholdAlarmTime));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }


    /**
     * Command to read  the time threshold to trigger the NoHeartAlert.
     * 0：turn off the function.
     * 30~86400： the time threshold to trigger the NoHeartAlert.
     *
     * @param radarId
     * @return
     * @throws RemotingException
     * @throws InterruptedException
     */
    public int getCardiacArrestReportTime(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getCardiacArrestReportTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the time threshold to trigger the NoHeartAlert (unit:second).
     * 0：turn off the function.
     * 30~86400： the time threshold to trigger the NoHeartAlert.
     *
     * @param radarId
     * @param noHeartAlertTimer
     * @return
     * @throws RemotingException
     */
    public boolean setCardiacArrestReportTime(String radarId, int noHeartAlertTimer)
            throws RemotingException {
        if (noHeartAlertTimer != 0 && (noHeartAlertTimer>86400 || noHeartAlertTimer<30)) {
            throw new IllegalArgumentException("noHeartAlertTimer value invalid : " + noHeartAlertTimer);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setCardiacArrestReportTime, ByteUtil.intToByteBig(noHeartAlertTimer));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the LowHeartAlert (BPM) threshold (unit: s).
     * 0：turn off the function.
     * N：the  LowHeartAlert (BPM) threshold.
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getHeartRateLowThreshold(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getHeartRateLowThreshold, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the LowHeartAlert (BPM) threshold (unit: s).
     * 0：turn off the function.
     * N：the LowHeartAlert (BPM) Alert threshold.
     *
     * @param radarId
     * @param lowThresholdValue
     * @return
     * @throws RemotingException
     */
    public boolean setHeartRateLowThreshold(String radarId, int lowThresholdValue)
            throws RemotingException {
        if (lowThresholdValue < 0) {
            throw new IllegalArgumentException("lowThresholdValue value invalid : " + lowThresholdValue);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setHeartRateLowThreshold, ByteUtil.intToByteBig(lowThresholdValue));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the HighHeart(BPM)Alert threshold (unit: s).
     * 0：turn off the function.
     * N：the HighHeart(BPM)Alert threshold.
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getHeartRateHighThreshold(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getHeartRateHighThreshold, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the HighHeart(BPM)Alert threshold (unit: s).
     * 0：turn off the function.
     * N：the HighHeart(BPM)Alert threshold.
     *
     * @param radarId
     * @param highThresholdValue
     * @return
     * @throws RemotingException
     */
    public boolean setHeartRateHighThreshold(String radarId, int highThresholdValue)
            throws RemotingException {
        if (highThresholdValue < 0) {
            throw new IllegalArgumentException("highThresholdValue value invalid : " + highThresholdValue);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setHeartRateHighThreshold, ByteUtil.intToByteBig(highThresholdValue));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the time interval to trigger the High/LowHeartAlert Timer (BPM) Alert (unit: s).
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getHeartRateBPMThresholdTimer(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.gettingTheCardiacArrestAlarmTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * High/LowHeartAlert Timer. To set the time interval to trigger the High/Low Heart (BPM) Alert (unit: s). Please note that the High/Low Heart (BPM) Alert will only be triggered when the detected heart rate is consistently higher/lower than the set value.
     * 30~86400：the time interval to trigger the High/Low Heart (BPM) Alert (default setting: 120).
     *
     * @param radarId
     * @param thresholdTimer
     * @return
     * @throws RemotingException
     */
    public boolean setHeartRateBPMThresholdTimer(String radarId, int thresholdTimer)
            throws RemotingException {
        if (thresholdTimer < 30 || thresholdTimer > 86400) {
            throw new IllegalArgumentException("thresholdTimer value invalid : " + thresholdTimer);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.settingTheCardiacArrestAlarmTime, ByteUtil.intToByteBig(thresholdTimer));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the time threshold to trigger the Bed Exit Alert (unit:s).
     * 0：：turn off the function.
     * 1~N：the  time threshold to trigger the Bed Exit Alert
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBedExitAlertTimer(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBedExitAlertTimer, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the time threshold to trigger the Bed Exit Alert (unit:s).
     * 0：turn off the function.
     * 1~N：the  time threshold to trigger the Bed Exit Alert
     *
     * @param radarId
     * @param bedExitAlertTimer
     * @return
     * @throws RemotingException
     */
    public boolean setBedExitAlertTimer(String radarId, int bedExitAlertTimer)
            throws RemotingException {
        if (bedExitAlertTimer < 0) {
            throw new IllegalArgumentException("bedExitAlertTimer value invalid : " + bedExitAlertTimer);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBedExitAlertTimer, ByteUtil.intToByteBig(bedExitAlertTimer));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the time threshold to trigger the No-Turn-Over Alert
     * 0：turn off the function.
     * 1~N：the  time threshold to trigger the No-Turn-Over Alert
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getNoTurnOverAlertTimer(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getLongTimeNoTurnOverReportTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * To set the time threshold to trigger the No-Turn-Over Alert (unit: s).
     * 0：turn off the function.
     * 1~N：the  time threshold to trigger the No-Turn-Over Alert
     *
     * @param radarId
     * @param noTurnOverAlertTime
     * @return
     * @throws RemotingException
     */
    public boolean setNoTurnOverAlertTimer(String radarId, int noTurnOverAlertTime)
            throws RemotingException {
        if (noTurnOverAlertTime < 0) {
            throw new IllegalArgumentException("noTurnOverAlertTimer value invalid : " + noTurnOverAlertTime);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setLongTimeNoTurnOverReportTime, ByteUtil.intToByteBig(noTurnOverAlertTime));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * To turn on the Turn-Over Alert.
     * 0：turn off the function.
     * 1：algorithm based on users turning over in bed.
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getTurnOverAlertStatus(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getTurnOverTurnOnState, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }

    /**
     * To turn on the Turn-Over Alert.
     * 0：turn off the function.
     * 1：algorithm based on users turning over in bed.
     *
     * @param radarId
     * @param turnOverAlertStatus
     * @return
     * @throws RemotingException
     */
    public boolean setTurnOverAlertStatus(String radarId, int turnOverAlertStatus)
            throws RemotingException {
        if (turnOverAlertStatus != RadarProtocolConsts.ALGORITHM_STATUS_CLOSE &&
                turnOverAlertStatus != RadarProtocolConsts.ALGORITHM_STATUS_OPEN) {
            throw new IllegalArgumentException("turnOverAlertStatus value invalid : " + turnOverAlertStatus);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.turnOnTurnOver, ByteUtil.intToByteBig(turnOverAlertStatus));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Command to read the bodymovement counter.
     * 0：OFF
     * 1：ON
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBodyMovementCounterStatus(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getTheStatusOfTheNumberOfMotionsCollected, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }


    /**
     * Turn ON/OFF counter for body movent.
     * 0：OFF
     * 1：ON
     *
     * @param radarId
     * @param bodyMovementCounterStatus
     * @return
     * @throws RemotingException
     */
    public boolean setBodyMovementCounterStatus(String radarId, int bodyMovementCounterStatus)
            throws RemotingException {
        if (bodyMovementCounterStatus != 0 && bodyMovementCounterStatus != 1) {
            throw new IllegalArgumentException("bodyMoveNumStatus value invalid : " + bodyMovementCounterStatus);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.bodyMovementCollection, ByteUtil.intToByteBig(bodyMovementCounterStatus));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * Get Radar ID by radar connect address
     *
     * @param address
     * @return
     * @throws RemotingException
     */
    public String getRadarId(String address) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newFunctionInstance(
                FunctionEnum.getRadarId, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadarByAddress(address, radarProtocolData);
        return new String(retObj.getData(), StandardCharsets.UTF_8);
    }

    /**
     * Command to reset the radar.
     *
     * @param radarId
     * @return
     */
    public boolean softRebootRadar(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.softReboot, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }


    /**
     * batch upgrade radar firmware
     *
     * @param radarUpgradeFirmwareDto
     * @return
     */
    public List<String> upgradeFirmware(RadarUpgradeFirmwareDto radarUpgradeFirmwareDto) {
        try {
            RadarUpgradeFirmwareDto upgradeResult = radarFirmwareUpgradeHandler
                    .doUpgradeFirmware(radarUpgradeFirmwareDto);
            return upgradeResult.getRadarIds();
        } catch (Exception e) {
            log.error("upgrade radar firmware fail", e);
        }
        return Lists.newArrayList();
    }


    // wavve pro----------------------2024-01-15
    /**
     *
     * @param radarId
     * @return
     * @throws RemotingException
     */
    public int getBufferTime(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBufferTime, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return ByteUtil.bytes2IntBig(retObj.getData());
    }

    /**
     *
     * @param radarId
     * @param bufferTime
     * @return
     * @throws RemotingException
     */
    public boolean setBufferTime(String radarId, int bufferTime) throws RemotingException {
        if (bufferTime > 300 || bufferTime < 30) {
            throw new IllegalArgumentException("buffer time value invalid, must be 30-300 second - actual value "  + bufferTime);
        }
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBufferTime, ByteUtil.intToByteBig(bufferTime));
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     * get radar firmware version
     * @return
     * @throws RemotingException
     */
    public String getFirmwareVersion(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getRadarFirmwareVersion, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.getHardwareVersion(retObj.getData());
    }

    /**
     * set WAVVE PRO  radar install mode
     *
     * @param dto
     * @return
     */
    public boolean setRadarInstallMode(RadarInstallModeDto dto) throws RemotingException {
        String radarId = dto.getRadarId();
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setInstallMode, dto.toBytes());
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     *
     * @param radarId
     * @return
     */
    public RadarInstallModeDto getRadarInstallMode(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getInstallMode, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        RadarInstallModeDto dto = RadarInstallModeDto.fromBytes(retObj.getData());
        dto.setRadarId(radarId);
        return dto;
    }

    /**
     *  set WAVVE PRO  radar bed location
     * @param dto
     * @return
     */
    public boolean setBedLocation(BedLocationDto dto) throws RemotingException {
        String radarId = dto.getRadarId();
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setBedLocation, dto.toBytes());
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     *
     * @param radarId
     * @return
     */
    public BedLocationDto getBedLocation(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getBedLocation, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        BedLocationDto dto = BedLocationDto.fromBytes(retObj.getData());
        dto.setRadarId(radarId);
        return dto;
    }

    /**
     * set WAVVE PRO  radar detection area
     * @param dto
     * @return
     */
    public boolean setDetectionArea(DetectionAreaDto dto) throws RemotingException {
        String radarId = dto.getRadarId();
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.setDetectionArea, dto.toBytes());
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        return RadarProtocolUtil.isRetSuccess(retObj);
    }

    /**
     *
     * @param radarId
     * @return
     */
    public DetectionAreaDto getDetectionArea(String radarId) throws RemotingException {
        RadarProtocolData radarProtocolData = RadarProtocolData.newInstance(radarId,
                FunctionEnum.getDetectionArea, ByteUtil.BYTES_0);
        RadarProtocolData retObj = invokeToRadar(radarProtocolData);
        DetectionAreaDto dto = DetectionAreaDto.fromBytes(retObj.getData());
        dto.setRadarId(radarId);
        return dto;
    }
}
