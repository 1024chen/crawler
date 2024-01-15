package com.example.crawler.util;

import com.example.crawler.model.IptContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import jakarta.annotation.Resource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.io.IOException;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@ActiveProfiles("dev")
class WebCrawlerUtilTest {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private WebCrawlerUtil webCrawlerUtil;

    @Test
    void parseHtmlToDoc() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(30000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(30000);
        Assertions.assertNotNull(webCrawlerUtil.parseHtmlToDoc(webCrawlerUtil.getHtmlPageResponseAsync(url)));
    }

    @Test
    void getHtmlPageResponse() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(30000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(30000);
        Assertions.assertNotNull(webCrawlerUtil.getHtmlPageResponseAsync(url));
    }

    @Test
    void getHtmlPageResponseAsDocument() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        webCrawlerUtil.setTimeout(6000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(6000);
        Document document = webCrawlerUtil.getHtmlPageDocumentAsync(url);
        Element element = document.getElementById("iptContent");
        assert element != null;
        IptContent content;
        try {
            content = objectMapper.readValue(element.val(), new TypeReference<List<IptContent>>() {
            }).get(0);
            content.setPath("https://www.lingang.gov.cn" + content.getPath());
            content.setUrl(Objects.requireNonNull(document.getElementById("iptContent2")).text());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(content);
    }

    @Test
    void getWebClient() throws IOException {
        try (final WebClient webClient = webCrawlerUtil.getWebClient()) {
            String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
            final HtmlPage page = webClient.getPage(url);
            DomNodeList<HtmlElement> elements = page.getElementById("pages")
                    .getFirstElementChild().getElementsByTagName("a");
            Assertions.assertFalse(elements.isEmpty());
        }
    }

    @Test
    void firefoxDriverPropertySet() {
        webCrawlerUtil.firefoxDriverPropertySet();
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        WebDriver driver = new FirefoxDriver(webCrawlerUtil.getFirefoxOptions());
        driver.get(url);
        String title = driver.getTitle();
        System.out.printf(title);
        driver.close();
    }

    @Test
    void chromeDriverPropertySet() {
        webCrawlerUtil.chromeDriverPropertySet();
    }

    @Test
    void getHtmlPageDocumentSync() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(6000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(6000);
        Document document = webCrawlerUtil.getHtmlPageDocumentSync(url);
        Assertions.assertNotNull(document);
    }
}