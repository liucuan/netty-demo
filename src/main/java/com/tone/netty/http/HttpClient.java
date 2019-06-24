package com.tone.netty.http;

import com.tone.netty.inaction.cp2.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class HttpClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("www.baidu.com", 80))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new HttpRequestEncoder())
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new SimpleChannelInboundHandler<ByteBuffer>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) throws Exception {
                                 if (msg instanceof FullHttpResponse) {
                                     FullHttpResponse response = (FullHttpResponse)msg;
                                     System.out.println(response.content().toString());

                                 }
                                }
                            });
                        }
                    });
            ChannelFuture f = b.connect().sync();//连到远端，一直等待直到连接完成
            f.channel().closeFuture().sync();//一直阻塞到Channel关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
