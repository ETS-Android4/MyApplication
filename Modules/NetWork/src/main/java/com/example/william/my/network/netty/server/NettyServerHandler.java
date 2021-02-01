package com.example.william.my.network.netty.server;

import android.util.Log;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = "ServerHandler";

    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Log.e(TAG, channel.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        Log.e(TAG, channel.remoteAddress() + "离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel channel = ctx.channel();
        Log.e(TAG, channel.remoteAddress() + "异常");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Channel channel = ctx.channel();
        Log.e(TAG, channel.remoteAddress() + "加入");

        channels.writeAndFlush("[Server]: " + channel.remoteAddress() + " 加入\n");
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        Channel channel = ctx.channel();
        Log.e(TAG, channel.remoteAddress() + "离开");

        channels.writeAndFlush("[Server]: " + channel.remoteAddress() + " 离开\n");
        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
        channels.remove(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        Channel inComing = ctx.channel();
        for (Channel channel : channels) {
            if (channel != inComing) {
                channel.writeAndFlush("[" + inComing.remoteAddress() + "]:  " + msg + "\n");
            } else {
                channel.writeAndFlush("[localhost]:  " + msg + "\n");
            }
        }
        Log.e(TAG, msg.toString());
    }
}
