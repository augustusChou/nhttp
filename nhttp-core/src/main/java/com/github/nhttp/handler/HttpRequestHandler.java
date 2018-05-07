package com.github.nhttp.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.Map;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    /**
     * application.properties 配置
     */
    private Map<String, String> confMap;


    public HttpRequestHandler(Map<String, String> confMap) {
        this.confMap = confMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request.decoderResult().isSuccess()) {
            resultError(ctx, request, HttpResponseStatus.BAD_REQUEST);
        }



    }


    private void resultError(ChannelHandlerContext ctx, FullHttpRequest request, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, request.headers().get(HttpHeaderNames.CONTENT_TYPE));
        result(ctx, response);
    }

    private void result(ChannelHandlerContext ctx, FullHttpResponse response) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
