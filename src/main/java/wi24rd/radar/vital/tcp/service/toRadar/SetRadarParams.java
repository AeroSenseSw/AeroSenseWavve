package wi24rd.radar.vital.tcp.service.toRadar;

import com.alipay.remoting.exception.RemotingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import wi24rd.radar.vital.tcp.handler.base.AbstractToRadarProtocolDataHandler;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;
import wi24rd.radar.vital.tcp.protocol.RadarProtocolData;
import wi24rd.radar.vital.tcp.protocol.http.GetSetRadarParamVo;
import wi24rd.radar.vital.tcp.protocol.http.ResponseCode;
import wi24rd.radar.vital.tcp.protocol.http.ResponseResult;
import wi24rd.radar.vital.tcp.server.RadarTcpServer;
import wi24rd.radar.vital.tcp.util.RadarCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author ：ywb
 * @date ：Created in 2022/1/7 11:30
 * @modified By：
 */
@Service
@Slf4j
public class SetRadarParams extends AbstractToRadarProtocolDataHandler {
    @Autowired
    private RadarTcpServer radarTcpServer;

    public ResponseResult process(GetSetRadarParamVo getRadarParamVo) {
        log.debug("set function {}", getRadarParamVo);
        ResponseResult check = RadarCheck.check(getRadarParamVo, radarTcpServer);
        if (check != null) {
            return check;
        }
        try {
            return doSet(getRadarParamVo);
        } catch (Exception e) {
            //可能超时
            for (int i = 0; i < 5; i++) {
                try {
                    return doSet(getRadarParamVo);
                } catch (RemotingException | InterruptedException ex) {
                    log.error("Setting timeout: {}, retrying", getRadarParamVo.getCode());
                }
            }
        }
        return new ResponseResult(ResponseCode.SERVER_ERROR);
    }
    @Override
    public Integer process(String radarId) throws RemotingException, InterruptedException {
        return null;
    }

    private ResponseResult doSet(GetSetRadarParamVo getRadarParamVo) throws RemotingException, InterruptedException {
        doSetReal(getRadarParamVo);
        int data = (int) getRadarParamVo.getData();
        if (data == 1) {
            return new ResponseResult(getRadarParamVo.getCode(), "success", 1);
        } else {
            return new ResponseResult(getRadarParamVo.getCode(), "fail", 0);
        }
    }

    private void doSetReal(GetSetRadarParamVo getRadarParamVo) throws RemotingException, InterruptedException {

        Object data = getRadarParamVo.getData();
        if (getRadarParamVo.getCode() == 1001) {
            data = (int) getRadarParamVo.getData() / 50;
        }
        if (getRadarParamVo.getCode().shortValue() == FunctionEnum.setLiveBedNoBackReportTime.getFunction()) {
            data = (int) getRadarParamVo.getData() * 60;
        }

        ByteBuf dataBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        RadarProtocolData radarProtocolData = new RadarProtocolData();
        radarProtocolData.setRadarId(getRadarParamVo.getRadarId());
        radarProtocolData.setFunction(FunctionEnum.from(getRadarParamVo.getCode().shortValue()));
        try {

            if (radarProtocolData.getFunction() == FunctionEnum.setTargetDistance) {
                if (data instanceof Double) {
                    Double newData = (Double) data;
                    dataBuf.writeFloat(newData.floatValue() / 100);
                } else {
                    dataBuf.writeFloat((float) (int) data / 100);
                }
            } else {
                if (data instanceof Integer) {
                    dataBuf.writeInt((int) data);
                } else if (data instanceof Double) {
                    Double newData = (Double) data;
                    dataBuf.writeFloat(newData.floatValue());
                }
            }
            byte[] dataBt = new byte[dataBuf.readableBytes()];
            dataBuf.readBytes(dataBt);

            radarProtocolData.setData(dataBt);
            RadarProtocolData process = (RadarProtocolData) super.process(radarProtocolData);
            dataBuf.writeBytes(process.getData());
            int readInt = process.getFunction() == FunctionEnum.calibration ? dataBuf.readIntLE() : dataBuf.readInt();
            if (readInt == 1 ) {
                getRadarParamVo.setData(1);
            } else {
                getRadarParamVo.setData(0);
            }
        } finally {
            dataBuf.release();
        }
    }
}
