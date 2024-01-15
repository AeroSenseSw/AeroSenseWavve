package com.aerosense.radar.tcp.domain.dto;

import com.aerosense.radar.tcp.util.ByteUtil;

import java.io.Serializable;

/**
 * @description: DetectionAreaDto，unit：mm
 *  xMin, xMax, yMin, yMax, zMin, zMax (Int * 6)
 * @author jia.wu
 * @date 2024/1/15 17:06
 * @version 1.0.0
 */
public class DetectionAreaDto implements Serializable {
    private static final long serialVersionUID = 1;
    /**radarId*/
    private String radarId;
    /**xMin*/
    private int xMin;
    /**xMax*/
    private int xMax;
    /**yMin*/
    private int yMin;
    /**yMax*/
    private int yMax;
    /**zMin*/
    private int zMin;
    /**zMax*/
    private int zMax;


    public DetectionAreaDto(){}

    public DetectionAreaDto(String radarId, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax);
        this.radarId = radarId;
    }

    public DetectionAreaDto(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public String getRadarId() {
        return radarId;
    }

    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public int getzMin() {
        return zMin;
    }

    public void setzMin(int zMin) {
        this.zMin = zMin;
    }

    public int getzMax() {
        return zMax;
    }

    public void setzMax(int zMax) {
        this.zMax = zMax;
    }

    /**
     * @return
     */
    public byte[] toBytes(){
        byte[] bytes = new byte[24];
        System.arraycopy(ByteUtil.intToByteBig(xMin), 0, bytes, 0, 4);
        System.arraycopy(ByteUtil.intToByteBig(xMax), 0, bytes, 4, 4);
        System.arraycopy(ByteUtil.intToByteBig(yMin), 0, bytes, 8, 4);
        System.arraycopy(ByteUtil.intToByteBig(yMax), 0, bytes, 12,4);
        System.arraycopy(ByteUtil.intToByteBig(zMin), 0, bytes, 16,4);
        System.arraycopy(ByteUtil.intToByteBig(zMax), 0, bytes, 20,4);
        return bytes;
    }

    /**
     * @param data
     * @return
     */
    public static DetectionAreaDto fromBytes(byte[] data) {
        int disToWall = ByteUtil.bytes2IntBig(data);
        int bedToWall = ByteUtil.bytes2IntBig(data, 4);
        int bedRange = ByteUtil.bytes2IntBig(data, 8);
        int bedWidth = ByteUtil.bytes2IntBig(data, 12);
        int bedHeight = ByteUtil.bytes2IntBig(data, 16);
        int reserve = ByteUtil.bytes2IntBig(data, 20);
        return new DetectionAreaDto(disToWall, bedToWall, bedRange, bedWidth, bedHeight, reserve);
    }

    @Override
    public String toString() {
        return "DetectionAreaDto{" +
                "radarId='" + radarId + '\'' +
                ", xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", zMin=" + zMin +
                ", zMax=" + zMax +
                '}';
    }
}
