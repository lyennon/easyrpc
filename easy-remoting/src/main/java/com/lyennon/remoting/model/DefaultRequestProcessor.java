package com.lyennon.remoting.model;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.RequestProcessorRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultRequestProcessor implements RequestProcessor, RequestProcessorRegistry {


    private Map<Integer,RequestProcessor> requestProcessors;

    private ExecutorService publicExecutor;

    public DefaultRequestProcessor() {
        this(new HashMap<Integer, RequestProcessor>());
        this.publicExecutor = Executors.newFixedThreadPool(1, new NamedThreadFactory("NettyServerPublicExecutor"));
    }

    public DefaultRequestProcessor(Map<Integer, RequestProcessor> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    @Override
    public RemotingTransporter processRequest(RemotingTransporter request) {
        int code = request.getCode();
        RequestProcessor requestProcessor = requestProcessors.get(code);
        return requestProcessor.processRequest(request);
    }

    @Override
    public void registerProcessor(Integer code, RequestProcessor requestProcessor) {
        this.requestProcessors.put(code,requestProcessor);
    }
}
