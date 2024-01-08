package com.example.demo.crawler.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HttpCrawlerUtilTest {
    @Resource
    private HttpCrawlerUtil httpCrawlerUtil;
    @Test
    void linGangPageCrawler() {
        String jsonBody = "{\"pageSize\":90,\"pageNumber\":1,\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}";
        String res = httpCrawlerUtil.linGangPageCrawler(jsonBody,false);
        System.out.println(res);
    }

    @Test
    void mpWeiXinCrawler() {
        String res = httpCrawlerUtil.mpWeiXinCrawler(false);
        System.out.println(res);
    }
}