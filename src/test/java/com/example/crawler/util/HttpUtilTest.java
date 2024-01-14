package com.example.crawler.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HttpUtilTest {

    @Value("${crawler.ling-gang-gov-cn.media}")
    private String mediaUrl;

    @Test
    void get() {
        System.out.println(HttpUtil.get(mediaUrl + "index.html"));
    }

    @Test
    void post() {
        String body = "{\"pageSize\":90,\"pageNumber\":2,\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}";
        String url = "https://www.lingang.gov.cn/baseSelect";
        System.out.println(HttpUtil.post(url, body));
    }
}