package com.lyennon.remoting.netty;

import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.model.RequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<RemotingTransporter> {

    private RequestProcessor requestProcessor;

    public NettyServerHandler(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingTransporter msg) throws Exception {
            requestProcessor.processRequest(msg);
    }
}
