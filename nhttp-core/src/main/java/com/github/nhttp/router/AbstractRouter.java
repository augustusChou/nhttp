package com.github.nhttp.router;

import com.github.nhttp.core.SimpleHttpRequest;
import com.github.nhttp.core.SimpleHttpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

public abstract class AbstractRouter {
    protected RouterHandler handler;
    protected ExceptionHandler exceptionHandler;

    abstract public Router method(HttpMethod httpMethod);

    abstract public HttpMethod getMethod();

    abstract public String getRouterPath();

    abstract public Router addHandler(RouterHandler handler);

    abstract public SimpleHttpResponse execHandler(SimpleHttpRequest request);

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
