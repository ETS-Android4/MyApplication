package com.example.william.my.module.network.netty.client;

import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    private final String TAG = getClass().getSimpleName();

    /**
     * 观察接收到的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Log.e(TAG, msg);
        //ctx.channel().writeAndFlush("[Client]: " + msg + " \n");
    }
}