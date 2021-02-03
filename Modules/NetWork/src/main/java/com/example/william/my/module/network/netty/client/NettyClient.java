package com.example.william.my.module.network.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

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
            b.option(ChannelOption.SO_KEEPALIVE, true);//如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
            b.option(ChannelOption.SO_BACKLOG, 10000);//指定队列的大小
            b.option(ChannelOption.SO_REUSEADDR, true);//允许共用端口
            b.option(ChannelOption.TCP_NODELAY, true);//小数据即时传输
            b.option(ChannelOption.SO_LINGER, 0);//阻塞时间，直到数据完全发送
            b.option(ChannelOption.SO_SNDBUF, 8192);//发送缓冲区
            b.option(ChannelOption.SO_RCVBUF, 8192);//接收缓冲区
            b.option(ChannelOption.SO_TIMEOUT, 5000);//等待超时时间
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000);//链接超时时间
            b.handler(new NettyClientHandler());

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
