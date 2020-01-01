package com.lyennon.remoting.model;

import com.lyennon.remoting.InvokeCallback;
import java.nio.channels.Channel;
import java.util.concurrent.CountDownLatch;

/**
 * @author yong.liang
 */
public class ResponseFuture {

    private final long opaque;

    private final Channel channel;

    private final long timeoutMillis;

    private final InvokeCallback invokeCallback;

    private final long beginTimestamp = System.currentTimeMillis();

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
}
