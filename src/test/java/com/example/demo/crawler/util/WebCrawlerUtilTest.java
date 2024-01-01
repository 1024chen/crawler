package com.example.demo.crawler.util;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

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
        //https://www.lingang.gov.cn/erroritem/2023-12-29/40780B6B-1957-EC5E-18A1-13A7C007C752.pdf?#toolbar=1&navpanes=0&scrollbar=0&view=FitH,top
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        FrameWindow frameWindow = webCrawlerUtil.getPage(url).getFrameByName("content");
//        Page page = frameWindow.getEnclosedPage();

        System.out.println(frameWindow);
    }
}