package com.aerosense.radar.tcp.protocol;

import com.aerosense.radar.tcp.serilazer.RadarCommandDecoder;
import com.aerosense.radar.tcp.serilazer.RadarCommandEncoder;
import com.alipay.remoting.*;
import com.alipay.remoting.rpc.RpcCommandFactory;
import com.alipay.remoting.rpc.protocol.RpcCommandHandler;
import com.alipay.remoting.rpc.protocol.RpcHeartbeatTrigger;

/**
 * @description: AbstractRadarProtocol
 * @author jia.wu
 */
public abstract class AbstractRadarProtocol implements RadarProtocol{

    private CommandEncoder encoder;
    private CommandDecoder     decoder;
    private HeartbeatTrigger   heartbeatTrigger;
    private CommandHandler commandHandler;
    private CommandFactory commandFactory;

    public AbstractRadarProtocol() {
        this.encoder = new RadarCommandEncoder(this);
        this.decoder = new RadarCommandDecoder(this);
        this.commandFactory = new RpcCommandFactory();
        this.heartbeatTrigger = new RpcHeartbeatTrigger(this.commandFactory);
        this.commandHandler = new RpcCommandHandler(this.commandFactory);
    }

    @Override
    public CommandEncoder getEncoder() {
        return encoder;
    }

    @Override
    public CommandDecoder getDecoder() {
        return decoder;
    }

    @Override
    public HeartbeatTrigger getHeartbeatTrigger() {
        return heartbeatTrigger;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

}
