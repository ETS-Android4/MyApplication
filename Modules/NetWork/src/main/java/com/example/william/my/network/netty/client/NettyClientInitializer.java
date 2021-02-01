package com.example.william.my.network.netty.client;

import com.example.william.my.network.netty.server.NettyServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast("handler", new NettyClientHandler());
    }
}
