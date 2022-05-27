package com.timevary.radar.tcp.util;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


/**
 * Network card operation tool class.
 */
@Slf4j
public class InetUtils {

    private static String selfIP;
    
    private static boolean useOnlySiteLocalInterface = false;

    private static boolean preferHostnameOverIP = false;
    
    private static final List<String> PREFERRED_NETWORKS = new ArrayList<String>();

    private static final List<String> IGNORED_INTERFACES = new ArrayList<String>();

    public static void setUseOnlySiteLocalInterface(boolean useOnlySiteLocalInterface) {
        InetUtils.useOnlySiteLocalInterface = useOnlySiteLocalInterface;
    }

    public static void setPreferHostnameOverIP(boolean preferHostnameOverIP) {
        InetUtils.preferHostnameOverIP = preferHostnameOverIP;
    }

    public static void addPreferredNetworks(List<String> preferredNetworks){
        PREFERRED_NETWORKS.addAll(preferredNetworks);
    }

    public static void addIgnoredInterfaces(List<String> ignoredInterfaces){
        IGNORED_INTERFACES.addAll(ignoredInterfaces);
    }

    static {
        parseSelfIp();
    }

    public static void parseSelfIp() {
        String tmpSelfIP = null;
        if (preferHostnameOverIP) {
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getLocalHost();
                if (inetAddress.getHostName().equals(inetAddress.getCanonicalHostName())) {
                    tmpSelfIP = inetAddress.getHostName();
                } else {
                    tmpSelfIP = inetAddress.getCanonicalHostName();
                }
            } catch (UnknownHostException ignore) {
                log.warn("Unable to retrieve localhost");
            }
        } else {
            tmpSelfIP = Objects.requireNonNull(findFirstNonLoopbackAddress()).getHostAddress();
        }
        if (InternetAddressUtil.PREFER_IPV6_ADDRESSES && !tmpSelfIP.startsWith(InternetAddressUtil.IPV6_START_MARK) && !tmpSelfIP
                .endsWith(InternetAddressUtil.IPV6_END_MARK)) {
            tmpSelfIP = InternetAddressUtil.IPV6_START_MARK + tmpSelfIP + InternetAddressUtil.IPV6_END_MARK;
            if (tmpSelfIP.contains(InternetAddressUtil.PERCENT_SIGN_IN_IPV6)) {
                tmpSelfIP = tmpSelfIP.substring(0, tmpSelfIP.indexOf(InternetAddressUtil.PERCENT_SIGN_IN_IPV6))
                        + InternetAddressUtil.IPV6_END_MARK;
            }
        }
        selfIP = tmpSelfIP;
    }

    public static String getSelfIP() {
        return selfIP;
    }
    
    /**
     * findFirstNonLoopbackAddress.
     *
     * @return {@link InetAddress}
     */
    public static InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;
        
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
                    nics.hasMoreElements(); ) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    log.debug("Testing interface: " + ifc.getDisplayName());
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else {
                        continue;
                    }
                    
                    if (!ignoreInterface(ifc.getDisplayName())) {
                        for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements(); ) {
                            InetAddress address = addrs.nextElement();
                            boolean isLegalIpVersion = InternetAddressUtil.PREFER_IPV6_ADDRESSES ? address instanceof Inet6Address
                                    : address instanceof Inet4Address;
                            if (isLegalIpVersion && !address.isLoopbackAddress() && isPreferredAddress(address)) {
                                log.debug("Found non-loopback interface: " + ifc.getDisplayName());
                                result = address;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            log.error("Cannot get first non-loopback address", ex);
        }
        
        if (result != null) {
            return result;
        }
        
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.warn("Unable to retrieve localhost");
        }
        
        return null;
    }
    
    private static boolean isPreferredAddress(InetAddress address) {
        if (useOnlySiteLocalInterface) {
            final boolean siteLocalAddress = address.isSiteLocalAddress();
            if (!siteLocalAddress) {
                log.debug("Ignoring address: " + address.getHostAddress());
            }
            return siteLocalAddress;
        }
        if (PREFERRED_NETWORKS.isEmpty()) {
            return true;
        }
        for (String regex : PREFERRED_NETWORKS) {
            final String hostAddress = address.getHostAddress();
            if (hostAddress.matches(regex) || hostAddress.startsWith(regex)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean ignoreInterface(String interfaceName) {
        for (String regex : IGNORED_INTERFACES) {
            if (interfaceName.matches(regex)) {
                log.debug("Ignoring interface: " + interfaceName);
                return true;
            }
        }
        return false;
    }
}
