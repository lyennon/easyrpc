package com.lyennon.remoting;

import com.lyennon.remoting.model.RequestProcessor;

public interface RequestProcessorRegistry {

    void registerProcessor(Integer code, RequestProcessor requestProcessor);
}
