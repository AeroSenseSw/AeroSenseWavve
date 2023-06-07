package com.aerosense.radar.tcp.util;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/12 11:07
 * @modified By：
 */
public class CRC16 {

    public static String getCRC16(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuilder sb = new StringBuilder("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        return result;
    }

}
