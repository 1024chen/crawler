package com.example.demo.crawler.service;

import com.example.demo.crawler.model.CrawlerLinkBo;
import com.example.demo.crawler.model.InnerTbody;
import com.example.demo.crawler.model.IptContent;
import com.example.demo.crawler.util.WebCrawlerUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class CrawlerService {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private WebCrawlerUtil webCrawlerUtil;
    @Value("${crawler.ling-gang-gov-cn.index}")
    private String indexUrl;
    @Value("${crawler.ling-gang-gov-cn.basePath}")
    private String basePath;

    public List<CrawlerLinkBo> crawlerFirstPageWithoutBrowser() {
        Document document = webCrawlerUtil.getHtmlPageDocumentAsync(indexUrl);
        return getCrawlerLinkBoListFromDocument(document);
    }

    public List<CrawlerLinkBo> crawlerAllPageWithBrowser(String driverName,boolean isProxyUsing) {
        WebDriver driver = webCrawlerUtil.getDriver(driverName,isProxyUsing);
        assert driver != null;
        return crawlerLinkBoList(driver);
    }

    private List<CrawlerLinkBo> crawlerLinkBoList(WebDriver driver) {
        List<CrawlerLinkBo> crawlerLinkBoList = new ArrayList<>();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));
        driver.get(indexUrl);
        WebElement nextPage = driver.findElement(By.className("layui-laypage-next"));
        int lastIndex = Integer.parseInt(driver.findElement(By.className("layui-laypage-last")).getAttribute("data-page"));
        while (Integer.parseInt(nextPage.getAttribute("data-page")) <= lastIndex) {
            List<WebElement> webElementList = driver.findElement(By.className("pnwlst")).findElements(By.tagName("a"));
            webElementList.forEach(a -> {
                crawlerLinkBoList.add(CrawlerLinkBo.builder()
                        .fileLink(a.getAttribute("href"))
                        .fileTime(a.findElement(By.className("time")).getText())
                        .fileName(a.getText().trim().substring(10)).build());
            });
            nextPage.click();
            nextPage = driver.findElement(By.className("layui-laypage-next"));
        }
        driver.close();
        return crawlerLinkBoList;
    }

    private static List<CrawlerLinkBo> getCrawlerLinkBoListFromDocument(Document document) {
        Elements elements = document.getElementsByClass("pnwlst").get(0).getElementsByTag("a");
        List<CrawlerLinkBo> crawlerLinkBoList = new ArrayList<>();
        elements.forEach(a -> {
            crawlerLinkBoList.add(CrawlerLinkBo.builder()
                    .fileLink(a.attr("href"))
                    .fileTime(a.getElementsByClass("time").get(0).text())
                    .fileName(a.text().split(" ")[1]).build());
        });
        return crawlerLinkBoList;
    }

    public List<InnerTbody> crawlerTBodyList() {
        List<InnerTbody> innerTbodyList = new ArrayList<>();
        crawlerFirstPageWithoutBrowser().forEach(a -> {
            innerTbodyList.add(innerCrawler(a.getFileLink()));
        });
        return innerTbodyList;
    }

    public InnerTbody innerCrawler(String innerUrl) {
        Document document = webCrawlerUtil.getHtmlPageDocumentAsync(innerUrl);
        InnerTbody innerTbody = getInnerTBodyFromDocument(document);
        innerTbody.setContent(getIptContentFromDocument(document).getPath());
        return innerTbody;
    }

    private InnerTbody getInnerTBodyFromDocument(Document document) {
        Elements divElements = document.getElementsByClass("ifo_itv_tbl");
        Elements elements = divElements.get(0)
                .getElementsByTag("tbody").get(0)
                .getElementsByTag("tr");
        InnerTbody innerTbody = InnerTbody.builder().build();
        elements.forEach(innerTBodyAttributeAssign(innerTbody));
        return innerTbody;
    }

    private static Consumer<Element> innerTBodyAttributeAssign(InnerTbody innerTbody) {
        return a -> {
            String content = a.getElementsByTag("td").get(0).text();
            switch (a.getElementsByTag("th").get(0).text()) {
                case "索取号：":
                    innerTbody.setClaimCode(content);
                    break;
                case "文件编号：":
                    innerTbody.setFileCode(content);
                    break;
                case "关键词：":
                    innerTbody.setKeyWords(content);
                    break;
                case "信息名称：":
                    innerTbody.setInfoName(content);
                    break;
                case "公开类目：":
                    innerTbody.setOpenCategory(content);
                    break;
                case "附加文档：":
                    innerTbody.setAttachedDocument(content);
                    break;
                case "时间：":
                    innerTbody.setTime(content);
                    break;
                default:
                    break;
            }
        };
    }

    private IptContent getIptContentFromDocument(Document document) {
        Element element = document.getElementById("iptContent");
        assert element != null;
        IptContent content;
        try {
            content = objectMapper.readValue(element.val(), new TypeReference<List<IptContent>>() {
            }).get(0);
            content.setPath(basePath + content.getPath());
            content.setUrl(Objects.requireNonNull(document.getElementById("iptContent2")).text());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
}
