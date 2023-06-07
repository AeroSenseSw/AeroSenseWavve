package com.aerosense.radar.tcp.util;

import java.util.regex.Pattern;

/**
 * ipv4 ipv6 check util.s
 */
public class InetAddressValidator {
    
    private static final String PERCENT = "%";
    
    private static final String DOUBLE_COLON = "::";
    
    private static final String DOUBLE_COLON_FFFF = "::ffff:";
    
    private static final String FE80 = "fe80:";
    
    private static final int ZERO = 0;
    
    private static final int SEVEN = 7;
    
    private static final int FIVE = 5;
    
    private static final Pattern IPV4_PATTERN = Pattern
            .compile("^" + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)" + "(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}" + "$");
    
    private static final Pattern IPV6_STD_PATTERN = Pattern
            .compile("^" + "(?:[0-9a-fA-F]{1,4}:){7}" + "[0-9a-fA-F]{1,4}" + "$");
    
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
            .compile("^" + "(" + "(?:[0-9A-Fa-f]{1,4}" + "(?::[0-9A-Fa-f]{1,4})*)?" + ")" + "::"
                    
                    + "(" + "(?:[0-9A-Fa-f]{1,4}" + "(?::[0-9A-Fa-f]{1,4})*)?" + ")" + "$");
    
    private static final Pattern IPV6_MIXED_COMPRESSED_REGEX = Pattern.compile(
            "^" + "(" + "(?:[0-9A-Fa-f]{1,4}" + "(?::[0-9A-Fa-f]{1,4})*)?" + ")" + "::" + "(" + "(?:[0-9A-Fa-f]{1,4}:"
                    + "(?:[0-9A-Fa-f]{1,4}:)*)?" + ")" + "$");
    
    private static final Pattern IPV6_MIXED_UNCOMPRESSED_REGEX = Pattern
            .compile("^" + "(?:[0-9a-fA-F]{1,4}:){6}" + "$");
    
    /**
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }
    
    /**
     */
    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }
    
    /**
     */
    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }
    
    /**
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input) || isLinkLocalIPv6WithZoneIndex(input)
                || isIPv6IPv4MappedAddress(input) || isIPv6MixedAddress(input);
    }
    
    /**

     */
    public static boolean isIPv6MixedAddress(final String input) {
        int splitIndex = input.lastIndexOf(':');
        
        if (splitIndex == -1) {
            return false;
        }
        
        boolean ipv4PartValid = isIPv4Address(input.substring(splitIndex + 1));
        
        String ipV6Part = input.substring(ZERO, splitIndex + 1);
        if (DOUBLE_COLON.equals(ipV6Part)) {
            return ipv4PartValid;
        }
        
        boolean ipV6UncompressedDetected = IPV6_MIXED_UNCOMPRESSED_REGEX.matcher(ipV6Part).matches();
        boolean ipV6CompressedDetected = IPV6_MIXED_COMPRESSED_REGEX.matcher(ipV6Part).matches();
        
        return ipv4PartValid && (ipV6UncompressedDetected || ipV6CompressedDetected);
    }
    
    /**
     */
    public static boolean isIPv6IPv4MappedAddress(final String input) {
        if (input.length() > SEVEN && input.substring(ZERO, SEVEN).equalsIgnoreCase(DOUBLE_COLON_FFFF)) {
            String lowerPart = input.substring(SEVEN);
            return isIPv4Address(lowerPart);
        }
        return false;
    }
    
    /**
     */
    public static boolean isLinkLocalIPv6WithZoneIndex(String input) {
        if (input.length() > FIVE && input.substring(ZERO, FIVE).equalsIgnoreCase(FE80)) {
            int lastIndex = input.lastIndexOf(PERCENT);
            if (lastIndex > ZERO && lastIndex < (input.length() - 1)) {
                String ipPart = input.substring(ZERO, lastIndex);
                return isIPv6StdAddress(ipPart) || isIPv6HexCompressedAddress(ipPart);
            }
        }
        return false;
    }
    
    /**
     */
    public static boolean isValidIP(String ipAddress) {
        if (ipAddress == null || ipAddress.length() == 0) {
            return false;
        }
        
        return isIPv4Address(ipAddress) || isIPv6Address(ipAddress);
    }
    
    /**
     *
     * @return
     */
    public static Pattern getIpv4Pattern() {
        return IPV4_PATTERN;
    }
    
}
