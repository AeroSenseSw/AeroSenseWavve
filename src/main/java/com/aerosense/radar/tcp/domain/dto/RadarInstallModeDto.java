package com.aerosense.radar.tcp.domain.dto;


import com.aerosense.radar.tcp.util.ByteUtil;

import java.io.Serializable;

/**
 * @description: RadarInstallModeDto, unit: mm  /  0.01 degree
 * @author jia.wu
 * @date 2024/1/15 17:07
 * @version 1.0.0
 */
public class RadarInstallModeDto implements Serializable {
    private static final long serialVersionUID = 1;

    private String radarId;
    private int deviceHeight;
    private int devicePitch;
    private int installMode;

    public RadarInstallModeDto(){}

    public RadarInstallModeDto(int deviceHeight, int devicePitch, int installMode) {
        this.deviceHeight = deviceHeight;
        this.devicePitch = devicePitch;
        this.installMode = installMode;
    }
    public RadarInstallModeDto(String radarId, int deviceHeight, int devicePitch, int installMode) {
        this(deviceHeight, devicePitch, installMode);
        this.radarId = radarId;
    }

    public String getRadarId() {
        return radarId;
    }

    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public int getDevicePitch() {
        return devicePitch;
    }

    public void setDevicePitch(int devicePitch) {
        this.devicePitch = devicePitch;
    }

    public int getInstallMode() {
        return installMode;
    }

    public void setInstallMode(int installMode) {
        this.installMode = installMode;
    }

    /**
     * @return
     */
    public byte[] toBytes(){
        byte[] bytes = new byte[12];
        System.arraycopy(ByteUtil.intToByteBig(deviceHeight), 0, bytes, 0, 4);
        System.arraycopy(ByteUtil.intToByteBig(devicePitch), 0, bytes, 4, 4);
        System.arraycopy(ByteUtil.intToByteBig(installMode), 0, bytes, 8, 4);
        return bytes;
    }

    /**
     * @param data
     * @return
     */
    public static RadarInstallModeDto fromBytes(byte[] data) {
        int deviceHeight = ByteUtil.bytes2IntBig(data);
        int devicePitch = ByteUtil.bytes2IntBig(data, 4);
        int installMode = ByteUtil.bytes2IntBig(data, 8);
        return new RadarInstallModeDto(deviceHeight, devicePitch,installMode);
    }

    @Override
    public String toString() {
        return "RadarInstallModeDto{" +
                "radarId='" + radarId + '\'' +
                ", deviceHeight=" + deviceHeight +
                ", devicePitch=" + devicePitch +
                ", installMode=" + installMode +
                '}';
    }
}
