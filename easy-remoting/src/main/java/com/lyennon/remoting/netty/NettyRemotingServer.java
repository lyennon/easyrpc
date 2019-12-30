package com.lyennon.remoting.netty;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.Server;
import com.lyennon.remoting.common.RemotingUtils;
import com.lyennon.remoting.model.NettyServerConfig;
import io.netty.bootstrap.ServerBootstrap;
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

public class NettyRemotingServer implements Server {

    private NettyServerConfig serverConfig;

    private EventExecutorGroup defaultEventExecutorGroup;

    @Override
    public void start() {
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(1, new NamedThreadFactory("NettyServerCodecThread"));

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
                                new RemotingTransporterEncoder());
                    }
                });
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
}
