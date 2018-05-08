package com.github.nhttp.router;

import com.github.nhttp.core.SimpleHttpRequest;
import com.github.nhttp.core.SimpleHttpResponse;
import com.github.nhttp.core.UriUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

public class Router extends AbstractRouter {


    private String path;
    private HttpMethod httpMethod;


    private Router() {
    }


    public static Router router() {
        return router("/");
    }

    public static Router router(String path) {
        Router router = new Router();
        router.path = UriUtil.handlerPath(path);
        return router;
    }

    @Override
    public Router method(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    @Override
    public HttpMethod getMethod() {
        return this.httpMethod;
    }

    @Override
    public String getRouterPath() {
        return path;
    }


    @Override
    public Router addHandler(RouterHandler handler) {
        super.handler = handler;
        return this;
    }

    @Override
    public SimpleHttpResponse execHandler(SimpleHttpRequest request) {
        if (super.handler == null) {
            throw new UnsupportedOperationException();
        }
        return super.handler.handler(request);
    }

}
