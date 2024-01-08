package com.example.demo.crawler.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HttpUtil {

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .build();

    public static String get(String url){
        Response execute = null;
        Request request = new Request.Builder()
                .method("GET",null)
                .url(url)
                .build();
        try {
            execute = client.newCall(request).execute();
            if (execute.isSuccessful()){
                assert execute.body() != null;
                return execute.body().string();
            }
        } catch (IOException e) {
            log.error("http get 请求失败--{}",e.getMessage(),e);
        }
        return null;
    }

    public static byte[] getBytes(String url){
        Response execute = null;
        Request request = new Request.Builder()
                .method("GET",null)
                .url(url)
                .build();
        try {
            execute = client.newCall(request).execute();
            if (execute.isSuccessful()){
                assert execute.body() != null;
                return execute.body().bytes();
            }
        } catch (IOException e) {
            log.error("http get 请求失败--{}",e.getMessage(),e);
        }
        return null;
    }

    public static String post(String url,String body){
        Response execute = null;
        Request request = new Request.Builder()
                .method("POST", RequestBody.create(body, MediaType.get("application/json")))
                .url(url)
                .build();
        try {
            execute = client.newCall(request).execute();
            if (execute.isSuccessful()){
                assert execute.body() != null;
                return execute.body().string();
            }
        } catch (IOException e) {
            log.error("http post 请求失败--{}",e.getMessage(),e);
        }
        return null;
    }
}
