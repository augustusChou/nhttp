package com.github.nhttp.codec;

import io.netty.util.CharsetUtil;

public class Message {


    private int length;
    private byte[] data;


    public Message() {
    }

    public Message(String data) {
        this.data = data.getBytes(CharsetUtil.UTF_8);
        this.length = this.data.length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
