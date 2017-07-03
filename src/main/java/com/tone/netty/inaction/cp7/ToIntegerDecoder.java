package com.tone.netty.inaction.cp7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * read     ToIntDecoder   add
 * inbound ByteBuf ------->    decode()   --------> Integer --->ChannelInboundHandler
 * Created by echolau on 2017/6/24.
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {//int 4个字节长度
            out.add(in.readInt()); //读取int 添加到解码消息的list中
        }
    }
}
