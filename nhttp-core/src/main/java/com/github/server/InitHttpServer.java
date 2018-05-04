package com.github.server;

import com.github.server.http.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class InitHttpServer extends AbstractServer {
    private Logger LOG = LogManager.getLogger(InitHttpServer.class);


    public static void main(String[] args) throws Exception {
        new InitHttpServer().init();
    }


    public void init() throws Exception {
        Map<String, String> confMap = parseConf();
        int listenerPort = Integer.parseInt(confMap.getOrDefault("http.server.port", "7803"));


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInitChannelHandler(confMap))
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


    private class HttpInitChannelHandler extends ChannelInitializer<Channel> {
        /**
         * application.properties 配置
         */
        private Map<String, String> confMap;

        public HttpInitChannelHandler(Map<String, String> confMap) {
            this.confMap = confMap;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline cp = ch.pipeline();
            cp.addLast("codec", new HttpServerCodec());
            cp.addLast("aggregator", new HttpObjectAggregator(65536));
            cp.addLast("request", new HttpRequestHandler(this.confMap));
        }
    }


}
