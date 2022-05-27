package com.timevary.radar.tcp.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ByteUtils {

    public static final int BEFORE = 0;
    public static final int AFTER = 1;

    /**
     * 打印16进制的字符串
     * @param str
     * @param bytes
     */
    public static void printHexString(String str, byte[] bytes){
        System.out.println(str + "------->" + ByteUtils.bytesToHexString(bytes));
    }

    public static void printHexString(byte[] bytes){
        System.out.println(ByteUtils.bytesToHexString(bytes));
    }

    /**
     * byte数组转16进制格式的字符串
     * @param src
     * @return
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
     * 16进制格式的字符串转byte数组
     * @param hexString
     * @return
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
     * 追加字节
     * @param byteArr 原字节数组
     * @param hex  需要追加的字节
     * @param position  ByteUtils.BEFORE  在前面追加
     *                  ByteUtils.AFTER   在后面追加
     * @return
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
     * 用来拼接、合并byte数组（传入的列表元素至少有 2 个）
     * @param byteList
     * @return
     */
    public static byte[] mergeByteArray(List<byte[]> byteList){
        if (byteList == null || byteList.size() <= 1){
            return null;
        }

        //统计所有字节数组总长度
        int length = 0;
        for (int i = 0; i < byteList.size(); i++){
            length += byteList.get(i).length;
        }

        //拷贝数组到新的数组
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

        //统计所有字节数组总长度
        int length = 0;
        for (int i = 0; i < byteList.length; i++){
            length += byteList[i].length;
        }

        //拷贝数组到新的数组
        byte[] result = new byte[length];
        int currentIndex = 0;
        for (int j = 0; j < byteList.length; j++){
            System.arraycopy(byteList[j], 0, result, currentIndex, byteList[j].length);
            currentIndex += byteList[j].length;
        }
        return result;
    }

    /**
     * 将byte[]以字符串的形式打印出来
     * @param data
     * @param charset
     */
    public void printHexString(byte[] data, String charset) {
        try {
            System.out.println(new String(data, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
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
     * int到byte[] 由底位到高位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
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
     * 将short转byte数组
     * @param x
     * @return
     */
    public static byte[] short2ByteNew(short x){
        byte high = (byte) (0x00FF & (x>>8));//定义第一个byte
        byte low = (byte) (0x00FF & x);//定义第二个byte
        byte[] bytes = new byte[2];
        bytes[0] = high;
        bytes[1] = low;
        return bytes;
    }

    /**
     * byte数组倒置
     * @param arr
     * @return
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

    /**
     * byte数组转int
     * @param bytes
     * @return
     */
//    public static int byteToInt(byte[] bs) {
//        int a = 0;
//        for (int i = bs.length - 1; i >= 0; i--) {
//            a += bs[i] * Math.pow(0xFF, bs.length - i - 1);
//        }
//        return a;
//    }
    public static int byteToInt(byte[] bytes){
        int result = 0;
        if(bytes.length == 4){
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }


//    public static int byteToInt(byte[] b, int index) {
//        if (b == null) {
//            throw new IllegalArgumentException("byte array is null!");
//        }
//        int l;
//        l = b[index + 0];
//        l &= 0xff;
//        l |= ((long) b[index + 1] << 8);
//        l &= 0xffff;
//        l |= ((long) b[index + 2] << 16);
//        l &= 0xffffff;
//        l |= ((long) b[index + 3] << 24);
//        return l;
//    }

    /**
     * 将byte[]转short
     * @param bytes
     * @return
     */
//    public static short byte2shortNew(byte[] bytes){
//        byte high = bytes[0];
//        byte low = bytes[1];
//        short z = (short)(((high & 0x00FF) << 8) | (0x00FF & low));
//        return z;
//    }

    /**
     * 从byte数组的index处的连续4个字节获得一个float
     * @param arr
     * @param index
     * @return
     */
    public static float byteToFloat(byte[] arr, int index) {
        return Float.intBitsToFloat(getInt(arr, index));
    }

    /**
     * 从byte数组的index处的连续4个字节获得一个float
     * @param f
     * @return
     */
    public static byte[] floatToByte(float f) {
        int intbits = Float.floatToIntBits(f);//将float里面的二进制串解释为int整数
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

    // 从byte数组的index处的连续4个字节获得一个int
    private static int getInt(byte[] arr, int index) {
        return 	(0xff000000 	& (arr[index+0] << 24))  |
                (0x00ff0000 	& (arr[index+1] << 16))  |
                (0x0000ff00 	& (arr[index+2] << 8))   |
                (0x000000ff 	&  arr[index+3]);
    }


    /**
     * byte[] �?String UTF-8编码
     * @return
     * @throws UnsupportedEncodingException
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnStr;
    }














//    /**
//     * 16进制的字符串表示转成字节数组
//     *
//     * @param hexString 16进制格式的字符串
//     * @return 转换后的字节数组
//     **/
//    public static byte[] toByteArray(String hexString) {
//        if (ByteUtils.isEmpty(hexString))
//            throw new IllegalArgumentException("this hexString must not be empty");
//        try {
//            hexString = new String(hexString.getBytes(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        hexString = hexString.toLowerCase();
//        final byte[] byteArray = new byte[hexString.length() / 2];
//        int k = 0;
//        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
//            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xFF);
//            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xFF);
//            byteArray[i] = (byte) (high << 4 | low);
//            k += 2;
//        }
//
//        return toLowcase(byteMerger(byteHade, byteArray));
//    }
//
//    private static boolean isEmpty(String hexString) {
//        if (hexString == null || hexString.equals(""))
//            return true;
//        else return false;
//    }
//
//    /**
//     * 向串口发送数据转为字节数组
//     */
//    private static byte byteHade[] = {(byte) 0xAA, (byte) 0xAA, (byte) 0x55, (byte) 0x55};
//
//    public static byte[] hex2byte(String hex) {
//        String digital = "0123456789ABCDEF";
//        String hex1 = hex.replace(" ", "");
//        char[] hex2char = hex1.toCharArray();
//        byte[] bytes = new byte[hex1.length() / 2];
//        byte temp;
//        for (int p = 0; p < bytes.length; p++) {
//            temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
//            temp += digital.indexOf(hex2char[2 * p + 1]);
//            bytes[p] = (byte) (temp & 0xFF);
//        }
//        return toLowcase(byteMerger(byteHade, bytes));
//    }
//
    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组 大端输入转小端，小端输入转大端
     * @return 16进制表示格式的字符串, 小端排序(大端输入转小端 ， 小端输入转大端)
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
//
//    /**
//     * 字节数组转成16进制表示格式的字符串
//     *
//     * @param byteArray 需要转换的字节数组
//     * @return 16进制表示格式的字符串
//     **/
//    public static String toHexString(byte[] byteArray) {
//
//        if (byteArray == null || byteArray.length < 1)
//            throw new IllegalArgumentException("this byteArray must not be null or empty");
//        char[] _16 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        final StringBuilder hexString = new StringBuilder();
//        byte temp1[] = new byte[4];
//        byte temp2[] = new byte[4];
//        byte temp0[] = new byte[4];
//        byte temp3[] = new byte[12];
//        for (int i = 0, j = 0; i < byteArray.length - 8 && j < 4; i++, j++) {
//          /*  if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
//                hexString.append("0");
//            hexString.append(Integer.toHexString(0xFF & byteArray[i]));*/
//       /*     hexString.append(_16[byteArray[i] >> 4 & 0xf])
//                    .append(_16[byteArray[i] & 0xf]);*/
//            temp0[j] = byteArray[i];
//        }
//        for (int i = 11, j = 0; i > 7 && j < 4; i--, j++) {
//          /*  if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
//                hexString.append("0");
//            hexString.append(Integer.toHexString(0xFF & byteArray[i]));*/
//       /*     hexString.append(_16[byteArray[i] >> 4 & 0xf])
//                    .append(_16[byteArray[i] & 0xf]);*/
//            temp2[j] = byteArray[i];
//        }
//        for (int i = 7, j = 0; i > 3 && j < 4; i--, j++) {
//          /*  if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
//                hexString.append("0");
//            hexString.append(Integer.toHexString(0xFF & byteArray[i]));*/
//       /*     hexString.append(_16[byteArray[i] >> 4 & 0xf])
//                    .append(_16[byteArray[i] & 0xf]);*/
//            temp1[j] = byteArray[i];
//        }
//        temp3 = byteMerger(byteMerger(temp0, temp1), temp2);
//        for (int i = 0; i < temp3.length; i++) {
//          /*  if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
//                hexString.append("0");
//            hexString.append(Integer.toHexString(0xFF & byteArray[i]));*/
//            hexString.append(_16[temp3[i] >> 4 & 0xf])
//                    .append(_16[temp3[i] & 0xf]);
//
//        }
//        return hexString.toString().toLowerCase();
//    }
//
//    /**
//     * 浮点转换为字节
//     *
//     * @param f
//     * @return
//     */
//    public static byte[] float2byte(float f) {
//
//        // 把float转换为byte[]
//        int fbit = Float.floatToIntBits(f);
//
//        byte[] b = new byte[4];
//        for (int i = 0; i < 4; i++) {
//            b[i] = (byte) (fbit >> (24 - i * 8));
//        }
//
//        // 翻转数组
//        int len = b.length;
//        // 建立一个与源数组元素类型相同的数组
//        byte[] dest = new byte[len];
//        // 为了防止修改源数组，将源数组拷贝一份副本
//        System.arraycopy(b, 0, dest, 0, len);
//        byte temp;
//        // 将顺位第i个与倒数第i个交换
//        for (int i = 0; i < len / 2; ++i) {
//            temp = dest[i];
//            dest[i] = dest[len - i - 1];
//            dest[len - i - 1] = temp;
//        }
//
//        return dest;
//
//    }
//
//    /**
//     * 字节转换为浮点
//     *
//     * @param b     字节（至少4个字节）
//     * @param index 开始位置
//     * @return
//     */
//    public static float byte2float(byte[] b, int index) {
//
//        int l;
//        l = b[index + 0];
//        l &= 0xff;
//        l |= ((long) b[index + 1] << 8);
//        l &= 0xffff;
//        l |= ((long) b[index + 2] << 16);
//        l &= 0xffffff;
//        l |= ((long) b[index + 3] << 24);
//        return Float.intBitsToFloat(l);
//    }
//
//    /* byte to  int */
//    public final static int getInt(byte[] b, int index) {
//        if (b == null) {
//            throw new IllegalArgumentException("byte array is null!");
//        }
//        int l;
//        l = b[index + 0];
//        l &= 0xff;
//        l |= ((long) b[index + 1] << 8);
//        l &= 0xffff;
//        l |= ((long) b[index + 2] << 16);
//        l &= 0xffffff;
//        l |= ((long) b[index + 3] << 24);
//        return l;
//    }
//
//    /**
//     * int到byte[] 由高位到低位
//     *
//     * @param i 需要转换为byte数组的整行值。
//     * @return byte数组
//     */
//    public static byte[] intToByteArray(int i) {
//        byte[] result = new byte[4];
//        result[0] = (byte) (i & 0xff);
//        result[1] = (byte) ((i >> 8) & 0xff);
//        result[2] = (byte) ((i >> 16) & 0xff);
//        result[3] = (byte) (i >> 24 & 0xff);
//        return result;
//    }
//
//
    /**
     * 数组合并
     *
     * @param bt1
     * @param bt2
     * @return
     */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
//
//    /**
//     * int值转换为ip
//     *
//     * @param addr
//     * @return
//     */
//    public static String intToIp(int addr) {
//        return ((addr & 0xFF) + "." +
//                ((addr >>>= 8) & 0xFF) + "." +
//                ((addr >>>= 8) & 0xFF) + "." +
//                ((addr >>>= 8) & 0xFF));
//    }
public static byte[] hexString2Bytes(String src) {
    int l = src.length() / 2;
    byte[] ret = new byte[l];
    for (int i = 0; i < l; i++) {
        ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
    }
    return ret;
}
}
