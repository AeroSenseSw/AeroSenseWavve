
package com.aerosense.radar.tcp.util;

import com.alipay.remoting.util.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip tool.
 */
public class InternetAddressUtil {
    
    public static final boolean PREFER_IPV6_ADDRESSES = Boolean.parseBoolean(System.getProperty("java.net.preferIPv6Addresses"));
    
    public static final String IPV6_START_MARK = "[";
    
    public static final String IPV6_END_MARK = "]";
    
    public static final String ILLEGAL_IP_PREFIX = "illegal ip: ";
    
    public static final String IP_PORT_SPLITER = ":";
    
    public static final int SPLIT_IP_PORT_RESULT_LENGTH = 2;
    
    public static final String PERCENT_SIGN_IN_IPV6 = "%";
    
    public static final String LOCAL_HOST = "localhost";
    
    private static final String LOCAL_HOST_IP_V4 = "127.0.0.1";
    
    private static final String LOCAL_HOST_IP_V6 = "[::1]";
    
    private static final String CHECK_OK = "ok";
    
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
    
    private static final String IPV4_TUPLE = "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])";
    
    private static final Pattern IPV4_PATTERN = Pattern
            .compile("(?<!\\d)" + IPV4_TUPLE + "\\." + IPV4_TUPLE + "\\." + IPV4_TUPLE + "\\." + IPV4_TUPLE + "(?!\\d)");
    
    /**
     */
    public static String localHostIP() {
        if (PREFER_IPV6_ADDRESSES) {
            return LOCAL_HOST_IP_V6;
        }
        return LOCAL_HOST_IP_V4;
    }
    
    /**
     */
    public static boolean isIPv4(String addr) {
        return InetAddressValidator.isIPv4Address(addr);
    }
    
    /**
     */
    public static boolean isIPv6(String addr) {
        return InetAddressValidator.isIPv6Address(removeBrackets(addr));
    }
    
    /**
     */
    public static boolean isIP(String addr) {
        return isIPv4(addr) || isIPv6(addr);
    }
    
    /**
     */
    public static boolean containsPort(String address) {
        return splitIPPortStr(address).length == SPLIT_IP_PORT_RESULT_LENGTH;
    }
    
    /**
     */
    public static String[] splitIPPortStr(String str) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException("ip and port string cannot be empty!");
        }
        String[] serverAddrArr;
        if (str.startsWith(IPV6_START_MARK) && containsIgnoreCase(str, IPV6_END_MARK)) {
            if (str.endsWith(IPV6_END_MARK)) {
                serverAddrArr = new String[1];
                serverAddrArr[0] = str;
            } else {
                serverAddrArr = new String[2];
                serverAddrArr[0] = str.substring(0, (str.indexOf(IPV6_END_MARK) + 1));
                serverAddrArr[1] = str.substring((str.indexOf(IPV6_END_MARK) + 2));
            }
        } else {
            serverAddrArr = str.split(":");
        }
        return serverAddrArr;
    }
    
    /**
     */
    public static String getIPFromString(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String result = "";
        if (containsIgnoreCase(str, IPV6_START_MARK) && containsIgnoreCase(str, IPV6_END_MARK)) {
            result = str.substring(str.indexOf(IPV6_START_MARK), (str.indexOf(IPV6_END_MARK) + 1));
            if (!isIPv6(result)) {
                result = "";
            }
        } else {
            Matcher m = IPV4_PATTERN.matcher(str);
            if (m.find()) {
                result = m.group();
            }
        }
        return result;
    }
    
    /**
     */
    public static String checkIPs(String... ips) {
        
        if (ips == null || ips.length == 0) {
            
            return CHECK_OK;
        }
        // illegal response
        StringBuilder illegalResponse = new StringBuilder();
        for (String ip : ips) {
            if (InternetAddressUtil.isIP(ip)) {
                continue;
            }
            illegalResponse.append(ip + ",");
        }
        
        if (illegalResponse.length() == 0) {
            return CHECK_OK;
        }
        
        return ILLEGAL_IP_PREFIX + illegalResponse.substring(0, illegalResponse.length() - 1);
    }
    
    /**
     */
    public static boolean checkOK(String checkIPsResult) {
        return CHECK_OK.equals(checkIPsResult);
    }
    
    /**
     */
    public static String removeBrackets(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.replaceAll("[\\[\\]]", "");
    }
    
    /**
     */
    public static boolean isDomain(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (Objects.equals(str, LOCAL_HOST)) {
            return true;
        }
        return DOMAIN_PATTERN.matcher(str).matches();
    }

    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        String str1 = str.toString().toLowerCase();
        String str2 = searchStr.toString().toLowerCase();
        return str1.contains(str2);
    }
}