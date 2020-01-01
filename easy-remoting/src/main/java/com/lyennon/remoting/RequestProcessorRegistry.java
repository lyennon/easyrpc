package com.lyennon.remoting;

public interface RequestProcessorRegistry {

    void registerProcessor(Integer code, NettyProcessor nettyProcessor);
}
