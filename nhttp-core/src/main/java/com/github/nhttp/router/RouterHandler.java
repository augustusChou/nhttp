package com.github.nhttp.router;

import com.github.nhttp.core.SimpleHttpRequest;
import com.github.nhttp.core.SimpleHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface RouterHandler {

    SimpleHttpResponse handler(SimpleHttpRequest request);
}
