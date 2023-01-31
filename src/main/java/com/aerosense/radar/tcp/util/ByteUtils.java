package com.aerosense.radar.tcp.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ByteUtils {

    public static final int BEFORE = 0;
    public static final int AFTER = 1;

    /**
     */
    public static void printHexString(String str, byte[] bytes){
        System.out.println(str + "------->" + ByteUtils.bytesToHexString(bytes));
    }

    public static void printHexString(byte[] bytes){
        System.out.println(ByteUtils.bytesToHexString(bytes));
    }

    /**
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     */
    public static byte[] appendByte(byte[] byteArr, byte hex, int position){
        if (byteArr != null || byteArr.length > 0){
            byte[] clone = new byte[byteArr.length + 1];
            if (position == BEFORE){
                System.arraycopy(byteArr, 0, clone, 1, byteArr.length);
                clone[0] = hex;
            }else if (position == AFTER){
                System.arraycopy(byteArr, 0, clone, 0, byteArr.length);
                clone[clone.length -1] = hex;
            }
            return clone;
        }else {
            return null;
        }
    }

    /**
     */
    public static byte[] mergeByteArray(List<byte[]> byteList){
        if (byteList == null || byteList.size() <= 1){
            return null;
        }

        int length = 0;
        for (int i = 0; i < byteList.size(); i++){
            length += byteList.get(i).length;
        }

        byte[] result = new byte[length];
        int currentIndex = 0;
        for (int j = 0; j < byteList.size(); j++){
            System.arraycopy(byteList.get(j), 0, result, currentIndex, byteList.get(j).length);
            currentIndex += byteList.get(j).length;
        }
        return result;
    }

    public static byte[] mergeByteArray(byte[] ... byteList){
        if (byteList == null || byteList.length <= 1){
            return null;
        }

        int length = 0;
        for (int i = 0; i < byteList.length; i++){
            length += byteList[i].length;
        }

        byte[] result = new byte[length];
        int currentIndex = 0;
        for (int j = 0; j < byteList.length; j++){
            System.arraycopy(byteList[j], 0, result, currentIndex, byteList[j].length);
            currentIndex += byteList[j].length;
        }
        return result;
    }

    /**
     */
    public void printHexString(byte[] data, String charset) {
        try {
            System.out.println(new String(data, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     */
    public static byte[] intToBytesHigh2Low(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) (i & 0xff);
        result[1] = (byte) ((i >> 8) & 0xff);
        result[2] = (byte) ((i >> 16) & 0xff);
        result[3] = (byte) (i >> 24 & 0xff);
        return result;
    }

    /**
     */
    public static byte[] intToBytesLow2High(int i) {
        byte[] result = new byte[4];
        result[3] = (byte) (i & 0xff);
        result[2] = (byte) ((i >> 8) & 0xff);
        result[1] = (byte) ((i >> 16) & 0xff);
        result[0] = (byte) (i >> 24 & 0xff);
        return result;
    }

    /**
     */
    public static byte[] short2ByteNew(short x){
        byte high = (byte) (0x00FF & (x>>8));
        byte low = (byte) (0x00FF & x);
        byte[] bytes = new byte[2];
        bytes[0] = high;
        bytes[1] = low;
        return bytes;
    }

    /**
     */
    public static byte[] inversionArr(byte[] arr){
        byte[] temp = new byte[arr.length];
        int count = 0;
        for (int i = arr.length - 1; i >= 0; i--){
            temp[count] = arr[i];
            count++;
        }
        return temp;
    }

    public static int byteToInt(byte[] bytes){
        int result = 0;
        if(bytes.length == 4){
            int a = (bytes[0] & 0xff) << 24;
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }


    /**
     */
    public static float byteToFloat(byte[] arr, int index) {
        return Float.intBitsToFloat(getInt(arr, index));
    }

    /**
     */
    public static byte[] floatToByte(float f) {
        int intbits = Float.floatToIntBits(f);
        return getByteArray(intbits);
    }

    private static byte[] getByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >> 24);
        b[1] = (byte) ((i & 0x00ff0000) >> 16);
        b[2] = (byte) ((i & 0x0000ff00) >> 8);
        b[3] = (byte)  (i & 0x000000ff);
        return b;
    }

    private static int getInt(byte[] arr, int index) {
        return 	(0xff000000 	& (arr[index+0] << 24))  |
                (0x00ff0000 	& (arr[index+1] << 16))  |
                (0x0000ff00 	& (arr[index+2] << 8))   |
                (0x000000ff 	&  arr[index+3]);
    }


    /**
     */
    public static String bytesToString(byte[] bytes, int startPos, int length)
    {
        byte[] temp = new byte[length];
        System.arraycopy(bytes, startPos, temp, 0, length);
        String returnStr = "";
        try
        {
            returnStr = new String(temp, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return returnStr;
    }







    /**
     **/
    public static byte[] toLowcase(byte byteArray[]) {
        if (byteArray.length == 12) {
            byte temp1[] = new byte[4];
            byte temp2[] = new byte[4];
            byte temp0[] = new byte[4];
            byte temp3[] = new byte[12];
            for (int i = 0, j = 0; i < byteArray.length - 8 && j < 4; i++, j++) {
                temp0[j] = byteArray[i];
            }
            for (int i = 11, j = 0; i > 7 && j < 4; i--, j++) {
                temp2[j] = byteArray[i];
            }
            for (int i = 7, j = 0; i > 3 && j < 4; i--, j++) {
                temp1[j] = byteArray[i];
            }
            temp3 = byteMerger(byteMerger(temp0, temp1), temp2);
            return temp3;
        } else if (byteArray.length == 8) {
            byte temp0[] = new byte[4];
            byte temp1[] = new byte[4];
            for (int i = 0, j = 0; i < 4 && j < 4; i++, j++) {
                temp0[j] = byteArray[i];
            }
            for (int i = 7, j = 0; i > 3 && j < 4; i--, j++) {
                temp1[j] = byteArray[i];
            }
            return byteMerger(temp0, temp1);
        } else {
            byte temp1[] = new byte[4];
            for (int i = 0, j = 0; i < 4 && j < 4; i--, j++) {
                temp1[j] = byteArray[i];
            }
            return temp1;
        }
    }
    /**
     */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
public static byte[] hexString2Bytes(String src) {
    int l = src.length() / 2;
    byte[] ret = new byte[l];
    for (int i = 0; i < l; i++) {
        ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
    }
    return ret;
}
}
