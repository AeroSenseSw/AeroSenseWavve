package com.aerosense.radar.tcp.domain.dto;

import com.aerosense.radar.tcp.util.ByteUtil;

import java.io.Serializable;

/**
 * @description: BedLocationDto，unit：mm
 * @author jia.wu
 * @date 2024/1/15 17:05
 * @version 1.0.0
 */
public class BedLocationDto implements Serializable {
    private static final long serialVersionUID = 1;
    private String radarId;
    private int disToWall;
    private int bedToWall;
    private int bedRange;
    private int bedWidth;
    private int bedHeight;
    private int reserve;


    public BedLocationDto(){}

    public BedLocationDto(String radarId, int disToWall, int bedToWall, int bedRange, int bedWidth, int bedHeight, int reserve) {
        this(disToWall, bedToWall, bedRange, bedWidth, bedHeight, reserve);
        this.radarId = radarId;
    }

    public BedLocationDto(int disToWall, int bedToWall, int bedRange, int bedWidth, int bedHeight, int reserve) {
        this.disToWall = disToWall;
        this.bedToWall = bedToWall;
        this.bedRange = bedRange;
        this.bedWidth = bedWidth;
        this.bedHeight = bedHeight;
        this.reserve = reserve;
    }

    public String getRadarId() {
        return radarId;
    }

    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }

    public int getDisToWall() {
        return disToWall;
    }

    public void setDisToWall(int disToWall) {
        this.disToWall = disToWall;
    }

    public int getBedToWall() {
        return bedToWall;
    }

    public void setBedToWall(int bedToWall) {
        this.bedToWall = bedToWall;
    }

    public int getBedRange() {
        return bedRange;
    }

    public void setBedRange(int bedRange) {
        this.bedRange = bedRange;
    }

    public int getBedWidth() {
        return bedWidth;
    }

    public void setBedWidth(int bedWidth) {
        this.bedWidth = bedWidth;
    }

    public int getBedHeight() {
        return bedHeight;
    }

    public void setBedHeight(int bedHeight) {
        this.bedHeight = bedHeight;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    /**
     * @return
     */
    public byte[] toBytes(){
        byte[] bytes = new byte[24];
        System.arraycopy(ByteUtil.intToByteBig(disToWall), 0, bytes, 0, 4);
        System.arraycopy(ByteUtil.intToByteBig(bedToWall), 0, bytes, 4, 4);
        System.arraycopy(ByteUtil.intToByteBig(bedRange), 0, bytes, 8, 4);
        System.arraycopy(ByteUtil.intToByteBig(bedWidth), 0, bytes, 12, 4);
        System.arraycopy(ByteUtil.intToByteBig(bedHeight), 0, bytes, 16, 4);
        System.arraycopy(ByteUtil.intToByteBig(reserve), 0, bytes, 20, 4);
        return bytes;
    }

    /**
     * @param data
     * @return
     */
    public static BedLocationDto fromBytes(byte[] data) {
        int disToWall = ByteUtil.bytes2IntBig(data);
        int bedToWall = ByteUtil.bytes2IntBig(data, 4);
        int bedRange = ByteUtil.bytes2IntBig(data, 8);
        int bedWidth = ByteUtil.bytes2IntBig(data, 12);
        int bedHeight = ByteUtil.bytes2IntBig(data, 16);
        int reserve = ByteUtil.bytes2IntBig(data, 20);
        return new BedLocationDto(disToWall, bedToWall, bedRange, bedWidth, bedHeight, reserve);
    }

    @Override
    public String toString() {
        return "BedLocationDto{" +
                "radarId='" + radarId + '\'' +
                ", disToWall=" + disToWall +
                ", bedToWall=" + bedToWall +
                ", bedRange=" + bedRange +
                ", bedWidth=" + bedWidth +
                ", bedHeight=" + bedHeight +
                ", reserve=" + reserve +
                '}';
    }
}
