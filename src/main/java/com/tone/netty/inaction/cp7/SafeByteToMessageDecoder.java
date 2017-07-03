package com.tone.netty.inaction.cp7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 设定最大帧，避免缓存太多数据耗尽内存
 * Created by echolau on 2017/6/24.
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    public static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readable = in.readableBytes();
        if (readable > MAX_FRAME_SIZE) {//检测缓冲区数据是否大于最大值
            in.skipBytes(readable);//忽略可读数据，并抛出异常来通知 ChannelPipeline 中的 ChannelHandler 这个帧数据超长
            throw new TooLongFrameException("Frame too big!");
        }
        //decode
    }
}
