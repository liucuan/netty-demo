package com.tone.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaderValues;
import io.netty.handler.codec.rtsp.RtspHeaders;

import static com.sun.deploy.net.HttpRequest.CONTENT_LENGTH;
import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.CONNECTION;

/**
 * Created by zhaoxiang.liu on 2017/4/26.
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            String uri = request.uri();
            System.out.println("Uri:" + uri);
        } else if(msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
            String res = "I am OK";
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(res
                    .getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            if(HTTP_1_1.isKeepAliveDefault()) {
                response.headers().set(CONNECTION, RtspHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
