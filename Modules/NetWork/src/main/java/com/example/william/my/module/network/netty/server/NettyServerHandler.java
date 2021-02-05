package com.example.william.my.module.network.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "加入");
    }

    /**
     * 将会在连接被建立并且准备进行通信时被调用
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "在线");

        channels.add(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "离线");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "离开");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
        channels.remove(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "异常");

        // Close the connection when an exception is raised.
        cause.printStackTrace();
        channel.close();
    }

    /**
     * 观察接收到的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel inComing = ctx.channel();
        for (Channel channel : channels) {
            if (channel != inComing) {
                channel.writeAndFlush("[" + inComing.remoteAddress() + "]:  " + msg + "\n");
            } else {
                channel.writeAndFlush("[localhost]:  " + msg + "\n");
            }
        }
        System.out.println("Msg : " + msg);
    }
}
