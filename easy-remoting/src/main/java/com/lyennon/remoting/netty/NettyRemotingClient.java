package com.lyennon.remoting.netty;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.ClientRemotingService;
import com.lyennon.remoting.InvokeCallback;
import com.lyennon.remoting.Server;
import com.lyennon.remoting.model.RemotingTransporter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yong.liang
 */
public class NettyRemotingClient implements ClientRemotingService, Server {

    private DefaultEventLoopGroup defaultEventLoopGroup;

    public void init(){
        this.defaultEventLoopGroup = new DefaultEventLoopGroup(1,new NamedThreadFactory("NettyClientSelector"));
    }

    @Override
    public RemotingTransporter invokeSync(String addr, RemotingTransporter request,
        long timeoutMillis) {
        return null;
    }

    @Override
    public void invokeAsync(String addr, RemotingTransporter request, long timeoutMillis,
        InvokeCallback invokeCallback) {

    }

    @Override
    public void invokeOneway(String addr, RemotingTransporter request, long timeoutMillis) {

    }

    @Override
    public RemotingTransporter invokeSync(Channel channel, RemotingTransporter request,
        long timeoutMillis) {
        return null;
    }

    @Override
    public void invokeAsync(Channel channel, RemotingTransporter request, long timeoutMillis,
        InvokeCallback invokeCallback) {

    }

    @Override
    public void invokeOneway(Channel channel, RemotingTransporter request, long timeoutMillis) {

    }

    @Override
    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(defaultEventLoopGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .option(ChannelOption.SO_KEEPALIVE,false)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
            .option(ChannelOption.SO_SNDBUF,65535)
            .option(ChannelOption.SO_RCVBUF,65535)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new RemotingTransporterEncoder(),
                        new RemotingTransporterDecoder(),
                        new NettyClientHandler());
                }
            });

    }

    @Override
    public void stop() {

    }

    @Override
    public void isRuning() {

    }
}
