package com.example.demo.crawler.util;

import com.example.demo.crawler.model.IptContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
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
        System.out.println(webCrawlerUtil.parseHtmlToDoc(webCrawlerUtil.getHtmlPageResponseAsync(url)));
    }

    @Test
    void getHtmlPageResponse() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(30000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(30000);
        System.out.println(webCrawlerUtil.getHtmlPageResponseAsync(url));
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
            content = objectMapper.readValue(element.val(), new TypeReference<List<IptContent>>() {}).get(0);
            content.setPath("https://www.lingang.gov.cn"+content.getPath());
            content.setUrl(Objects.requireNonNull(document.getElementById("iptContent2")).text());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(content);
    }

    @Test
    void getAllOut() throws IOException {
        try (final WebClient webClient = webCrawlerUtil.getWebClient(false)) {
            String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
            // Get the first page
            final HtmlPage page = webClient.getPage(url);
            DomNodeList<HtmlElement> elements = page.getElementById("pages")
                    .getFirstElementChild().getElementsByTagName("a");

            String pre = elements.get(0).getAttribute("data-page");
            String last = elements.get(elements.size() - 2).getAttribute("data-page");
            HtmlElement next = elements.get(elements.size() - 1);

            try {
                HtmlPage page1 = next.click();
                DomNodeList<HtmlElement> element = page1.getElementById("app").getElementsByTagName("div");
                HtmlElement htmlElement = element.stream()
                        .filter(a -> a.getAttribute("class").equals("pnwlst"))
                        .collect(Collectors.toList()).get(0);
                htmlElement.getFirstElementChild().getChildNodes().forEach(a -> {
                    System.out.println(a.getTextContent());
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //<a href="javascript:;" data-page="0" class="layui-laypage-prev layui-disabled"> 上一页 </a>
        //<a href="javascript:;" data-page="2"> 2 </a>
        //<a href="javascript:;" data-page="3"> 3 </a>
        //<a href="javascript:;" data-page="4"> 4 </a>
        //<a href="javascript:;" data-page="5"> 5 </a>
        //<a href="javascript:;" title="尾页" data-page="126" class="layui-laypage-last"> 126 </a>
        //<a href="javascript:;" data-page="2" class="layui-laypage-next"> 下一页 </a>
    }

    @Test
    void getAllBySelenium(){
        webCrawlerUtil.driverPropertySet();
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        WebDriver driver = new FirefoxDriver();
        driver.get(url);
        String title = driver.getTitle();
        System.out.printf(title);
        driver.close();
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