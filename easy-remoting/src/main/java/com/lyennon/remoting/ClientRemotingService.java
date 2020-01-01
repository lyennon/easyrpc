package com.lyennon.remoting;

import com.lyennon.remoting.model.RemotingTransporter;

/**
 * @author yong.liang
 */
public interface ClientRemotingService extends RemotingService {

    RemotingTransporter invokeSync(final String addr, final RemotingTransporter request,
        final long timeoutMillis);

    void invokeAsync(final String addr, final RemotingTransporter request, final long timeoutMillis,
        final InvokeCallback invokeCallback);

    void invokeOneway(final String addr, final RemotingTransporter request, final long timeoutMillis);
}
