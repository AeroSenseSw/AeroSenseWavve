package com.aerosense.radar.tcp.domain.dto;

import com.aerosense.radar.tcp.domain.enums.InstallMode;

import java.io.Serializable;

/**
 * @author jia.wu
 */
public class SetTargetDistanceDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String radarId;
    private InstallMode installMode;
    private float installHeight;

    public String getRadarId() {
        return radarId;
    }

    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }

    public InstallMode getInstallMode() {
        return installMode;
    }

    public void setInstallMode(InstallMode installMode) {
        this.installMode = installMode;
    }

    public float getInstallHeight() {
        return installHeight;
    }

    public void setInstallHeight(float installHeight) {
        this.installHeight = installHeight;
    }

    @Override
    public String toString() {
        return "SetTargetDistanceDto{" +
                "radarId='" + radarId + '\'' +
                ", installMode=" + installMode +
                ", installHeight=" + installHeight +
                '}';
    }
}
