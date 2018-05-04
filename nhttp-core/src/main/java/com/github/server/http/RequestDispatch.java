package com.github.server.http;

import io.netty.handler.codec.http.FullHttpRequest;

public class RequestDispatch {

    private FullHttpRequest request;
    private String mark;


    public RequestDispatch(FullHttpRequest request) {
        this.request = request;
        this.mark = request.method().name() + ":" + request.uri();
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public String getMark() {
        return mark;
    }
}
