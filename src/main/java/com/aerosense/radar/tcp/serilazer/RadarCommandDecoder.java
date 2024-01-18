
package com.aerosense.radar.tcp.serilazer;

import com.aerosense.radar.tcp.exception.RadarException;
import com.aerosense.radar.tcp.protocol.RadarProtocol;
import com.alipay.remoting.CommandCode;
import com.alipay.remoting.CommandDecoder;
import com.alipay.remoting.ResponseStatus;
import com.alipay.remoting.rpc.*;
import com.alipay.remoting.rpc.protocol.RpcCommandCode;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;
import com.alipay.remoting.rpc.protocol.RpcResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/6/16 11:42
 * @version: 1.0
 */
@Slf4j
public class RadarCommandDecoder implements CommandDecoder {
    private RadarProtocol radarProtocol;

    public RadarCommandDecoder(RadarProtocol radarProtocol) {
        this.radarProtocol = radarProtocol;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // the less length between response header and request header
        if (in.readableBytes() >= RadarProtocol.PROTOCOL_HEADER_LENGTH) {
            StringBuilder dump = new StringBuilder();
            ByteBufUtil.appendPrettyHexDump(dump, in);
            log.info("receive bytes {} - {}\n{}", ctx.channel(), in.readableBytes(), dump);
            in.markReaderIndex();
            byte protocol = in.readByte();
            if (protocol != radarProtocol.getProtocolCodeByte()) {
                String emsg = "Unknown protocol: " + protocol;
                log.error(emsg);
                throw new RadarException(emsg);
            }
            /*
             * ver: version for protocol
             * type: request/response/request oneway
             * code: code for remoting command
             * requestId: id of request
             * (resp)respStatus: response status
             * contentLen: length of content
             * content
             */
            byte version = in.readByte(); //protocol version
            byte type = in.readByte(); //type
            if (type == RpcCommandType.REQUEST || type == RpcCommandType.REQUEST_ONEWAY) {
                //decode request
                byte cmdCode = in.readByte();
                int requestId = in.readInt();
                byte serializer = RadarSerializer.IDX;
                int timeout = in.readShort();
                byte[] clazz = RadarProtocol.PROTOCOL_DATA_CLASS_BYTES;
                int contentLen = in.readInt();
                byte[] content = null;
                if (contentLen > 0) {
                    if (in.readableBytes() >= contentLen) {
                        content = new byte[contentLen];
                        in.readBytes(content);
                    } else {// not enough data
                        in.resetReaderIndex();
                        return;
                    }
                }
                RequestCommand command;
                if (cmdCode == CommandCode.HEARTBEAT_VALUE) {
                    command = new HeartbeatCommand();
                } else {
                    command = createRequestCommand(cmdCode);
                }
                command.setType(type);
                command.setId(requestId);
                command.setSerializer(serializer);
                command.setTimeout(timeout);
                command.setClazz(clazz);
                command.setContent(content);
                out.add(command);
            } else if (type == RpcCommandType.RESPONSE) {
                //decode response
                short cmdCode = in.readByte();
                int requestId = in.readInt();
                byte serializer = RadarSerializer.IDX;
                short status = in.readShort();
                int contentLen = in.readInt();
                byte[] clazz = RadarProtocol.PROTOCOL_DATA_CLASS_BYTES;
                byte[] content = null;
                if (contentLen > 0) {
                    if (in.readableBytes() >= contentLen) {
                        content = new byte[contentLen];
                        in.readBytes(content);
                    } else {// not enough data
                        in.resetReaderIndex();
                        return;
                    }
                }
                ResponseCommand command;
                if (cmdCode == CommandCode.HEARTBEAT_VALUE) {
                    command = new HeartbeatAckCommand();
                } else {
                    command = createResponseCommand(cmdCode);
                }
                command.setType(type);
                command.setId(requestId);
                command.setSerializer(serializer);
                command.setResponseStatus(ResponseStatus.valueOf(status));
                command.setClazz(clazz);
                command.setContent(content);
                command.setResponseTimeMillis(System.currentTimeMillis());
                command.setResponseHost((InetSocketAddress) ctx.channel().remoteAddress());
                out.add(command);
            } else {
                String emsg = "Unknown command type: " + type;
                log.error(emsg);
                throw new RadarException(emsg);
            }
        }
    }

    private ResponseCommand createResponseCommand(short cmdCode) {
        RpcResponseCommand command = new RpcResponseCommand();
        command.setCmdCode(RpcCommandCode.valueOf(cmdCode));
        return command;
    }

    private RequestCommand createRequestCommand(short cmdCode) {
        RpcRequestCommand command = new RpcRequestCommand();
        command.setCmdCode(RpcCommandCode.valueOf(cmdCode));
        command.setArriveTime(System.currentTimeMillis());
        return command;
    }
}
