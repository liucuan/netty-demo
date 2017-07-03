package com.tone.netty.keepalive2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by echolau on 2017/6/23.
 */
public class ClientHandler extends CustomHeartbeatHandler {
    private ClientWithReConnect client;

    public ClientHandler(ClientWithReConnect client) {
        super("client");
        this.client = client;
    }

    @Override
    protected void handleData(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] data = new byte[msg.readableBytes() - 5];
        msg.skipBytes(5);
        msg.readBytes(data);
        String content = new String(data);
        System.out.println(name + " get content: " + content);
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }
}
