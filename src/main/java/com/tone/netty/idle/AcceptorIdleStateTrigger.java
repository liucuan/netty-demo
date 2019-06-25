package com.tone.netty.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by zhaoxiang.liu on 2017/4/17.
 */
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(ctx instanceof IdleStateEvent) {
            IdleState idleState = ((IdleStateEvent) evt).state();
            if(idleState == IdleState.READER_IDLE) {
                throw new RuntimeException("idle exception");
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
