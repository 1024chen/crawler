package com.example.demo.crawler.util;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebCrawlerUtilTest {

    @Resource
    private WebCrawlerUtil webCrawlerUtil;

    @Test
    void parseHtmlToDoc() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(30000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(30000);
        System.out.println(webCrawlerUtil.parseHtmlToDoc(webCrawlerUtil.getHtmlPageResponse(url)));
    }

    @Test
    void getHtmlPageResponse() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(30000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(30000);
        System.out.println(webCrawlerUtil.getHtmlPageResponse(url));
    }

    @Test
    void getHtmlPageResponseAsDocument() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        webCrawlerUtil.setTimeout(3000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(3000);
        System.out.println(webCrawlerUtil.getHtmlPageResponseAsDocument(url));
    }

    @Test
    void getInner(){
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        Document document = webCrawlerUtil.getHtmlPageResponseAsDocument(url);
        Elements elements = document.getElementsByClass("ifo_itv_tbl")
                .get(0).getElementsByTag("tbody");
        System.out.println(elements);
    }
}