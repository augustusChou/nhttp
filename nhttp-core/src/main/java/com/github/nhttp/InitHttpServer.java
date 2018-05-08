package com.github.nhttp;

import com.github.nhttp.core.HttpInitializer;
import com.github.nhttp.handler.HttpRequestHandler;
import com.github.nhttp.handler.SimpleCreateRequestHandler;
import com.github.nhttp.router.Router;
import com.github.nhttp.router.RouterHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InitHttpServer extends AbstractServer {
    private Logger LOG = LogManager.getLogger(InitHttpServer.class);

    public void init(HttpInitializer initializer) throws Exception {
        Map<String, String> confMap = initializer.getConfMap();
        int listenerPort = Integer.parseInt(confMap.getOrDefault("http.server.port", "7803"));

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            LOG.info("Http服务将于端口:{} 启动", listenerPort);
            ChannelFuture future = bootstrap.bind(listenerPort).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOG.info("Http服务即将关闭");
            try {
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
