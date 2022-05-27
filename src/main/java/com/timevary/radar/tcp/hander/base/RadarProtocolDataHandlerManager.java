package com.timevary.radar.tcp.hander.base;

import com.google.common.base.Preconditions;
import com.timevary.radar.tcp.exception.RadarException;
import com.timevary.radar.tcp.protocol.FunctionEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： jia.w@timevary.com
 * @date： 2021/8/12 10:02
 * @version: 1.0
 */
@Slf4j
public class RadarProtocolDataHandlerManager {
    private static final ConcurrentHashMap<FunctionEnum, RadarProtocolDataHandler> HANDLER_MAP = new ConcurrentHashMap<>();

    public static void registerHandler(RadarProtocolDataHandler handler) {
        Preconditions.checkNotNull(handler, "handler is null");
        Preconditions.checkNotNull(handler.interests(), "handler interest function is null");
        handler.interests().forEach(functionEnum -> {
            RadarProtocolDataHandler oldHandler = HANDLER_MAP.putIfAbsent(functionEnum, handler);
            if(oldHandler != null){
                throw new RadarException(functionEnum.toString() + " function handler registered");
            }else{
                log.info("register radar protocol data handler successful. {}", functionEnum);
            }
        });
    }

    public static RadarProtocolDataHandler unRegisterHandler(FunctionEnum functionEnum) {
        RadarProtocolDataHandler radarProtocolDataHandler = HANDLER_MAP.remove(functionEnum);
        if(radarProtocolDataHandler != null){
            log.info("unregister radar protocol data handler successful. {}", functionEnum);
        }
        return radarProtocolDataHandler;
    }

    public static void unRegisterHandler(RadarProtocolDataHandler handler) {
        Preconditions.checkNotNull(handler, "handler is null");
        Preconditions.checkNotNull(handler.interests(), "handler interest function is null");
        handler.interests().forEach(RadarProtocolDataHandlerManager::unRegisterHandler);
    }

    public static RadarProtocolDataHandler getHandler(FunctionEnum functionEnum) {
        return HANDLER_MAP.get(functionEnum);
    }

    public static void clear() {
        HANDLER_MAP.clear();
        log.info("clear radar protocol data handler map successful.");
    }
}
