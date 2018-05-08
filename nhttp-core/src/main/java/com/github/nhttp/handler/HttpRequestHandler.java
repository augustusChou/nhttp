package com.github.nhttp.handler;

import com.github.nhttp.core.SimpleHttpRequest;
import com.github.nhttp.core.SimpleHttpResponse;
import com.github.nhttp.core.UriUtil;
import com.github.nhttp.router.Router;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    /**
     * application.properties 配置
     */
    private Map<String, String> confMap;
    private Map<String, Router> routerMap;


    private HttpRequestHandler() {
    }

    public static HttpRequestHandler create(Map<String, String> confMap) {
        HttpRequestHandler requestHandler = new HttpRequestHandler();
        requestHandler.routerMap = new HashMap<>();
        requestHandler.confMap = confMap;
        return requestHandler;
    }

    public HttpRequestHandler addConf(String key, String value) {
        this.confMap.put(key, value);
        return this;
    }

    public HttpRequestHandler addConf(Map<String, String> confMap) {
        this.confMap.putAll(confMap);
        return this;
    }


    public HttpRequestHandler addRouter(Router router) {
        //加入路由map的时候将服务主路径加进去
        String serverPath = UriUtil.handlerPath(confMap.getOrDefault("server.path", ""));
        String uri = UriUtil.mergePath(serverPath, router.getRouterPath());
        this.routerMap.put(routerMapKey(uri, router.getMethod()), router);
        return this;
    }

    public Map<String, String> getConfMap() {
        return confMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        Router router = routerMap.get(routerMapKey(request.uri(), request.method()));
        if (router == null) {
            router = routerMap.get(UriUtil.handlerPath(request.uri()));
            if (router == null) {
                resultError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
        }
        try {
            SimpleHttpResponse response = router.execHandler(new SimpleHttpRequest(request));
            result(ctx, response.getResponse());
        } catch (Throwable cause) {
            if (router.getExceptionHandler() != null) {
                router.getExceptionHandler().exceptionHandler(ctx, cause);
            } else {
                throw cause;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        resultError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    private void resultError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        result(ctx, response);
    }

    private void result(ChannelHandlerContext ctx, FullHttpResponse response) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private String routerMapKey(String uri, HttpMethod httpMethod) {
        return UriUtil.handlerPath(uri) + ":" + (httpMethod == null ? "" : httpMethod.name());
    }

}
