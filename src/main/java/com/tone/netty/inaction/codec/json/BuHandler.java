package com.tone.netty.inaction.codec.json;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoxiang.liu on 2017/4/19.
 */
public class BuHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        TimeUnit.SECONDS.sleep(new Random().nextInt(6));
        System.out.println("bu ok.");
    }
}
