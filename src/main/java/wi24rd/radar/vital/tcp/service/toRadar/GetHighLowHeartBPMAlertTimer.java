package wi24rd.radar.vital.tcp.service.toRadar;

import io.netty.buffer.ByteBuf;
import wi24rd.radar.vital.tcp.handler.base.AbstractToRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;

import org.springframework.stereotype.Service;

/**
 * @author ：ywb
 * @date ：Created in 2022/2/17 17:09
 * @modified By：
 */
@Service
public class GetHighLowHeartBPMAlertTimer extends AbstractToRadarProtocolDataHandler {

    public Integer process(String radarId) throws Exception {
        ByteBuf byteBuf = super.processDo(radarId, FunctionEnum.getHighLowHeartBPMAlertTimer);
        int readInt = byteBuf.readInt();
        byteBuf.release();
        return readInt;
    }

}
