package com.tone.netty.keepalive;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by jenny on 2017/3/9.
 */
public class NettyClient {
    public void connect(String remoteServer, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class).remoteAddress(remoteServer, port)
                    .handler(new ChildChannelHandler());

            ChannelFuture f = b.connect();
            System.out.println("Netty time Client connected at port " + port);

            f.channel().closeFuture().sync();
        } finally {
            try {
                TimeUnit.SECONDS.sleep(5);
                try {
                    System.out.println("重新链接。。。");
                    connect(remoteServer, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(final SocketChannel ch) throws Exception {
            // -8表示lengthAdjustment，让解码器从0开始截取字节，并且包含消息头
            long c = System.currentTimeMillis();
            ch.pipeline().addLast(new RpcEncoder(NettyMessage.class)).addLast(new RpcDecoder(NettyMessage.class))
                    .addLast(new IdleStateHandler(120, 10, 0, TimeUnit.SECONDS)).addLast(new HeartBeatReqHandler());
        }

    }

    public static void main(String[] args) {
        try {
            new NettyClient().connect("127.0.0.1", 12000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
