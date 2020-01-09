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
import com.lyennon.remoting.model.ResponseFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
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

    private DefaultNettyProcessor nettyProcessor;

    public void init(){
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(1, new NamedThreadFactory("NettyServerCodecThread"));
        this.nettyProcessor = new DefaultNettyProcessor();
    }

    @Override
    public void start() {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(10, new NamedThreadFactory("NIO_EVENT_LOOP_GROUP"));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
//                .localAddress(new InetSocketAddress(serverConfig.getIp(),serverConfig.getPort()))
                .localAddress(new InetSocketAddress("127.0.0.1",9090))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
//                .option(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 65535)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defaultEventExecutorGroup,
                                new RemotingTransporterDecoder(),
                                new RemotingTransporterEncoder(),
                                new NettyServerHandler(nettyProcessor));
                    }
                });
        try {
            ChannelFuture sync = serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            int port = addr.getPort();
            System.out.println(port);
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
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
        this.nettyProcessor.registerProcessor(code, nettyProcessor);
    }

    @Override
    public RemotingTransporter invokeSync(Channel channel, RemotingTransporter request,
        long timeoutMillis) throws InterruptedException {
        final long opaque = request.getOpaque();
        final ResponseFuture responseFuture = new ResponseFuture(channel, opaque, timeoutMillis, null);
        nettyProcessor.registerResponseProcessor(opaque, new DefaultResponseNettyProcessor(responseFuture));
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    responseFuture.setSendRequestOK(true);
                    return;
                }
                responseFuture.setSendRequestOK(false);
                responseFuture.setCause(channelFuture.cause());
                responseFuture.putResponse(null);
            }
        });
        RemotingTransporter responseCommand = responseFuture.waitResponse(timeoutMillis);
        if (null == responseCommand) {
            if (responseFuture.isSendRequestOK()) {
                throw new RuntimeException("time out");
            } else {
                throw new RuntimeException("send request failure");
            }
        }
        return responseCommand;    }

    @Override
    public void invokeAsync(Channel channel, RemotingTransporter request, long timeoutMillis,
        InvokeCallback invokeCallback) {
        final long opaque = request.getOpaque();
        final ResponseFuture responseFuture = new ResponseFuture(channel, opaque, timeoutMillis, invokeCallback);
        this.nettyProcessor.registerResponseProcessor(opaque,new DefaultResponseNettyProcessor(responseFuture));
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    responseFuture.setSendRequestOK(true);
                    return;
                }

                nettyProcessor.unRegisterResponseProcessor(opaque);
                requestFail(responseFuture);
            }
        });
    }

    @Override
    public void invokeOneway(Channel channel, RemotingTransporter request, long timeoutMillis) {

    }

    private void requestFail(ResponseFuture responseFuture) {
        if (responseFuture != null) {
            responseFuture.setSendRequestOK(false);
            responseFuture.putResponse(null);
            responseFuture.executeInvokeCallback();
        }
    }
}
