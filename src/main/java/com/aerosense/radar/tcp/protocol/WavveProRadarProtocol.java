package com.aerosense.radar.tcp.protocol;

import com.alipay.remoting.ProtocolCode;

/**
 * Request command protocol for v1
 * format：fixLength(14）+dynamicLen(contentLen)
 * 0     1     2           4           6           8          10           12          14         16     contentLen+14
 * +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
 * |proto|ver  |type |cmd  |       requestId       | timeout   |       contentLen      | content  bytes ... ...
 * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----+-----+
 *
 * proto: protocol code for protocol magic number:12
 * ver: protocol version:1
 * type: package type.   request:1   response:0   request oneway:2(一次性请求，服务器不会返回状态）
 * cmd: cmd code for remoting command. heartbeat:0  request:1   response:2
 * requestId: id of request,auto increment from 1
 * timeout: client wait the response max milliseconds (0-32768)
 * contentLen: length of dynamic content
 * content bytes format: (function:2 bytes, contentData: contentLen-2 bytes)
 *
 * 0           2            contentLen-2
 * +-----------+----------------------+
 * |  function |content data  bytes ... ...
 * +-----------+----------+-----------+
 *
 * Response command protocol for v1
 * format：fixLength(14） + dynamicLen(contentLen)
 * 0     1     2     3     4           6           8          10           12          14         16     contentLen+14
 * +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
 * |proto|ver  | type|cmd  |   requestId           |respstatus |      contentLen       | content  bytes ... ...
 * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----+-----+
 * respstatus: response status code -> 0:success  >0:failure
 *        SUCCESS: 0;
 *        ERROR: 1;
 *        SERVER_EXCEPTION: 2;
 *        UNKNOWN: 3;
 *        SERVER_THREADPOOL_BUSY: 4;
 *        ERROR_COMM: 5;
 *        NO_PROCESSOR: 6;
 *        TIMEOUT: 7;
 *        CLIENT_SEND_ERROR: 8;
 *        CODEC_EXCEPTION: 9;
 *        CONNECTION_CLOSED: 16;
 *        SERVER_SERIAL_EXCEPTION: 17;
 *        SERVER_DESERIAL_EXCEPTION: 18;
 *
 */
public class WavveProRadarProtocol extends AbstractRadarProtocol {

    public static final byte PROTOCOL_CODE_BYTE = 0x15;
    public static final byte PROTOCOL_VERSION_1  = 0x01;
    public static final ProtocolCode PROTOCOL_CODE = ProtocolCode.fromBytes(new byte[]{PROTOCOL_CODE_BYTE});


    @Override
    public byte getProtocolCodeByte() {
        return PROTOCOL_CODE_BYTE;
    }

    @Override
    public ProtocolCode getProtocolCode() {
        return PROTOCOL_CODE;
    }

    @Override
    public byte getProtocolVersion() {
        return PROTOCOL_VERSION_1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"-"+PROTOCOL_CODE_BYTE;
    }


}
