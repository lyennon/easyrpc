package com.lyennon;

import com.lyennon.remoting.NettyProcessor;
import com.lyennon.remoting.model.RemotingTransporter;
import com.lyennon.remoting.netty.NettyRemotingClient;
import com.lyennon.remoting.netty.NettyRemotingServer;
import io.netty.channel.ChannelHandlerContext;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        NettyRemotingServer nettyRemotingServer = new NettyRemotingServer();
        nettyRemotingServer.init();
        nettyRemotingServer.start();
        nettyRemotingServer.registerProcessor(100, new NettyProcessor() {
            @Override
            public RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request) {
                RemotingTransporter responseTransporter = RemotingTransporter.createResponseTransporter(request.getCode(), null, request.getOpaque());
                responseTransporter.setBytes(new String("hello world,this is 100").getBytes());
                ctx.writeAndFlush(responseTransporter);
                return responseTransporter;
            }
        });
        nettyRemotingServer.registerProcessor(101, new NettyProcessor() {
            @Override
            public RemotingTransporter process(ChannelHandlerContext ctx, RemotingTransporter request) {
                RemotingTransporter responseTransporter = RemotingTransporter.createResponseTransporter(request.getCode(), null, request.getOpaque());
                responseTransporter.setBytes(new String("hello world,this is 101").getBytes());
                ctx.writeAndFlush(responseTransporter);
                return responseTransporter;
            }
        });


        NettyRemotingClient nettyRemotingClient = new NettyRemotingClient();
        nettyRemotingClient.init();
        nettyRemotingClient.start();

        RemotingTransporter requestTransporter1 = RemotingTransporter.createRequestTransporter((byte) 100, null);
        RemotingTransporter requestTransporter2 = RemotingTransporter.createRequestTransporter((byte) 101, null);
        requestTransporter1.setBytes(new String("123").getBytes());
        requestTransporter2.setBytes(new String("123").getBytes());
        RemotingTransporter remotingTransporter1 = nettyRemotingClient.invokeSync("127.0.0.1:9090", requestTransporter1, 10000);
        byte[] bytes1 = remotingTransporter1.getBytes();
        System.out.println(new String(bytes1));
        RemotingTransporter remotingTransporter2 = nettyRemotingClient.invokeSync("127.0.0.1:9090", requestTransporter2, 10000);
        byte[] bytes2 = remotingTransporter2.getBytes();
        System.out.println(new String(bytes2));
    }
}
