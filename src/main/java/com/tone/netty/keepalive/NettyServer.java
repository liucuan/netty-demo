package com.tone.netty.keepalive;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by jenny on 2017/3/9.
 */
public class NettyServer {
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Netty time Server started at port " + port);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(final SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new RpcDecoder(NettyMessage.class)).addLast(new RpcEncoder(NettyMessage.class))
                    .addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS))
                    .addLast(new HeartBeatRespHandler());
        }

    }

    public static void main(String[] args) {
        try {
            new NettyServer().bind(12000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
