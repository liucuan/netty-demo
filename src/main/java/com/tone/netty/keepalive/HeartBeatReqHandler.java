package com.tone.netty.keepalive;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by jenny on 2017/3/9.
 */
public class HeartBeatReqHandler extends ChannelDuplexHandler {

    /**
     * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext,
     *      java.lang.Object)
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("read 空闲");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("write 空闲");
                ctx.writeAndFlush(buildHeartBeat(MessageType.HEARTBEAT_REQ.getType()));
            }
        }
    }

    /**
     *
     * @return
     * @author zhangwei<wei.zw@corp.netease.com>
     */
    private NettyMessage buildHeartBeat(byte type) {
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(type);
        msg.setHeader(header);
        return msg;
    }
}
