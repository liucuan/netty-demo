package com.tone.netty.inaction.codec.json;

import com.tone.netty.inaction.cp2.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jenny on 2017/3/13.
 */
public class JacksonClient {
    private final String host;

    private final int port;

    public JacksonClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup buGroup = new NioEventLoopGroup(16);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new JacksonDecoder<>(JacksonBean.class))
                                    .addLast(new JacksonEncoder()).addLast(new JacksonClinetHandler())
                                    .addLast(buGroup,new BuHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();// 连到远端，一直等待直到连接完成
            f.channel().closeFuture().sync();// 一直阻塞到Channel关闭
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new JacksonClient("127.0.0.1", 8888).start();
    }
}
