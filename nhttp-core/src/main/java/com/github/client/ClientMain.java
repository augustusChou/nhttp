package com.github.client;

import com.github.codec.MessageEncoder;
import com.github.codec.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[100];

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                EventLoopGroup loopGroup = new NioEventLoopGroup();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(loopGroup)
                            .channel(NioSocketChannel.class)
                            .remoteAddress(new InetSocketAddress("localhost", 7802))
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new MessageEncoder());
                                }
                            });
                    Channel ch = bootstrap.connect().sync().channel();
                    ch.writeAndFlush(new Message("测试信息" + finalI));
                    ch.close().sync();
                } catch (Exception ignore) {
                } finally {
                    try {
                        loopGroup.shutdownGracefully().sync();
                    } catch (Exception ignore) {
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(10000);


    }
}
