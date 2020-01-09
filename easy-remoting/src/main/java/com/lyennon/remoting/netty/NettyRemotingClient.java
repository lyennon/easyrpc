package com.lyennon.remoting.netty;

import com.lyennon.common.thread.NamedThreadFactory;
import com.lyennon.remoting.ClientRemotingService;
import com.lyennon.remoting.InvokeCallback;
import com.lyennon.remoting.Server;
import com.lyennon.remoting.common.RemotingUtils;
import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.model.ResponseFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yong.liang
 */
public class NettyRemotingClient implements ClientRemotingService, Server {

    private EventLoopGroup defaultEventLoopGroup;

    private DefaultNettyProcessor nettyProcessor;

    private Bootstrap bootstrap;

    public void init() {
        this.defaultEventLoopGroup = new NioEventLoopGroup(1, new NamedThreadFactory("NettyClientSelector"));
        this.nettyProcessor = new DefaultNettyProcessor();
    }

    @Override
    public RemotingTransporter invokeSync(String addr, RemotingTransporter request,
                                          long timeoutMillis) throws InterruptedException {
        ChannelFuture channelFuture = this.bootstrap.connect(RemotingUtils.string2SocketAddress(addr));
        channelFuture.awaitUninterruptibly(10000);
        if(channelFuture.channel() != null && channelFuture.channel().isActive()){
            return this.invokeSync(channelFuture.channel(), request, timeoutMillis);
        }else {
            throw new RuntimeException("invoke syanc failure");
        }
    }

    @Override
    public void invokeAsync(String addr, RemotingTransporter request, long timeoutMillis,
                            InvokeCallback invokeCallback) {
        ChannelFuture channel = this.bootstrap.connect(RemotingUtils.string2SocketAddress(addr));
        this.invokeAsync(channel.channel(), request, timeoutMillis, invokeCallback);
    }

    @Override
    public void invokeOneway(String addr, RemotingTransporter request, long timeoutMillis) {
        ChannelFuture channel = this.bootstrap.connect(RemotingUtils.string2SocketAddress(addr));
        this.invokeOneway(channel.channel(), request, timeoutMillis);

    }

    @Override
    public RemotingTransporter invokeSync(final Channel channel, RemotingTransporter request,
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
        return responseCommand;
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
        this.bootstrap = new Bootstrap();
        bootstrap.group(defaultEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RemotingTransporterEncoder(),
                                new RemotingTransporterDecoder(),
                                new NettyClientHandler(nettyProcessor));
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
