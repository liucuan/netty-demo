package com.tone.netty.keepalive2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by echolau on 2017/6/23.
 */
public class ServerHandler extends CustomHeartbeatHandler {
    public ServerHandler() {
        super("server");
    }

    @Override
    protected void handleData(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] data = new byte[msg.readableBytes() - 5];
        ByteBuf buf = Unpooled.copiedBuffer(msg);
        buf.skipBytes(5);
        buf.readBytes(data);
        String content = new String(data);
        System.out.println(name + " get content:" + content);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.write(buf);
    }

    @Override
    protected void handleReadIdle(ChannelHandlerContext ctx) {
        super.handleReadIdle(ctx);
        System.err.println("--client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it ---");
        ctx.close();
    }
}
