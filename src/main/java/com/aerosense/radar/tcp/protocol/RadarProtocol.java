package com.aerosense.radar.tcp.protocol;

import com.alipay.remoting.Protocol;
import com.alipay.remoting.ProtocolCode;

import java.nio.charset.StandardCharsets;

/**
 * @author jia.wu
 */
public interface RadarProtocol extends Protocol {
    byte[] PROTOCOL_DATA_CLASS_BYTES = RadarProtocolData.class.getName().getBytes(StandardCharsets.UTF_8);
    int PROTOCOL_HEADER_LENGTH = 14;
    int FUNCTION_BYTES_LENGTH = 2;
    /**
     * magicNum
     * @return
     */
    byte getProtocolCodeByte();

    /**
     * code
     * @return
     */
    ProtocolCode getProtocolCode();

    /**
     * @return
     */
    byte getProtocolVersion();

}
