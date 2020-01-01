package com.lyennon.remoting;

import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.channel.ChannelHandlerContext;

public interface NettyProcessor {

    RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request);
}
