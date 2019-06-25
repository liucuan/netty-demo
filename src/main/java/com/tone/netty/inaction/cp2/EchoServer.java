package com.tone.netty.inaction.cp2;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jenny on 2017/3/13.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8888).start();
    }

    private void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group, cGroup).channel(NioServerSocketChannel.class)// 指定Nio传输CHannel
                    .localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);// EchoServerHandler是@Shareable所以可以一直用同一个实例
                        }
                    });
            ChannelFuture f = b.bind().sync();// 异步绑定服务器，sync()一直等待到绑定完成
            f.channel().closeFuture().sync();// 获得这个Channel的CloseFuture，阻塞当前线程直到关闭操作
        }finally {
            group.shutdownGracefully().sync();// 关闭EventLoopGroup，释放所有资源
        }
    }

    /**
     * 当你分享一个 EventLoop ，你保证所有 Channel 分配给 EventLoop 将使用相同的线程,消除上下文切换和相关的开销。(请记住,一个EventLoop分配给一个线程执行操作。)
     */
    private void cc() {
        ServerBootstrap bootstrap = new ServerBootstrap(); // 1
        bootstrap.group(new NioEventLoopGroup(), // 2
                new NioEventLoopGroup()).channel(NioServerSocketChannel.class) // 3
                .childHandler( // 4
                        new SimpleChannelInboundHandler<ByteBuf>(){
                            ChannelFuture connectFuture;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                Bootstrap bootstrap = new Bootstrap();// 5
                                bootstrap.channel(NioSocketChannel.class) // 6
                                        .handler(new SimpleChannelInboundHandler<ByteBuf>(){ // 7
                                                    @Override
                                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in)
                                                            throws Exception {
                                                        System.out.println("Reveived data");
                                                    }
                                                });
                                bootstrap.group(ctx.channel().eventLoop()); // 8
                                connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80)); // 9
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
                                    throws Exception {
                                if(connectFuture.isDone()) {
                                    // do something with the data //10
                                }
                            }
                        });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080)); // 11
        future.addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                }else {
                    System.err.println("Bound attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
