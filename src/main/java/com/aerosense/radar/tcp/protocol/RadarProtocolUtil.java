package com.aerosense.radar.tcp.protocol;


import com.aerosense.radar.tcp.util.ByteUtil;

/**
 * @description: RadarProtocolUtil
 * @author jia.wu
 * @date 2024/1/12 16:26
 * @version 1.0.0
 */
public class RadarProtocolUtil {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    /**
     * @param data
     * @return
     */
    public static String encodeHexString(byte[] data) {
        char[] out = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            out[i * 2] = HEX_ARRAY[v >>> 4];
            out[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(out);
    }

    /**
     * @param bytes
     * @return
     */
    public static String getHardwareVersion(byte[] bytes){
        if (null == bytes) {
            return "";
        }
        StringBuilder version = new StringBuilder();
        byte[] temp = new byte[4];
        for (int i = 0,len = bytes.length; i < len; i++){
            temp[3] = bytes[i];
            String hexString = String.format("%02d", ByteUtil.byte4ToInt(temp));
            version.append(hexString).append(".");
        }
        version.deleteCharAt(version.length()-1);
        return version.toString();
    }

    /**
     * @param retObj
     * @return
     */
    public static boolean isRetSuccess(RadarProtocolData retObj) {
        return retObj!=null&&retObj.getFunction()!=FunctionEnum.UNDEFINED
                &&ByteUtil.bytes2IntBig(retObj.getData()) == RadarProtocolConsts.RET_SUCCESS;
    }
}
