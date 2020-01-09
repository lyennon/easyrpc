package com.lyennon.remoting.netty;

import com.lyennon.common.protocol.EasyProtocol;
import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.NettyProcessor;
import com.lyennon.remoting.RequestProcessorRegistry;
import com.lyennon.remoting.ResponseProcessorRegistry;
import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultNettyProcessor implements NettyProcessor, RequestProcessorRegistry, ResponseProcessorRegistry {

    private Map<Integer, NettyProcessor> requestProcessors = new HashMap<>();

    private Map<Long, NettyProcessor> responseProcessors = new HashMap<>();

    private ExecutorService publicExecutor;

    public DefaultNettyProcessor() {
        this(new HashMap<Integer, NettyProcessor>());
        this.publicExecutor = Executors.newFixedThreadPool(1, new NamedThreadFactory("NettyServerPublicExecutor"));
    }

    public DefaultNettyProcessor(Map<Integer, NettyProcessor> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    @Override
    public RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request) {
        byte transporterType = request.getTransporterType();
        NettyProcessor processor;
        if (transporterType == EasyProtocol.REQUEST_REMOTING) {
            int code = request.getCode();
            processor = requestProcessors.get(code);
        } else if (transporterType == EasyProtocol.RESPONSE_REMOTING) {
            long code = request.getOpaque();
            processor = responseProcessors.get(code);
        } else {
            throw new RuntimeException("transporterType exception");
        }
        return processor.process(ctx, request);
    }

    @Override
    public void registerProcessor(Integer code, NettyProcessor nettyProcessor) {
        this.requestProcessors.put(code, nettyProcessor);
    }

    @Override
    public void registerResponseProcessor(Long code, NettyProcessor nettyProcessor) {
        this.responseProcessors.put(code, nettyProcessor);
    }

    @Override
    public void unRegisterResponseProcessor(Long code) {
        this.responseProcessors.remove(code);
    }
}
