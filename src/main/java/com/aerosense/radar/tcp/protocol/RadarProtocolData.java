package com.aerosense.radar.tcp.protocol;

import com.aerosense.radar.tcp.util.ByteUtil;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/3 14:30
 * @version: 1.0
 */
@SuperBuilder
@NoArgsConstructor
public class RadarProtocolData implements Serializable {
    /** For serialization  */
    private static final long serialVersionUID = 1;

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
    /**
     * @return
     */
    public static final RadarProtocolData newEmptyInstance(){
        return new RadarProtocolData();
    }
    /**
     * @param id
     * @param function
     * @param data
     * @return
     */
    public RadarProtocolData fillFunctionData(String id, FunctionEnum function, byte[] data){
        this.setRadarId(id);
        this.setFunction(function);
        this.setData(data);
        return this;
    }

    /**
     * @return
     */
    public static final RadarProtocolData newFunctionInstance(FunctionEnum function, byte[] data){
        RadarProtocolData radarProtocolData = RadarProtocolData.newEmptyInstance();
        radarProtocolData.setFunction(function);
        radarProtocolData.setData(data);
        return radarProtocolData;
    }

    public static final RadarProtocolData newInstance(String id, FunctionEnum function, byte[] data){
        RadarProtocolData radarProtocolData = RadarProtocolData.newEmptyInstance();
        radarProtocolData.setRadarId(id);
        radarProtocolData.setFunction(function);
        radarProtocolData.setData(data);
        return radarProtocolData;
    }

    public final RadarProtocolData success(){
        this.setData(ByteUtil.intToByteBig(RadarProtocolConsts.RET_SUCCESS));
        return this;
    }

    public final RadarProtocolData failure(){
        this.setData(ByteUtil.intToByteBig(RadarProtocolConsts.RET_FAILURE));
        return this;
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

}
