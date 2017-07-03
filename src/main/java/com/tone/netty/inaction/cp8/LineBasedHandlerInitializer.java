package com.tone.netty.inaction.cp8;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 *
 * aa\r\nbc\r\n ----> aa\r\n  bc\r\n
 * 按行读取，根据换行符来拆分
 *
 * Created by echolau on 2017/6/25.
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LineBasedFrameDecoder(65*1024));
        pipeline.addLast(new FrameHandler());
    }

    private static class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            //每次调用都需要传递一个单帧的内容
        }
    }
}
