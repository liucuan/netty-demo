package com.tone.netty.inaction.codec.json;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoxiang.liu on 2017/4/14.
 */
public class JacksonClinetHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送对象
        JacksonBean user = new JacksonBean();
        user.setAge(27);
        user.setName("waylau");
        List<String> sons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sons.add("Lucy" + i);
            sons.add("Lily" + i);
        }
        user.setSons(sons);
        Map<String, String> addrs = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            addrs.put("001" + i, "18998366112");
            addrs.put("002" + i, "15014965012");
        }
        user.setAddrs(addrs);
        ctx.writeAndFlush(user);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String js;
        if(msg instanceof JacksonBean) {
            System.out.println("is JacksonBean");
        }
        // 收到消息处理
        System.out.println("Client received: " + JacksonMapper.getInstance().writeValueAsString(msg));
        ctx.fireChannelRead(msg);
    }
}
