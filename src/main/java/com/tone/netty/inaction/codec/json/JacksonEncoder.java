package com.tone.netty.inaction.codec.json;

import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zhaoxiang.liu on 2017/4/14.
 */
public class JacksonEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ObjectMapper om = JacksonMapper.getInstance();
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        om.writeValue((OutputStream) byteBufOutputStream, msg);
    }
}
