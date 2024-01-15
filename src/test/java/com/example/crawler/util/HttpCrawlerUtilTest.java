package com.example.crawler.util;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev")
class HttpCrawlerUtilTest {
    @Resource
    private HttpCrawlerUtil httpCrawlerUtil;

    @Test
    void linGangPageCrawler() {
        String jsonBody = "{\"pageSize\":90,\"pageNumber\":1,\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}";
        String res = httpCrawlerUtil.linGangPageCrawler(jsonBody);
        System.out.println(res);
    }

    @Test
    void mpWeiXinSpecialCaseCrawler() {
        String res = httpCrawlerUtil.mpWeiXinSpecialCaseCrawler();
        System.out.println(res);
    }

    @Test
    void mpWeiXinAllianceCrawler(){
        String res = httpCrawlerUtil.mpWeiXinAllianceCrawler();
        System.out.println(res);
    }
}