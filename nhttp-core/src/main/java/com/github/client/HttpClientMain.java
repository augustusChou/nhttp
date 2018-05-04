package com.github.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

public class HttpClientMain {

    public static void main(String[] args) throws Exception {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 7803))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpClientCodec(), new HttpObjectAggregator(65536));
                        }
                    });
            Channel ch = bootstrap.connect().sync().channel();

            ByteBuf buf = ch.alloc().buffer();
            buf.writeInt(10);
            buf.writeInt(20);
            FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/count", buf);
            ch.writeAndFlush(fullHttpRequest);
            ch.close().sync();


        } catch (Exception ignore) {
        } finally {
            loopGroup.shutdownGracefully();
        }
    }
}
