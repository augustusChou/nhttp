package com.github.nhttp.handler;

import com.alibaba.fastjson.JSON;
import com.github.nhttp.InitHttpServer;
import com.github.nhttp.router.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SimpleCreateRequestHandler {
    private static Logger LOG = LogManager.getLogger(SimpleCreateRequestHandler.class);


    private SimpleCreateRequestHandler() {
    }

    public static HttpRequestHandler create(List<Router> routerList) {
        HttpRequestHandler requestHandler = HttpRequestHandler.create(parseConf());

        for (Router router : routerList) {
            requestHandler.addRouter(router);
        }


        return requestHandler;
    }

    /**
     * 解析资源目录下的应用配置文件
     *
     * @return map结构的配置
     */
    private static Map<String, String> parseConf() {
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
}
