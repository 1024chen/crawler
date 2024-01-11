package com.example.crawler.service;

import com.example.crawler.model.CrawlerLinkBo;
import com.example.crawler.model.InnerTbody;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



import java.util.List;

@SpringBootTest
class CrawlerServiceTest {
    @Resource
    private CrawlerService crawlerService;

    @Test
    void crawlerFirstPageWithoutBrowser() {
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstPageWithoutBrowser();
        Assertions.assertNotNull(crawlerLinkBoList);
        System.out.println(crawlerLinkBoList);
    }

    /**
     * fileTime=2024-01-08
     */
    @Test
    void crawlerFirstGovFilesWithHttpString() throws JsonProcessingException {
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstGovFilesWithHttpString(false);
        Assertions.assertNotNull(crawlerLinkBoList);
        System.out.println(crawlerLinkBoList);
    }

    /**
     * fileTime=2023-12-18
     */
    @Test
    void crawlerAllMediaFocusSync(){
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerAllMediaFocusSync();
        Assertions.assertNotNull(crawlerLinkBoList);
        System.out.println(crawlerLinkBoList);
    }

    @Test
    void crawlerFistMediaFocusSync(){
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFistMediaFocusSync();
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
    void crawler30WeiXinSpecialCase(){
        System.out.println(crawlerService.crawler30WeiXinSpecialCase(false));
    }


    @Test
    void crawler30WeiXinAlliance(){
        System.out.println(crawlerService.crawler30WeiXinAlliance(false));
    }

    @Test
    void crawlerAllGovFilesWithHttpPage() throws JsonProcessingException {
        System.out.println(crawlerService.crawlerAllGovFilesWithHttpPage());
    }

    @Test
    void crawlerAllGovFilesWithHttpString() throws JsonProcessingException {
        System.out.println(crawlerService.crawlerAllGovFilesWithHttpString(false));
    }
}