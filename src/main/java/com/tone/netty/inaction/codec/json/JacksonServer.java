package com.tone.netty.inaction.codec.json;

import com.tone.netty.inaction.cp2.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jenny on 2017/3/13.
 */
public class JacksonServer {

    private final int port;

    public JacksonServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new JacksonServer(8888).start();
    }

    private void start() throws Exception {
        final JacksonServerHandler serverHandler = new JacksonServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group, cGroup).channel(NioServerSocketChannel.class)// 指定Nio传输CHannel
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new JacksonDecoder<>(JacksonBean.class))
                                    .addLast(new JacksonEncoder())
                                    .addLast(
                                            serverHandler);// EchoServerHandler是@Shareable所以可以一直用同一个实例
                        }
                    });
            ChannelFuture f = b.bind().sync();// 异步绑定服务器，sync()一直等待到绑定完成
            f.channel().closeFuture().sync();// 获得这个Channel的CloseFuture，阻塞当前线程直到关闭操作
        } finally {
            group.shutdownGracefully().sync();// 关闭EventLoopGroup，释放所有资源
        }
    }
}
