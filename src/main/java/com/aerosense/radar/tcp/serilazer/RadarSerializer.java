
package com.aerosense.radar.tcp.serilazer;

import com.aerosense.radar.tcp.protocol.FunctionEnum;
import com.aerosense.radar.tcp.protocol.RadarProtocol;
import com.aerosense.radar.tcp.protocol.RadarProtocolData;
import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.Serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
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

    @Override
    public byte[] serialize(Object object) throws CodecException {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof RadarProtocolData) {
            RadarProtocolData protocolData = (RadarProtocolData) object;
            int dataLen = protocolData.getData() == null ? 0 : protocolData.getData().length;
            byte[] bytes = new byte[RadarProtocol.FUNCTION_BYTES_LENGTH + dataLen];
            byte[] functionBytes = toByteArray(protocolData.getFunction().getFunction());
            System.arraycopy(functionBytes, 0, bytes, 0, 2);
            if (dataLen > 0) {
                System.arraycopy(protocolData.getData(), 0, bytes, 2, dataLen);
            }
            return bytes;
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
                int dataLen = bytes.length - RadarProtocol.FUNCTION_BYTES_LENGTH;
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
     * @param value
     * @return
     */
    public static byte[] toByteArray(short value) {
        return new byte[]{(byte)(value >> 8), (byte)(value&0xff)};
    }

    /**
     * @param b1
     * @param b2
     * @return
     */
    public static short fromBytes(byte b1, byte b2) {
        return (short)(b1 << 8 | b2 & 255);
    }
}
