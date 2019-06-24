package com.tone.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Map;

public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        System.out.println("status: " + msg.status());
        System.out.println("version: " + msg.protocolVersion());
        System.out.println();
        if (!msg.headers().isEmpty()) {
            for (Map.Entry<String, String> httpHeaders : msg.headers()) {
                System.out.println("header: " + httpHeaders.getKey() + " = " + httpHeaders.getValue());
            }
            System.out.println();
        }
        ByteBuf content = msg.content();
        if (content.isReadable()) {
            System.out.println("content {");
        }
    }
}
