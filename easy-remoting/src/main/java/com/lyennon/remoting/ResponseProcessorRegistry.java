package com.lyennon.remoting;

/**
 * @author yong.liang
 */
public interface ResponseProcessorRegistry {

    void registerResponseProcessor(Long code, NettyProcessor nettyProcessor);

    void unRegisterResponseProcessor(Long code);
}
