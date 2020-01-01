package com.lyennon.remoting.netty;

import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yong.liang
 */
public class ClientRemotingProcessor implements NettyProcessor {

    @Override
    public RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request) {
        return null;
    }
}
