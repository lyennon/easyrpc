package com.lyennon.remoting;

/**
 * @author yong.liang
 */
public interface ResponseProcessorRegistry {

    void registerResponseProcessor(Integer code, NettyProcessor nettyProcessor);

}
