
package com.timevary.radar.tcp.protocol;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/3 14:30
 * @version: 1.0
 */
@SuperBuilder
@NoArgsConstructor
public class RadarProtocolData implements Serializable {
    /** For serialization  */
    private static final long serialVersionUID = 1;
    /**协议数据固定长度：function：1， radarId：12，radarVersion：8*/
    public static final int DATA_FIX_LEN = 15;

    /**函数接口*/
    private FunctionEnum    function;
    /**雷达id*/
    private String          radarId;
    /**雷达版本号*/
    private String          radarVersion;
    /**雷达上报数据*/
    private byte[]          data = new byte[4];

    public FunctionEnum getFunction() {
        return function;
    }

    public void setFunction(FunctionEnum function) {
        this.function = function;
    }

    public String getRadarId() {
        return radarId;
    }

    public void setRadarId(String radarId) {
        this.radarId = radarId;
    }

    public String getRadarVersion() {
        return radarVersion;
    }

    public void setRadarVersion(String radarVersion) {
        this.radarVersion = radarVersion;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RadarProtocolData{" +
                "function=" + function +
                ", radarId='" + radarId + '\'' +
                ", radarVersion='" + radarVersion + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
//
//    /**
//     * 转化为之前http协议传输的数据格式字节数组
//     * @return
//     */
//    public byte[] toBytes(){
//        StringBuilder fixStr = new StringBuilder();
//        fixStr.append(function.toString()).append(",");
//        fixStr.append(radarId);
//        if(function!=FunctionEnum.HeatMap){
//            fixStr.append(",").append(radarVersion);
//        }
//        int heatMapLen =  getData()!=null ? getData().length : 0;
//        if(heatMapLen>0){
//            fixStr.append(",");
//        }
//        byte[] fixBytes = fixStr.toString().getBytes(StandardCharsets.UTF_8);
//        byte[] data = new byte[fixBytes.length + heatMapLen];
//        System.arraycopy(fixBytes, 0, data, 0, fixBytes.length);
//        if(heatMapLen>0){
//            System.arraycopy(getData(),0, data, fixBytes.length, heatMapLen);
//        }
//        return data;
//    }
}
