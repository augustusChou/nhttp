package com.github.nhttp.example;

import com.alibaba.fastjson.JSONObject;
import com.github.nhttp.InitHttpServer;
import com.github.nhttp.core.HttpContentType;
import com.github.nhttp.core.HttpInitializer;
import com.github.nhttp.core.SimpleHttpRequest;
import com.github.nhttp.core.SimpleHttpResponse;
import com.github.nhttp.handler.HttpRequestHandler;
import com.github.nhttp.handler.SimpleCreateRequestHandler;
import com.github.nhttp.router.ExceptionHandler;
import com.github.nhttp.router.Router;
import com.github.nhttp.router.RouterHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Example {


    public static void main(String[] args) throws Exception {
        //get 返回数据
        Router router = Router.router("/").method(HttpMethod.GET).addHandler(new RouterHandler() {
            @Override
            public SimpleHttpResponse handler(SimpleHttpRequest request) {
                return new SimpleHttpResponse(Unpooled.copiedBuffer("测试", CharsetUtil.UTF_8));
            }
        });

        //post 解析json返回json
        Router count = Router.router("/count").method(HttpMethod.POST).addHandler(new RouterHandler() {
            @Override
            public SimpleHttpResponse handler(SimpleHttpRequest request) {
                JSONObject jsonObject = request.getJson();
                Integer a = jsonObject.getInteger("a");
                Integer b = jsonObject.getInteger("b");
                JSONObject result = new JSONObject();
                result.put("result", a * b);
                return new SimpleHttpResponse(HttpContentType.JSON, Unpooled.copiedBuffer(result.toString(), CharsetUtil.UTF_8));
            }
        });

        //解析表单数据(没有完全实现，因为我不用这个。。)
        Router add = Router.router("/add").method(HttpMethod.POST).addHandler(new RouterHandler() {
            @Override
            public SimpleHttpResponse handler(SimpleHttpRequest request) {
                Map<String, Object> paramMap = request.getParam();
                Integer c = Integer.valueOf(paramMap.get("a").toString()) + Integer.valueOf(paramMap.get("b").toString());
                return new SimpleHttpResponse(Unpooled.copiedBuffer(c.toString(), CharsetUtil.UTF_8));
            }
        });


        //解析uri查询字符串
        Router sub = Router.router("/sub").method(HttpMethod.POST).addHandler(new RouterHandler() {
            @Override
            public SimpleHttpResponse handler(SimpleHttpRequest request) {
                Map<String, String> paramMap = request.getQueryString();
                Integer c = Integer.valueOf(paramMap.get("a")) + Integer.valueOf(paramMap.get("b"));
                return new SimpleHttpResponse(Unpooled.copiedBuffer(c.toString(), CharsetUtil.UTF_8));
            }
        });
        //设置自定义异常处理
        sub.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void exceptionHandler(ChannelHandlerContext ctx, Throwable cause) {
                JSONObject result = new JSONObject();
                result.put("result", "-1");
                result.put("desc", cause.getMessage() == null ? "系统错误" : cause.getMessage());
                SimpleHttpResponse response = new SimpleHttpResponse(HttpContentType.JSON, Unpooled.copiedBuffer(result.toString(), CharsetUtil.UTF_8));
                ctx.writeAndFlush(response.getResponse()).addListener(ChannelFutureListener.CLOSE);
            }
        });


        List<Router> routers = new ArrayList<>();
        routers.add(router);
        routers.add(count);
        routers.add(add);
        routers.add(sub);


        HttpRequestHandler requestHandler = SimpleCreateRequestHandler.create(routers);
        HttpInitializer initializer = new HttpInitializer(requestHandler);
        new InitHttpServer().init(initializer);
    }

}
