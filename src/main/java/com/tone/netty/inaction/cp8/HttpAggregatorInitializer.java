package com.tone.netty.inaction.cp8;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * Created by echolau on 2017/6/24.
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            pipeline.addLast("decompress", new HttpContentDecompressor());//支持压缩，节省带宽
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            pipeline.addLast("compress", new HttpContentCompressor());
        }
        pipeline.addLast("aggeator", new HttpObjectAggregator(512 * 1024));//最大512kb
    }
}
