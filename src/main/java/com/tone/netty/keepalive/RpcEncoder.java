package com.tone.netty.keepalive;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by jenny on 2017/3/9.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            System.out.println("发送的请求是：" + in);
            byte[] data = SerializationUtil.serializer(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
