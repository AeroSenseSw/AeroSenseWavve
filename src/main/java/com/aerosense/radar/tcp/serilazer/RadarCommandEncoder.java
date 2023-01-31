
package com.aerosense.radar.tcp.serilazer;

import com.alipay.remoting.CommandEncoder;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import com.aerosense.radar.tcp.protocol.RadarProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/6/16 9:33
 * @version: 1.0
 */
@Slf4j
public class RadarCommandEncoder implements CommandEncoder {

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
            int index = out.writerIndex();
            RpcCommand cmd = (RpcCommand) msg;
            out.writeByte(RadarProtocol.PROTOCOL_CODE);
            Attribute<Byte> version = ctx.channel().attr(Connection.VERSION);
            byte ver = RadarProtocol.PROTOCOL_VERSION_1;
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
//            if (cmd instanceof RpcRequestCommand) {
//                RadarProtocolData requestObject = (RadarProtocolData) ((RpcRequestCommand) cmd).getRequestObject();
//                out.writeShortLE(requestObject.getFunction().getFunction());
//            }
            if (cmd.getContentLength() > 0) {
                out.writeBytes(cmd.getContent());
            }
            byte[] dat = new byte[out.readableBytes()];
            out.readBytes(dat);
//            System.out.println(ByteUtils.bytesToHexString(dat));
            out.writeBytes(dat);
        } else {
            String warnMsg = "cancel encode msg type [" + msg.getClass() + "] is not subclass of RpcCommand";
            log.warn(warnMsg);
        }
    }
}