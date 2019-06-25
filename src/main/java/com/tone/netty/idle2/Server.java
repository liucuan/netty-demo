package com.tone.netty.idle2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.FileInputStream;
import java.text.ParseException;

/**
 * Created by zhaoxiang.liu on 2017/4/18.
 */
public class Server {
    public static void main(String[] args) throws ParseException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 3))
                                    .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0))
                                    .addLast(new ServerHandler());
                        }
                    });
            Channel ch = serverBootstrap.bind(12345).sync().channel();
            ch.closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
