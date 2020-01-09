package com.lyennon.remoting.model;

import com.lyennon.remoting.InvokeCallback;
import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private final AtomicBoolean executeCallbackOnlyOnce = new AtomicBoolean(false);

    private volatile RemotingTransporter responseCommand;

    private volatile boolean sendRequestOK = true;

    private volatile Throwable cause;

    public ResponseFuture(io.netty.channel.Channel channel, long opaque, long timeoutMillis, InvokeCallback invokeCallback) {
        this.opaque = opaque;
        this.channel = channel;
        this.timeoutMillis = timeoutMillis;
        this.invokeCallback = invokeCallback;
    }

    public RemotingTransporter waitResponse(long timeout) throws InterruptedException {
        this.countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        return this.responseCommand;
    }

    public void putResponse(RemotingTransporter responseCommand){
        this.responseCommand = responseCommand;
        this.countDownLatch.countDown();
    }

    public void setResponseCommand(RemotingTransporter responseCommand) {
        this.responseCommand = responseCommand;
    }

    public InvokeCallback getInvokeCallback() {
        return invokeCallback;
    }

    public void executeInvokeCallback() {
        if (invokeCallback != null) {
            if (this.executeCallbackOnlyOnce.compareAndSet(false, true)) {
                invokeCallback.operationComplete(this);
            }
        }
    }

    public void setSendRequestOK(boolean sendRequestOK) {
        this.sendRequestOK = sendRequestOK;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public boolean isSendRequestOK() {
        return sendRequestOK;
    }
}
