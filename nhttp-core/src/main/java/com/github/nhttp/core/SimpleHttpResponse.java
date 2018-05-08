package com.github.nhttp.core;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

public class SimpleHttpResponse {

    private String contentType = HttpContentType.TEXT;
    private String acceptCharset = "utf-8";
    private ByteBuf responseData;

    public SimpleHttpResponse(ByteBuf responseData) {
        this.responseData = responseData;
    }

    public SimpleHttpResponse(String contentType, ByteBuf responseData) {
        this.contentType = contentType;
        this.responseData = responseData;
    }

    public SimpleHttpResponse(String contentType, String acceptCharset, ByteBuf responseData) {
        this.contentType = contentType;
        this.acceptCharset = acceptCharset;
        this.responseData = responseData;
    }


    public FullHttpResponse getResponse() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, responseData);
        response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, acceptCharset);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        return response;
    }
}
