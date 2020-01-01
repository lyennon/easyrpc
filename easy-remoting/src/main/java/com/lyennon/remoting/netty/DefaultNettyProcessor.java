package com.lyennon.remoting.netty;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.RequestProcessorRegistry;

import com.lyennon.remoting.NettyProcessor;
import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultNettyProcessor implements NettyProcessor, RequestProcessorRegistry {


    private Map<Integer, NettyProcessor> requestProcessors;

    private ExecutorService publicExecutor;

    public DefaultNettyProcessor() {
        this(new HashMap<Integer, NettyProcessor>());
        this.publicExecutor = Executors.newFixedThreadPool(1, new NamedThreadFactory("NettyServerPublicExecutor"));
    }

    public DefaultNettyProcessor(Map<Integer, NettyProcessor> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    @Override
    public RemotingTransporter process(ChannelHandlerContext ctx,RemotingTransporter request) {
        int code = request.getCode();
        NettyProcessor nettyProcessor = requestProcessors.get(code);
        return nettyProcessor.process(ctx,request);
    }

    @Override
    public void registerProcessor(Integer code, NettyProcessor nettyProcessor) {
        this.requestProcessors.put(code, nettyProcessor);
    }
}
