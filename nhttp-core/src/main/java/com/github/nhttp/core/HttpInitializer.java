package com.github.nhttp.core;

import com.github.nhttp.handler.HttpRequestHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.Map;

public class HttpInitializer extends ChannelInitializer<Channel> {

    private HttpRequestHandler requestHandler;

    public HttpInitializer(HttpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public Map<String, String> getConfMap() {
        return this.requestHandler.getConfMap();
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        cp.addLast("codec", new HttpServerCodec());
        cp.addLast("aggregator", new HttpObjectAggregator(65536));
        cp.addLast("request", this.requestHandler);
    }


}
