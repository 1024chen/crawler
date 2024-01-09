package com.example.demo.crawler.service;

import com.example.demo.crawler.model.CrawlerLinkBo;
import com.example.demo.crawler.model.InnerTbody;
import com.example.demo.crawler.model.IptContent;
import com.example.demo.crawler.model.http.lingang.*;
import com.example.demo.crawler.model.http.weixin.TitleBo;
import com.example.demo.crawler.model.http.weixin.WeiXinReceiveBo;
import com.example.demo.crawler.util.HttpCrawlerUtil;
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
import java.util.*;
import java.util.function.Consumer;

@Service
public class CrawlerService {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private WebCrawlerUtil webCrawlerUtil;
    @Resource
    private HttpCrawlerUtil httpCrawlerUtil;
    @Value("${crawler.ling-gang-gov-cn.index}")
    private String indexUrl;
    @Value("${crawler.ling-gang-gov-cn.basePath}")
    private String basePath;

    /**
     * 请求获取weixin公众号列表
     */
    public List<TitleBo> crawlerAllWeiXinTitle(boolean isProxyUsing) {
        List<TitleBo> titleBoList = new ArrayList<>();
        String response = httpCrawlerUtil.mpWeiXinCrawler(false);
        WeiXinReceiveBo weiXinReceiveBo;
        try {
            weiXinReceiveBo = objectMapper.readValue(response, new TypeReference<WeiXinReceiveBo>() {
            });
            weiXinReceiveBo.getAppmsg_list().forEach(a -> {
                titleBoList.add(TitleBo.builder().title(a.getTitle()).link(a.getLink()).cover(a.getCover()).build());
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return titleBoList;
    }

    /**
     * 请求形式抓取
     */
    public List<CrawlerLinkBo> crawlerAllWithHttpPage() {
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        RequestBo requestBo = getRequestBo();
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
        LinGangReceiveBo receiveBo;
        try {
            receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
            });
            receiveBo.getData().getList().forEach(a -> {
                linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(a.getTIME().substring(0, 10)).fileLink(a.getContent()).build());
            });
            while (receiveBo.getData().isHasNextPage()) {
                requestBo.setPageNumber(requestBo.getPageNumber() + 1);
                try {
                    jsonBody = objectMapper.writeValueAsString(requestBo);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
                receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
                });
                receiveBo.getData().getList().forEach(a -> {
                    linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(a.getTIME().substring(0, 10)).fileLink(a.getContent()).build());
                });
            }
            ;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return linkBoList;
    }

    public List<CrawlerLinkBo> crawlerAllWithHttpString() {
        String[] reString = {"{\"pageSize\":90,\"pageNumber\":", ",\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}"};
        int pageNumber = 1;
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        RequestBo requestBo = getRequestBo();
        String jsonBody = reString[0] + pageNumber + reString[1];
        String response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
        LinGangReceiveBo receiveBo;
        try {
            receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
            });
            receiveBo.getData().getList().forEach(a -> {
                linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(a.getTIME().substring(0, 10)).fileLink(a.getContent()).build());
            });
            while (receiveBo.getData().isHasNextPage()) {
                pageNumber += 1;
                jsonBody = reString[0] + pageNumber + reString[1];
                response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
                receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
                });
                receiveBo.getData().getList().forEach(a -> {
                    linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(a.getTIME().substring(0, 10)).fileLink(a.getContent()).build());
                });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return linkBoList;
    }

    private RequestBo getRequestBo() {
        return RequestBo.builder().pageSize(90).pageNumber(1)
                .columns(Arrays.asList("id", "content_title", "content_datetime", "content_hit", "time", "file_url", "content", "public_type", "info_name", "words", "file_code", "sq_code", "manuscripts", "del"))
                .tableName("view_zhengcewenjian").orSql("").orderBy("content_datetime desc")
                .betweenMap(Collections.singletonList(BetweenMap.builder().begin("2019-01-01").end("2100-01-01").column("content_datetime").build()))
                .inMap(new InMap()).eqMap(EqMap.builder().del(0).content_display(0).build())
                .likeMap(new ArrayList<>()).map(new EmptyMap()).file_code_like(new ArrayList<>()).build();
    }

    /**
     * htmlunit形式抓取
     */
    public List<CrawlerLinkBo> crawlerFirstPageWithoutBrowser() {
        Document document = webCrawlerUtil.getHtmlPageDocumentAsync(indexUrl);
        return getCrawlerLinkBoListFromDocument(document);
    }

    /**
     * 浏览器形式抓取
     */
    public List<CrawlerLinkBo> crawlerAllPageWithBrowser(String driverName, boolean isProxyUsing) {
        WebDriver driver = webCrawlerUtil.getDriver(driverName, isProxyUsing);
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
