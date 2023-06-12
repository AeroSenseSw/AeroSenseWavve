
package com.radar.radar.tcp.serilazer;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.Serializer;
import com.radar.radar.tcp.protocol.FunctionEnum;
import com.radar.radar.tcp.protocol.RadarProtocolData;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/4 11:33
 * @version: 1.0
 */
public class RadarSerializer implements Serializer {
    public static final int IDX = 4;
    public static final Byte IDX_BYTE = 4;
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final int RADAR_PROTOCOL_FUNCTION_BYTE_LENGTH = 2;

    @Override
    public byte[] serialize(Object object) throws CodecException {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof RadarProtocolData) {
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
            try {
                RadarProtocolData protocolData = (RadarProtocolData) object;
                int dataLen = protocolData.getData() == null ? 0 : protocolData.getData().length;
                byte[] bytes = new byte[dataLen + 2];
//            bytes[0] = protocolData.getFunction().getFunction();
                byteBuf.writeShort(protocolData.getFunction().getFunction());
                byte[] functionNew = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(functionNew);
                System.arraycopy(functionNew, 0, bytes, 0, functionNew.length);
                System.arraycopy(protocolData.getData(), 0, bytes, functionNew.length, dataLen);
                return bytes;
            } finally {
                byteBuf.release();
            }
        } else if (object instanceof String) {
            return object.toString().getBytes(charset);
        } else if (object instanceof Exception) {
            return null;
        } else {
            throw new CodecException("unsupport serialize data type "
                    + object.getClass().toString());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, String clazz) {
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        if (bytes.length > 0) {
            short function = fromBytes(bytes[0], bytes[1]);
            FunctionEnum deserializedFunction = FunctionEnum.from(function);
            if (deserializedFunction != FunctionEnum.UNDEFINED) {
                radarProtocolData.setFunction(deserializedFunction);
                int dataLen = bytes.length - RADAR_PROTOCOL_FUNCTION_BYTE_LENGTH;
                if (dataLen > 0) {
                    byte[] data = new byte[dataLen];
                    System.arraycopy(bytes, 2, data, 0, dataLen);
                    radarProtocolData.setData(data);
                }
            } else {
                radarProtocolData.setFunction(FunctionEnum.UNDEFINED);
                radarProtocolData.setData(bytes);
            }
        }
        return (T)radarProtocolData;
    }
    /**
     * 大端序解析2字节为short
     * @param b1
     * @param b2
     * @return
     */
    public static short fromBytes(byte b1, byte b2) {
        return (short)(b1 << 8 | b2 & 255);
    }
}
