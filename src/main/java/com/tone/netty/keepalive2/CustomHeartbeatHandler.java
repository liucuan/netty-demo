package com.tone.netty.keepalive2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by echolau on 2017/6/23.
 */
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final byte PING_MSG = 1;
    public static final byte PONG_MSG = 2;
    public static final byte CUSTOM_MSG = 3;
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.getByte(4) == PING_MSG) {
            sendPongMsg(ctx);
        } else if (msg.getByte(4) == PONG_MSG) {
            System.out.println(name + " get pong msg from " + ctx.channel().remoteAddress());
        } else {
            handleData(ctx, msg);
        }
    }

    protected void sendPingMsg(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        ctx.writeAndFlush(buf);
        heartbeatCount++;
        System.out.println(name + " sent ping msg to " + ctx.channel().remoteAddress() + ", count:" + heartbeatCount);
    }

    protected abstract void handleData(ChannelHandlerContext ctx, ByteBuf msg);

    protected void sendPongMsg(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        ctx.channel().writeAndFlush(buf);
        heartbeatCount++;
        System.out.println(name + " sent pong msg to " + ctx.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //IdleStateHandler 产生的 IdleStateEvent的处理逻辑
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    handleReadIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.out.println("----all_Idle---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.out.println("----write_Idle---");
    }

    protected void handleReadIdle(ChannelHandlerContext ctx) {
        System.out.println("----Reader_Idle---");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("----" + ctx.channel().remoteAddress() + " is active---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is inactive---");

    }

}
