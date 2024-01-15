package com.example.crawler.service;

import com.example.crawler.model.CrawlerLinkBo;
import com.example.crawler.model.InnerTbody;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
class CrawlerServiceTest {
    @Resource
    private CrawlerService crawlerService;

    @Test
    void getSpecialCase(){
        System.out.println(crawlerService.getSpecialCase());
    }

    @Test
    void getAlliance(){
        System.out.println(crawlerService.getAlliance());
    }

    @Test
    void getAllGovFiles(){
        System.out.println(crawlerService.getAllGovFiles());
    }

    @Test
    void getSomeGovFiles(){
        System.out.println(crawlerService.getSomeGovFiles());
    }

    @Test
    void getAllMediaFocus(){
        System.out.println(crawlerService.getAllMediaFocus());
    }

    @Test
    void getSomeMediaFocus(){
        System.out.println(crawlerService.getSomeMediaFocus());
    }

    @Test
    void crawlerFirstPageWithoutBrowser() {
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstGovFilesPageWithoutBrowser();
        Assertions.assertNotNull(crawlerLinkBoList);
        System.out.println(crawlerLinkBoList);
    }

    /**
     * fileTime=2024-01-08
     */
    @Test
    void crawlerFirstGovFilesWithHttpString() throws JsonProcessingException {
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstGovFilesWithHttpString();
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
        List<CrawlerLinkBo> crawlerLinkBoList = crawlerService.crawlerFirstMediaFocusSync();
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
        crawlerService.crawlerAllPageWithBrowser("chrome").forEach(System.out::println);
    }

    @Test
    void crawlerAllPageWithFirefox(){
        crawlerService.crawlerAllPageWithBrowser("firefox").forEach(System.out::println);
    }

    @Test
    void crawlerAllPageWithRemote(){
        crawlerService.crawlerAllPageWithBrowser("remote").forEach(System.out::println);
    }

    @Test
    void crawler30WeiXinSpecialCase(){
        System.out.println(crawlerService.crawler30WeiXinSpecialCase());
    }


    @Test
    void crawler30WeiXinAlliance(){
        System.out.println(crawlerService.crawler30WeiXinAlliance());
    }

    @Test
    void crawlerAllGovFilesWithHttpPage() throws JsonProcessingException {
        System.out.println(crawlerService.crawlerAllGovFilesWithHttpPage());
    }

    @Test
    void crawlerAllGovFilesWithHttpString() throws JsonProcessingException {
        System.out.println(crawlerService.crawlerAllGovFilesWithHttpString());
    }
}