package com.github.nhttp.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpRequest {
    private Logger LOG = LogManager.getLogger(SimpleHttpRequest.class);

    private FullHttpRequest request;
    private String strData;


    public SimpleHttpRequest(FullHttpRequest request) {
        this.request = request;
        this.strData = request.content().toString(CharsetUtil.UTF_8);
    }


    /**
     * @return 返回解析后的json对象
     */
    public JSONObject getJson() {
        return JSON.parseObject(this.strData);
    }


    /**
     * 获取表单数据  目前只解析了message body的数据  文件上传还没实现
     *
     * @return message body的数据
     */
    public Map<String, Object> getParam() {
        Map<String, Object> result = new HashMap<>();
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(), request, CharsetUtil.UTF_8);
        for (InterfaceHttpData interfaceHttpData : decoder.getBodyHttpDatas()) {
            switch (interfaceHttpData.getHttpDataType()) {
                case Attribute:
                    Attribute attribute = (Attribute) interfaceHttpData;
                    try {
                        result.put(attribute.getName(), attribute.getValue());
                    } catch (IOException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e);
                        }
                    }
                    break;
                case FileUpload:
                    // TODO: 2018/5/8
                    break;
                case InternalAttribute:
                    // TODO: 2018/5/8
                    break;
                default:

            }
        }
        return result;
    }

    public Object getParam(String name) {
        return getParam().get(name);
    }


    public Map<String, String> getQueryString() {
        Map<String, String> result = new HashMap<>();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(this.request.uri());
        Map<String, List<String>> uriAttributes = queryDecoder.parameters();
        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            result.put(attr.getKey(), attr.getValue().get(0));
        }
        return result;
    }


    public String getHeader(String name) {
        return request.headers().get(name);
    }

    public List<String> getAllHeader(String name) {
        return request.headers().getAll(name);
    }

    public String getContentType() {
        String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return contentType == null ? "" : contentType;
    }

}
