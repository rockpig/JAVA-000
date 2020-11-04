package com.example.javacourse.gateway.outbound.httpclient;


import com.example.javacourse.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {

    private final CloseableHttpClient httpclient;
    private final String backendUrl;

    public HttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        httpclient = HttpClients.createDefault();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws IOException {
        final String url = this.backendUrl + fullRequest.uri();

        HttpGet httpGet = new HttpGet(url);
        HttpHeaders headers = fullRequest.headers();
        for (Map.Entry<String, String> entry : headers) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        CloseableHttpResponse execute = httpclient.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        byte[] body = EntityUtils.toByteArray(entity);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
        response.headers().set("Content-Type", "application/json");
        response.headers().set("Content-Length", entity.getContentLength());
        ctx.write(response);
    }

}
