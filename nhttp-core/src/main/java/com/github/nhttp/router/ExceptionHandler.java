package com.github.nhttp.router;

import io.netty.channel.ChannelHandlerContext;

public interface ExceptionHandler {

    void exceptionHandler(ChannelHandlerContext ctx, Throwable cause);

}
