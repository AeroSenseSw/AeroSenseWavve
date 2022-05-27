
package com.timevary.radar.tcp.serilazer;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.Serializer;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import com.timevary.radar.tcp.protocol.RadarProtocolData;
import com.timevary.radar.tcp.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/4 11:33
 * @version: 1.0
 */
public class RadarSerializer implements Serializer {
    public static final int IDX = 4;
    public static final Byte IDX_BYTE = 4;
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final byte[] EMPTY_ARRAY = new byte[0];

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
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        try {
            if (bytes.length > 0) {
                RadarProtocolData radarProtocolData = new RadarProtocolData();
                byteBuf.writeBytes(bytes);
                short function = byteBuf.readShort();
                radarProtocolData.setFunction(FunctionEnum.from(function));
                if (radarProtocolData.getFunction() != FunctionEnum.UNDEFINED) {
                    byte[] radar = new byte[12];
                    byte[] data = new byte[0];
                    switch (FunctionEnum.from(function)) {
                        case createConnection:
                            byteBuf.readByte();
                            byte[] version = new byte[4];
                            System.arraycopy(bytes, 3, version, 0, version.length);
                            String versionStr = version[0] + "." + version[1] + "." + version[2] + "." + version[3];
                            System.arraycopy(bytes, 2 + 1 + version.length, radar, 0, radar.length);
                            System.out.println("设置雷达版本号:"+radarProtocolData.getRadarVersion());
                            radarProtocolData.setRadarId(ByteUtils.bytesToHexString(radar));
                            break;
                        //                            System.arraycopy(bytes, 2 + data.length, radar, 0, radar.length);
                        case getRadarId:
                            byteBuf.readBytes(radar);
                            radarProtocolData.setRadarId(ByteUtils.bytesToHexString(radar));
                            break;
                        default:
                            data = new byte[bytes.length - 2];
                            System.arraycopy(bytes, 2, data, 0, data.length);
                            break;
                    }
                    radarProtocolData.setData(data);
                    return (T) radarProtocolData;
                } else {
                    return (T) bytes;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            byteBuf.release();
        }
        return null;
    }
}
