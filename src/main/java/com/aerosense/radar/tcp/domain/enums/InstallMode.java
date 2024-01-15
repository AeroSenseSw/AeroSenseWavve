package com.aerosense.radar.tcp.domain.enums;

import java.util.Arrays;

/**
 * @description: install mode
 * @author jia.wu
 * @date 2024/1/15 11:25
 * @version 1.0.0
 */
public enum InstallMode {

    SIDE_WALL(0),
    HEADBOARD(1),
    CEILING(2)

    ;
    private Integer value;


    InstallMode(Integer value) {
        this.value = value;
    }

    public static InstallMode from(Integer installMode){
        return Arrays.stream(values()).filter(f->f.isInstallMode(installMode)).findFirst().get();
    }

    public Integer getInstallMode(){
        return this.value;
    }

    public boolean isInstallMode(Integer installMode){
        return installMode!=null && value.equals(installMode);
    }

}
