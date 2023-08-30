package wi24rd.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import wi24rd.radar.vital.tcp.handler.base.AbstractToRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;

import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/3/16 9:34
 * @modified By：
 */
@Service
public class GetBreathBPMLowThresholdAlertTimer extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getBreathBPMLowThresholdAlertTimer);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }
}
