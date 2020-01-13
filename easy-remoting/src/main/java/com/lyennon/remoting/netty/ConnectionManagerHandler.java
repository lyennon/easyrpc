package com.lyennon.remoting.netty;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class ConnectionManagerHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state().equals(IdleState.ALL_IDLE)){
                SocketAddress remoteAddress = ctx.channel().remoteAddress();
                ctx.channel().close().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("close channle,remoteAddress: " + remoteAddress.toString() + "isSuccess: " + future.isSuccess());
                    }
                });
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
