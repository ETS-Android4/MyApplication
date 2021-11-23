package com.example.william.my.module.network.netty.client;

import com.example.william.my.module.utils.L;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private final String TAG = this.getClass().getSimpleName();

    private static NettyClient instance;

    public static NettyClient getInstance() {
        if (instance == null) {
            synchronized (NettyClient.class) {
                if (instance == null) {
                    instance = new NettyClient();
                }
            }
        }
        return instance;
    }

    private Channel channel;

    private NettyClient() {
    }

    /**
     * 连接服务端
     */
    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new NettyClientInitializer());

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            channel = f.channel();
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public boolean sendMessage(String msg) {
        if (channel == null) {
            L.e(TAG, "聊天通道为空");
            return false;
        }
        if (channel.isActive() && channel.isWritable()) {
            channel.writeAndFlush(msg + "\n");
            return true;
        } else if (!channel.isActive()) {
            L.e(TAG, "聊天通道未连接");
        } else if (!channel.isWritable()) {
            L.e(TAG, "聊天通道连接，但不可写");
        }
        return false;
    }

    public String getAddress() {
        if (channel != null) {
            return channel.remoteAddress().toString();
        } else {
            return "";
        }
    }

    public void disconnect() {
        try {
            if (channel != null) {
                channel.disconnect();
                channel.close();
                channel = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
