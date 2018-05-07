package com.github.nhttp;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

abstract class AbstractServer {
    private static Logger LOG = LogManager.getLogger(AbstractServer.class);


    /**
     * 解析资源目录下的应用配置文件
     *
     * @return map结构的配置
     */
    static Map<String, String> parseConf() {
        Map<String, String> confMap = new HashMap<>();

        Properties props = new Properties();
        InputStream in = InitHttpServer.class.getResourceAsStream("/application.properties");
        try {
            props.load(in);
            in.close();
        } catch (IOException ioE) {
            LOG.error("无法读取到配置文件", ioE);
        } catch (Exception e) {
            LOG.error(e);
        }

        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String k = String.valueOf(en.nextElement());
            confMap.put(k, props.getProperty(k));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(JSON.toJSONString(confMap));
        }

        return confMap;
    }


    static class HeartbeatHandler extends ChannelInboundHandlerAdapter {
        private final ByteBuf HEARTBEAT = Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8);

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }


}
