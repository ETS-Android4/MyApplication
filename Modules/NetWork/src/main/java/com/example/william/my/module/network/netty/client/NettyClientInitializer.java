package com.example.william.my.module.network.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                //.addLast("handler", new NettyClientHandler())
                //自定义长度帧解码器
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, -2, 2));
                //.addLast(new MessageBinaryDecoder())
                //.addLast(new MessageEncoder())
                //.addLast(new ChatClientHandler(mHandler));
    }
}
