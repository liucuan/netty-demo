package com.tone.netty.inaction.cp7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * inbound ByteBuf ------->    decode()   --------> Integer --->ChannelInboundHandler
 * Created by echolau on 2017/6/24.
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt()); // 读取int 添加到解码消息的list中 不需要判断长度自动判断 比bytetomssage略慢
    }
}
