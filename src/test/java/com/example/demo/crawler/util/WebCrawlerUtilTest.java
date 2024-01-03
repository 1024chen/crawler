package com.example.demo.crawler.util;

import com.example.demo.crawler.model.IptContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Objects;

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
    void getAllOutPageData() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        webCrawlerUtil.setTimeout(6000);
        webCrawlerUtil.setWaitForBackgroundJavaScript(6000);
        Document document = webCrawlerUtil.getHtmlPageDocumentAsync(url);
        Element element = document.getElementById("pages");
        assert element != null;
        Elements elements = element.getElementsByTag("a");
        elements.forEach(System.out::println);

//        <a href="javascript:;" data-page="0" class="layui-laypage-prev layui-disabled"> 上一页 </a>
//<a href="javascript:;" data-page="2"> 2 </a>
//<a href="javascript:;" data-page="3"> 3 </a>
//<a href="javascript:;" data-page="4"> 4 </a>
//<a href="javascript:;" data-page="5"> 5 </a>
//<a href="javascript:;" title="尾页" data-page="126" class="layui-laypage-last"> 126 </a>
//<a href="javascript:;" data-page="2" class="layui-laypage-next"> 下一页 </a>
//        https://www.5axxw.com/questions/simple/autyj2
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