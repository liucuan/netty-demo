package com.tone.netty.inaction.codec.json;

import java.io.InputStream;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by zhaoxiang.liu on 2017/4/14.
 */
public class JacksonDecoder<T> extends ByteToMessageDecoder {
    private final Class<T> clazz;

    /**
     *
     */
    public JacksonDecoder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(in);
        out.add(JacksonMapper.getInstance().readValue((InputStream) byteBufInputStream, clazz));
    }
}
