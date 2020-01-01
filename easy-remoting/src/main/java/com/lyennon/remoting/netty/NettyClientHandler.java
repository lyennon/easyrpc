package com.lyennon.remoting.netty;

import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yong.liang
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RemotingTransporter> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingTransporter msg) throws Exception {

    }
}
