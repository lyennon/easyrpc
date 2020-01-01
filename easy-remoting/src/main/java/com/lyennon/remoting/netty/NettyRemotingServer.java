package com.lyennon.remoting.netty;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.InvokeCallback;
import com.lyennon.remoting.RemotingService;
import com.lyennon.remoting.RequestProcessorRegistry;
import com.lyennon.remoting.Server;
import com.lyennon.remoting.common.RemotingUtils;
import com.lyennon.remoting.model.NettyServerConfig;
import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;

public class NettyRemotingServer implements Server, RequestProcessorRegistry, RemotingService {

    private NettyServerConfig serverConfig;

    private EventExecutorGroup defaultEventExecutorGroup;

    private DefaultNettyProcessor defaultRequestProcessor;

    public void init(){
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(1, new NamedThreadFactory("NettyServerCodecThread"));
        this.defaultRequestProcessor = new DefaultNettyProcessor();
    }

    @Override
    public void start() {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(10, new NamedThreadFactory("NIO_EVENT_LOOP_GROUP"));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(serverConfig.getIp(),serverConfig.getPort()))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 65535)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defaultEventExecutorGroup,
                                new RemotingTransporterDecoder(),
                                new RemotingTransporterEncoder(),
                                new NettyServerHandler(defaultRequestProcessor));
                    }
                });
        try {
            ChannelFuture sync = serverBootstrap.bind().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e);
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void isRuning() {

    }

    private boolean useEpoll() {
        return RemotingUtils.isLinuxPlatform()
                && Epoll.isAvailable();
    }

    @Override
    public void registerProcessor(Integer code, NettyProcessor nettyProcessor) {
        this.defaultRequestProcessor.registerProcessor(code, nettyProcessor);
    }

    @Override public RemotingTransporter invokeSync(Channel channel, RemotingTransporter request,
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
}
