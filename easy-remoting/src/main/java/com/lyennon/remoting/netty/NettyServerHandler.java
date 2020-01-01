package com.lyennon.remoting.netty;

import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<RemotingTransporter> {

    private NettyProcessor nettyProcessor;

    public NettyServerHandler(NettyProcessor nettyProcessor) {
        this.nettyProcessor = nettyProcessor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingTransporter msg) throws Exception {
            nettyProcessor.process(ctx,msg);
    }
}
