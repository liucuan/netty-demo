package com.tone.netty.idle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoxiang.liu on 2017/4/17.
 */
public class HeartBeatServer {
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    private int port;

    public HeartBeatServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.TRACE)).localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<ServerChannel>(){
                        @Override
                        protected void initChannel(ServerChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(idleStateTrigger);
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new HeartBeatServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("Server start listen at " + port);
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }else {
            port = 8080;
        }
        new HeartBeatServer(port).start();
    }
}
