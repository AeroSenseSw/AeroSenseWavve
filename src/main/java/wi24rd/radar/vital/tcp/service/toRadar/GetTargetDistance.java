package wi24rd.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import wi24rd.radar.vital.tcp.handler.base.AbstractToRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;

import org.springframework.stereotype.Service;


/**
 * @author ：ywb
 * @date ：Created in 2022/1/8 17:17
 * @modified By：
 */
@Service
public class GetTargetDistance extends AbstractToRadarProtocolDataHandler {

    public Float process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getTargetDistance);
        float readFloat = byteBuf.readFloat() * 100;
        byteBuf.release();
        return readFloat;
    }

}