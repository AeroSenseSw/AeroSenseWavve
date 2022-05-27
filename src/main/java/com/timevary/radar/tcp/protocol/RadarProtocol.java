
package com.timevary.radar.tcp.protocol;

import com.alipay.remoting.*;
import com.alipay.remoting.rpc.RpcCommandFactory;
import com.alipay.remoting.rpc.protocol.RpcCommandHandler;
import com.alipay.remoting.rpc.protocol.RpcHeartbeatTrigger;
import com.timevary.radar.tcp.serilazer.RadarCommandDecoder;
import com.timevary.radar.tcp.serilazer.RadarCommandEncoder;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/6/11
 * @version: 1.0
/**
 * Request command protocol for v1
 * 请求协议格式：fixLength(12个控制字节）+dynamicLen(报文内容，由contentLen决定其长度)
 * 0     1     2           4           6           8          10           12          14         16
 * +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
 * |proto|ver  |type |cmd  |       requestId       | timeout   |contentLen | content  bytes ... ...
 * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
 *
 * proto: protocol code for protocol magic number:0x12
 * ver: protocol version:1
 * type: package type.   request:1   response:0   request oneway:2(一次性请求，服务器不会返回状态）
 * cmd: cmd code for remoting command. heartbeat:0  request:1   response:2
 * requestId: id of request,auto increment from 1
 * timout: client wait the response max milliseconds (0-32768)
 * contentLen: length of dynamic content
 * content bytes format: (function:1 bytes, radarId:12 bytes, radarVersion:8 bytes, HeatMapData: contentLen-21 bytes)
 *
 * 0           1                                   12                      21            contentLen-21
 * +-----------+-----------------------------------+-----------------------+-----------------------+
 * |  function |radarId                            |   radarVersion        | HeatMap  bytes ... ...
 * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
 *
 * Response command protocol for v1
 * 响应协议格式：fixLength(12个控制字节） + dynamicLen(报文内容，由contentLen决定其长度)
 * 0     1     2     3     4           6           8          10           12          14         16
 * +-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+
 * |proto|ver  | type|cmd  |   requestId           |respstatus |contentLen | content  bytes ... ...
 * +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
 * respstatus: response status
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
public class RadarProtocol implements Protocol {
    /**协议数据类字节数组*/
    public static final byte[] DATA_CLASS_BYTES    = RadarProtocolData.class.getName().getBytes(StandardCharsets.UTF_8);

    /**协议码自定义魔数，作为判断是否是雷达的数据请求*/
    public static final byte   PROTOCOL_CODE       = (byte) 0x13;
    /** 雷达传输数据协议第一版 */
    public static final byte   PROTOCOL_VERSION_1  = (byte) 0x01;
    /** 协议头长度*/
    public static final int    PROTOCOL_HEADER_LEN = 14;

    private CommandEncoder     encoder;
    private CommandDecoder     decoder;
    private HeartbeatTrigger   heartbeatTrigger;
    private CommandHandler     commandHandler;
    private CommandFactory     commandFactory;

    public RadarProtocol() {
        this.encoder = new RadarCommandEncoder();
        this.decoder = new RadarCommandDecoder();
        this.commandFactory = new RpcCommandFactory();
        this.heartbeatTrigger = new RpcHeartbeatTrigger(this.commandFactory);
        this.commandHandler = new RpcCommandHandler(this.commandFactory);
    }

    @Override
    public CommandEncoder getEncoder() {
        return encoder;
    }

    @Override
    public CommandDecoder getDecoder() {
        return decoder;
    }

    @Override
    public HeartbeatTrigger getHeartbeatTrigger() {
        return heartbeatTrigger;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }
}
