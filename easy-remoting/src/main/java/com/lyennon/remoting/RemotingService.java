package com.lyennon.remoting;

import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.channel.Channel;

/**
 * @author yong.liang
 */
public interface RemotingService {
    RemotingTransporter invokeSync(final Channel channel, final RemotingTransporter request,
        final long timeoutMillis) throws InterruptedException;

    void invokeAsync(final Channel channel, final RemotingTransporter request, final long timeoutMillis,
        final InvokeCallback invokeCallback);

    void invokeOneway(final Channel channel, final RemotingTransporter request, final long timeoutMillis);
}
