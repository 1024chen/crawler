package com.example.demo.crawler.service;

import com.example.demo.crawler.model.CrawlerLinkBo;
import com.example.demo.crawler.model.InnerTbody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest
class CrawlerServiceTest {
    @Resource
    private CrawlerService crawlerService;

    @Test
    void outCrawler() {
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstPageWithoutBrowser();
        Assertions.assertNotNull(crawlerLinkBoList);
        System.out.println(crawlerLinkBoList);
    }

    @Test
    void innerCrawler() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428653590777858.html";
        InnerTbody innerTbody = crawlerService.innerCrawler(url);
        Assertions.assertNotNull(innerTbody);
        System.out.println(innerTbody);
    }

    @Test
    void crawlerTBodyList(){
        List<InnerTbody> innerTbodyList = crawlerService.crawlerTBodyList();
        Assertions.assertNotNull(innerTbodyList);
        innerTbodyList.forEach(System.out::println);
    }

    @Test
    void crawlerAllPageWithChrome(){
        crawlerService.crawlerAllPageWithBrowser("chrome",false).forEach(System.out::println);
    }

    @Test
    void crawlerAllPageWithFirefox(){
        crawlerService.crawlerAllPageWithBrowser("firefox",false).forEach(System.out::println);
    }

    @Test
    void crawlerAllPageWithRemote(){
        crawlerService.crawlerAllPageWithBrowser("remote",false).forEach(System.out::println);
    }

    @Test
    void crawlerAllWeiXinTitle(){
        System.out.println(crawlerService.crawlerAllWeiXinTitle(false));
    }

    @Test
    void crawlerAllWithHttpPage(){
        System.out.println(crawlerService.crawlerAllWithHttpPage());
    }

    @Test
    void crawlerAllWithHttpString(){
        System.out.println(crawlerService.crawlerAllWithHttpString(false));
    }
}