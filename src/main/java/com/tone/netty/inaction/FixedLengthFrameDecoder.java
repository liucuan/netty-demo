package com.tone.netty.inaction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by zhaoxiang.liu on 2017/7/3.
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if(frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer:" + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= frameLength) {
            ByteBuf buf = in.readBytes(frameLength);
            out.add(buf);
        }
    }
}
