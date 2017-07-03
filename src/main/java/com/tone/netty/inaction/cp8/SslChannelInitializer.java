package com.tone.netty.inaction.cp8;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by echolau on 2017/6/24.
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SSLContext context;
    private final boolean startTls;
    private final boolean client;

    public SslChannelInitializer(SSLContext context, boolean startTls, boolean client) {
        this.context = context;
        this.startTls = startTls;
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(client);
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
