package com.tone.netty.inaction.cp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * channelActive()—和服务器的连接建立起来后被调用 channelRead0()—从服务器收到一条消息时被调用 exceptionCaught()—处理过程中异常发生时被调用
 * <p>
 * Created by jenny on 2017/3/13.
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    // 服务器的连接被建立后调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当受到链接成功的通知，发送1条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer(Thread.currentThread().getName() + " Netty rocks!", CharsetUtil.UTF_8));
    }

    // 数据后从服务器接收到调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 收到消息处理
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    // 捕获一个异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
