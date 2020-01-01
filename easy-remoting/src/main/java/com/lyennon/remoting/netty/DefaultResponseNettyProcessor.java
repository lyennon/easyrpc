package com.lyennon.remoting.netty;

import com.lyennon.remoting.NettyProcessor;
import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.model.ResponseFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yong.liang
 */
@Slf4j
public class DefaultResponseNettyProcessor implements NettyProcessor {

    private ResponseFuture responseFuture;

    public DefaultResponseNettyProcessor() {
    }

    public DefaultResponseNettyProcessor(ResponseFuture responseFuture) {
        this.responseFuture = responseFuture;
    }

    @Override
    public RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request) {
        if (responseFuture != null) {
            responseFuture.setResponseCommand(cmd);
            if (responseFuture.getInvokeCallback() != null) {
                executeInvokeCallback(responseFuture);
            } else {
                responseFuture.putResponse(cmd);
                responseFuture.release();
            }
        } else {
            log.warn("receive response, but not matched any request, " + ctx.channel().localAddress());
        }
    }
}
