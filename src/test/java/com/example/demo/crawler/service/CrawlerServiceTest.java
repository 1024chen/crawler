package com.example.demo.crawler.service;

import com.example.demo.crawler.model.CrawlerLinkBo;
import com.example.demo.crawler.model.InnerTbody;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * TitleBo(title=联盟早餐会｜聚焦生物医药企业，临港新片区科技金融联盟早餐会第二期如约而至,
     * cover=http://mmbiz.qpic.cn/sz_mmbiz_jpg/Ege2ibuzgpmg2Om68FibLvnX35ic0leR9q8nKIqAf49LZfGDEEHmKLiak7Yvoc8ohchOEnvp2C1sab3icmJl6auA2SA/0,
     * link=http://mp.weixin.qq.com/s?__biz=Mzg2MjU1NDE2MA==&mid=2247508462&idx=1&sn=543d6578c2f921883f30677517fa256e&scene=19#wechat_redirect,
     * time=2023-08-28 20:36:49),
     */
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