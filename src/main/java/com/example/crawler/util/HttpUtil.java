package com.example.crawler.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HttpUtil {

    @Value("${crawler.proxy.isProxyUsing}")
    private boolean isProxyUsing;
    @Value("${crawler.proxy.host}")
    private String proxyHost;
    @Value("${crawler.proxy.port}")
    private int proxyPort;

    @NotNull
    private OkHttpClient getClient() {
        return isProxyUsing ?
                new OkHttpClient.Builder()
                        .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                        .callTimeout(10, TimeUnit.SECONDS)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS).build()
                : new OkHttpClient().newBuilder()
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public String get(String url) {
        Response execute = null;
        Request request = new Request.Builder()
                .method("GET", null)
                .url(url)
                .build();
        try {
            execute = getClient().newCall(request).execute();
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                return execute.body().string();
            }
        } catch (IOException e) {
            log.error("http get 请求失败--{}", e.getMessage(), e);
        }
        return null;
    }

    public String post(String url, String body) {
        Response execute = null;
        Request request = new Request.Builder()
                .method("POST", RequestBody.create(body, MediaType.get("application/json")))
                .url(url)
                .build();
        try {
            execute = getClient().newCall(request).execute();
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                return execute.body().string();
            }
        } catch (IOException e) {
            log.error("http post 请求失败--{}", e.getMessage(), e);
        }
        return null;
    }
}
