
package com.aerosense.radar.tcp.serilazer;

import com.aerosense.radar.tcp.protocol.RadarProtocol;
import com.alipay.remoting.CommandEncoder;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/6/16 9:33
 * @version: 1.0
 */
@Slf4j
public class RadarCommandEncoder implements CommandEncoder {
    private RadarProtocol radarProtocol;

    public RadarCommandEncoder(RadarProtocol radarProtocol) {
        this.radarProtocol = radarProtocol;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (msg instanceof RpcCommand) {
            /*
             * proto: magic code for protocol
             * ver: version for protocol
             * type: request/response/request oneway
             * cmdcode: code for remoting command
             * requestId: id of request
             * (req)timeout: request timeout. //do by server
             * (resp)respStatus: response status
             * cotentLen: length of content //do by server
             * content
             */
            RpcCommand cmd = (RpcCommand) msg;
            out.writeByte(radarProtocol.getProtocolCodeByte());
            Attribute<Byte> version = ctx.channel().attr(Connection.VERSION);
            byte ver = radarProtocol.getProtocolVersion();
            if (version != null && version.get() != null) {
                ver = version.get();
            }
            out.writeByte(ver);
            out.writeByte(cmd.getType());
            out.writeByte(((RpcCommand) msg).getCmdCode().value());
            out.writeInt(cmd.getId());
            if (cmd instanceof RequestCommand) {
                //timeout
                out.writeShort(((RequestCommand) cmd).getTimeout());
            }
            if (cmd instanceof ResponseCommand) {
                //response status
                ResponseCommand response = (ResponseCommand) cmd;
                out.writeShort(response.getResponseStatus().getValue());
            }
            out.writeInt(cmd.getContentLength());
            if (cmd.getContentLength() > 0) {
                out.writeBytes(cmd.getContent());
            }

            StringBuilder dump = new StringBuilder();
            ByteBufUtil.appendPrettyHexDump(dump, out);
            log.info("send bytes: {} - {}\n{}", ctx.channel(), out.readableBytes(), dump);
        } else {
            String warnMsg = "cancel encode msg type [" + msg.getClass() + "] is not subclass of RpcCommand";
            log.warn(warnMsg);
        }
    }
}