package com.tone.netty.keepalive;

import java.io.Serializable;

/**
 * Created by jenny on 2017/3/9.
 */
public class NettyMessage implements Serializable {
    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NettyMessage [header=" + header + ", body=" + body + "]";
    }
}
