package com.tone.netty.inaction.cp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * ChannelHandlers被不同类型的events调用
 * 应用程序通过实现或者扩展ChannelHandlers来钩挂到event的生命周期，并且提供定制的应用逻辑
 * 在结构上，ChannelHandlers解耦你的业务逻辑和网络代码。这会简化开发过程，因为代码会随着需求的变化而变化。
 * Created by jenny on 2017/3/13.
 */
@ChannelHandler.Sharable//表明ChannelHandler可以被多个Channel安全共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in);//将收到的消息写入发送，不刷新输入消息
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //刷新挂起的数据到远端然后关闭Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
    //每个Channel有一个对应的ChannelPipeline，这个ChannelPipeline有一串ChannelHandler实例。
    // 默认情况下，一个handler会传递某个handler方法的调用到下一个handler。
    // 所以，如果在这个传递链中exceptionCaught()没有实现，异常会一直走到ChannelPipeline的终点，
    // 然后被载入日志。因为这个原因，你的应用应该提供至少一个实现了exceptionCaught()的handler。
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();//关闭这个Channel
    }
}
