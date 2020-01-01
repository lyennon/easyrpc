package com.lyennon.remoting;

import com.lyennon.remoting.model.ResponseFuture;

/**
 * @author yong.liang
 */
public interface InvokeCallback {

    void operationComplete(final ResponseFuture responseFuture);
}

