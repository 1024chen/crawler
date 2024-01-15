package com.example.crawler.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

@Service
@Slf4j
public class HttpCrawlerUtil {
    @Value("${crawler.proxy.isProxyUsing}")
    private boolean isProxyUsing;
    @Value("${crawler.proxy.host}")
    private String proxyHost;
    @Value("${crawler.proxy.port}")
    private int proxyPort;

    @NotNull
    private OkHttpClient getClient() {
        return isProxyUsing ?
                new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))).build()
                : new OkHttpClient().newBuilder().build();
    }

    public String linGangPageCrawler(String jsonBody) {
        OkHttpClient client = getClient();
        MediaType mediaType = MediaType.parse(" application/json;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = getLinGangPageRequest(body);
        return getResponseString(client, request);
    }

    @NotNull
    private static String getResponseString(OkHttpClient client, Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            log.error("请求失败:{}", e.getMessage(), e);
        }
        assert Objects.requireNonNull(response).body() != null;
        try {
            return response.body().string();
        } catch (IOException e) {
            log.error("获取请求体失败:{}", e.getMessage(), e);
        }
        return "";
    }

    @NotNull
    private static Request getLinGangPageRequest(RequestBody body) {
        return new Request.Builder()
                .url("https://www.lingang.gov.cn/baseSelect")
                .method("POST", body).addHeader("Host", " www.lingang.gov.cn")
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0")
                .addHeader("Accept", " application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Language", " zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
//                .addHeader("Accept-Encoding", " gzip, deflate, br")
                .addHeader("Referer", " https://www.lingang.gov.cn/")
                .addHeader("Content-Type", " application/json;charset=UTF-8")
                .addHeader("X-Requested-With", " XMLHttpRequest")
                .addHeader("Content-Length", " 455")
                .addHeader("Origin", " https://www.lingang.gov.cn")
                .addHeader("Connection", " keep-alive")
//                .addHeader("Cookie", " _gscu_1287315781=03773748jwggp918; UM_distinctid=18cb0d43fcb5c6-01fd1ac12c277-e545621-232800-18cb0d43fcc10a7; CNZZDATA1280737590=1665392313-1703773749-https%253A%252F%252Fwww.lingang.gov.cn%252F%7C1704724249; SHRIOSESSIONID=cf1c5ebd-d667-44b4-9ab6-c33e954eeccd; Path=/; _gscbrs_1287315781=1; _gscs_1287315781=t0472419839pd6x19|pv:2; __vtins__KIx0bUbeAUcFBMFN=%7B%22sid%22%3A%20%226e0a15f1-92b4-5bb3-9a80-7493e37bf21a%22%2C%20%22vd%22%3A%201%2C%20%22stt%22%3A%200%2C%20%22dr%22%3A%200%2C%20%22expires%22%3A%201704726049309%2C%20%22ct%22%3A%201704724249309%7D; __51uvsct__KIx0bUbeAUcFBMFN=1; __51vcke__KIx0bUbeAUcFBMFN=d4b50808-275a-5c04-83ba-ba4df6a402d6; __51vuft__KIx0bUbeAUcFBMFN=1704724249310")
                .addHeader("Sec-Fetch-Dest", " empty")
                .addHeader("Sec-Fetch-Mode", " cors")
                .addHeader("Sec-Fetch-Site", " same-origin")
                .build();
    }

    /**
     * 特色案例爬取
     */
    public String mpWeiXinSpecialCaseCrawler() {
        OkHttpClient client = getClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = getWeiXinSpecialCaseRequest(body);
        return getResponseString(client, request);
    }

    /**
     * 联盟动态爬取
     */
    public String mpWeiXinAllianceCrawler(){
        OkHttpClient client = getClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = getWeiXinAllianceRequest(body);
        return getResponseString(client, request);
    }

    @NotNull
    private static Request getWeiXinAllianceRequest(RequestBody body){
        return new Request.Builder()
                .url("https://mp.weixin.qq.com/mp/homepage?__biz=Mzg2MjU1NDE2MA==&hid=8&sn=aa9785958170e807f2449c4bf8e2df44&scene=18&uin=&key=&devicetype=Windows 10 x64&version=63090819&lang=zh_CN&ascene=0&begin=0&count=30&action=appmsg_list&f=json&r=0.9853306790351559&appmsg_token=")
                .method("POST", body)
                .addHeader("Host", " mp.weixin.qq.com")
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0")
                .addHeader("Accept", " application/json")
                .addHeader("Accept-Language", " zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .addHeader("X-Requested-With", " XMLHttpRequest")
                .addHeader("Origin", " https://mp.weixin.qq.com")
                .addHeader("Connection", " keep-alive")
                .addHeader("Referer", " https://mp.weixin.qq.com/mp/homepage?__biz=Mzg2MjU1NDE2MA==&hid=8&sn=aa9785958170e807f2449c4bf8e2df44&scene=18&uin=&key=&devicetype=Windows+10+x64&version=63090819&lang=zh_CN&ascene=0")
//                .addHeader("Cookie", " RK=8iER79/IdG; ptcz=0a317de749be377e39a2be9cfb66e07158cf344c5c33d50054546a667bbedc29; pgv_pvid=3563990808; pac_uid=0_fa0eea6dc6976; iip=0; Qs_lvt_323937=1691227803; Qs_pv_323937=3462242744228334000; rewardsn=; wxtokenkey=777")
                .addHeader("Sec-Fetch-Dest", " empty")
                .addHeader("Sec-Fetch-Mode", " cors")
                .addHeader("Sec-Fetch-Site", " same-origin")
                .addHeader("Content-Length", " 0")
                .addHeader("TE", " trailers")
                .build();
    }

    @NotNull
    private static Request getWeiXinSpecialCaseRequest(RequestBody body) {
        return new Request.Builder()
                .url("https://mp.weixin.qq.com/mp/homepage?__biz=Mzg2MjU1NDE2MA==&hid=1&sn=3cbde566fe84879605db9a465fc21ed1&scene=1&uin=MjAyODQxNzA4MA==&key=1aeb31bef52afc46c0eef3dfc19cfe3a93e7c27bba2c2bfdbf4ca15cb79dc8591f39dfabdcf99e660260e16cd2b4f0dfa651d0374eed3d2eb2707bb4bdc61dd52ad661a6f8ec6f9572316f1d27908cee5ff91cc770adc029e193fca01170aa2c54b46367867297ce137f3f9f9ade2b052ed5a9b617ab2751a8aa202597eb697c&devicetype=android-33&version=28002c51&lang=zh_CN&ascene=1&session_us=gh_d4f0feda48c3&pass_ticket=g3NCO9nHt+wKRTEKuYNPqfrK7WvB1Gsarz0/3PfJYKcbAsbxzJFtEs0sL1rEr27BQa4xD8AlbGAMOVzaCLHrtQ==&wx_header=3&from=groupmessage&nettype=ctnet&begin=0&count=30&action=appmsg_list&f=json&r=0.9606280061739596&appmsg_token=")
                .method("POST", body)
                .addHeader("Host", " mp.weixin.qq.com")
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0")
                .addHeader("Accept", " application/json")
                .addHeader("Accept-Language", " zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .addHeader("X-Requested-With", " XMLHttpRequest")
                .addHeader("Origin", " https://mp.weixin.qq.com")
                .addHeader("Connection", " keep-alive")
                .addHeader("Referer", " https://mp.weixin.qq.com/mp/homepage?__biz=Mzg2MjU1NDE2MA==&hid=1&sn=3cbde566fe84879605db9a465fc21ed1&scene=1&uin=MjAyODQxNzA4MA%3D%3D&key=1aeb31bef52afc46c0eef3dfc19cfe3a93e7c27bba2c2bfdbf4ca15cb79dc8591f39dfabdcf99e660260e16cd2b4f0dfa651d0374eed3d2eb2707bb4bdc61dd52ad661a6f8ec6f9572316f1d27908cee5ff91cc770adc029e193fca01170aa2c54b46367867297ce137f3f9f9ade2b052ed5a9b617ab2751a8aa202597eb697c&devicetype=android-33&version=28002c51&lang=zh_CN&ascene=1&session_us=gh_d4f0feda48c3&pass_ticket=g3NCO9nHt%2BwKRTEKuYNPqfrK7WvB1Gsarz0%2F3PfJYKcbAsbxzJFtEs0sL1rEr27BQa4xD8AlbGAMOVzaCLHrtQ%3D%3D&wx_header=3&from=groupmessage&nettype=ctnet")
//                .addHeader("Cookie", " RK=8iER79/IdG; ptcz=0a317de749be377e39a2be9cfb66e07158cf344c5c33d50054546a667bbedc29; pgv_pvid=3563990808; pac_uid=0_fa0eea6dc6976; iip=0; Qs_lvt_323937=1691227803; Qs_pv_323937=3462242744228334000; _qpsvr_localtk=0.4847970718243365; mail5k=82b94262")
                .addHeader("Sec-Fetch-Dest", " empty")
                .addHeader("Sec-Fetch-Mode", " cors")
                .addHeader("Sec-Fetch-Site", " same-origin")
                .addHeader("Content-Length", " 0")
                .addHeader("TE", " trailers")
                .build();
    }
}
