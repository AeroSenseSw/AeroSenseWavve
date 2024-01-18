package com.aerosense.radar.tcp.protocol;

import java.util.Arrays;
/**
 * @description: radar type
 * @author jia.wu
 * @date 2024/1/18 14:35
 * @version 1.0.0
 */
public enum RadarTypeEnum {

    WAVVE((byte)0x01, "WAVVE", WavveRadarProtocol.PROTOCOL_CODE_BYTE),
    WAVVE_PRO((byte)0x03, "WAVVE PRO", WavveProRadarProtocol.PROTOCOL_CODE_BYTE)
    ;

    private byte type;
    private String name;
    private byte protocolCode;


    RadarTypeEnum(byte type, String name, byte protocolCode){
        this.type = (byte) type;
        this.name = name;
        this.protocolCode = protocolCode;
    }

    public String getName() {
        return name;
    }

    public byte getType(){
        return type;
    }

    public byte getProtocolCode(){
        return protocolCode;
    }

    public boolean isType(byte type) {
        return this.type == type;
    }


    public static RadarTypeEnum fromType(byte type){
         return Arrays.stream(RadarTypeEnum.values())
                 .filter(t->t.isType(type))
                 .findFirst()
                 .get();
    }

    /**
     * @param protocolCode
     * @return
     */
    public static RadarTypeEnum fromProtocolCode(byte protocolCode){
        return Arrays.stream(RadarTypeEnum.values())
                .filter(t->t.getProtocolCode()==protocolCode)
                .findFirst()
                .get();
    }
}