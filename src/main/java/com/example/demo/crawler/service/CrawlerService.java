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
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    @Value("${crawler.ling-gang-gov-cn.media}")
    private String mediaUrl;
    @Value("${crawler.ling-gang-gov-cn.basePath}")
    private String basePath;

    /**
     * 请求获取特色案例列表
     */
    public List<TitleBo> crawler30WeiXinSpecialCase(boolean isProxyUsing) {
        String response = httpCrawlerUtil.mpWeiXinSpecialCaseCrawler(isProxyUsing);
        return transToTitleBoList(response);
    }

    /**
     * 请求获取联盟动态列表
     */
    public List<TitleBo> crawler30WeiXinAlliance(boolean isProxyUsing) {
        String response = httpCrawlerUtil.mpWeiXinAllianceCrawler(isProxyUsing);
        return transToTitleBoList(response);
    }

    @NotNull
    private List<TitleBo> transToTitleBoList(String response) {
        List<TitleBo> titleBoList = new ArrayList<>();
        WeiXinReceiveBo weiXinReceiveBo;
        try {
            weiXinReceiveBo = objectMapper.readValue(response, new TypeReference<WeiXinReceiveBo>() {
            });
            weiXinReceiveBo.getAppMsgList().forEach(a -> titleBoList.add(
                    TitleBo.builder()
                            .title(a.getTitle())
                            .link(a.getLink())
                            .cover(a.getCover())
                            .time(transTimeMillisToString(a.getSendTime())).build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return titleBoList;
    }

    /**
     * htmlunit形式抓取媒体聚焦首页
     */
    public List<CrawlerLinkBo> crawlerFistMediaFocusSync() {
        return getCrawlerLinkBoListFromDocument(webCrawlerUtil.getHtmlPageDocumentSync(mediaUrl + "index.html"));
    }

    /**
     * htmlunit形式抓取媒体聚焦全部页面
     */
    public List<CrawlerLinkBo> crawlerAllMediaFocusSync() {
        Document document = webCrawlerUtil.getHtmlPageDocumentSync(mediaUrl + "index.html");
        List<CrawlerLinkBo> crawlerLinkBoList = getCrawlerLinkBoListFromDocument(document);
        for (int i = 2; i < 133; i++) {
            crawlerLinkBoList.addAll(
                    getCrawlerLinkBoListFromDocument(
                            webCrawlerUtil.getHtmlPageDocumentSync(mediaUrl + "list-" + i + ".html")
                    )
            );
        }
        return crawlerLinkBoList;
    }

    /**
     * 请求形式抓取
     */
    public List<CrawlerLinkBo> crawlerAllGovFilesWithHttpPage() throws JsonProcessingException {
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        RequestBo requestBo = getRequestBo();
        String jsonBody;
        jsonBody = objectMapper.writeValueAsString(requestBo);
        String response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
        LinGangReceiveBo receiveBo;
        receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
        });
        receiveBo.getData().getList().forEach(a -> linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(transFullTime(a.getTIME())).fileLink(a.getContent()).build()));
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
            receiveBo.getData().getList().forEach(a -> linkBoList.add(CrawlerLinkBo.builder().fileName(a.getContent_title()).fileTime(transFullTime(a.getTIME())).fileLink(a.getContent()).build()));
        }
        return linkBoList;
    }

    /**
     * String形式抓取政策文件首页
     */
    public List<CrawlerLinkBo> crawlerFirstGovFilesWithHttpString(boolean isProxyUsing) throws JsonProcessingException {
        String[] reString = {"{\"pageSize\":90,\"pageNumber\":", ",\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}"};
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        String response = httpCrawlerUtil.linGangPageCrawler(reString[0] + 1 + reString[1], isProxyUsing);
        LinGangReceiveBo receiveBo;
        receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
        });
        receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
        return linkBoList;
    }

    /**
     * String形式抓取政策文件全部页面
     */
    public List<CrawlerLinkBo> crawlerAllGovFilesWithHttpString(boolean isProxyUsing) throws JsonProcessingException {
        String[] reString = {"{\"pageSize\":90,\"pageNumber\":", ",\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}"};
        int pageNumber = 1;
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        String jsonBody = reString[0] + pageNumber + reString[1];
        String response = httpCrawlerUtil.linGangPageCrawler(jsonBody, isProxyUsing);
        LinGangReceiveBo receiveBo;
        receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
        });
        receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
        while (receiveBo.getData().isHasNextPage()) {
            pageNumber += 1;
            jsonBody = reString[0] + pageNumber + reString[1];
            response = httpCrawlerUtil.linGangPageCrawler(jsonBody, false);
            receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
            });
            receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
        }
        return linkBoList;
    }

    @NotNull
    private Consumer<ContentData> getContentDataConsumer(List<CrawlerLinkBo> linkBoList) {
        return a -> {
            if (a.getDel().equals("0")) {
                linkBoList.add(CrawlerLinkBo.builder()
                        .fileName(a.getContent_title())
                        .fileTime(transFullTime(a.getTIME()))
                        .fileLink(a.getContent() != null ?
                                readIpContentFromString(a.getContent().substring(1, a.getContent().length() - 1)).getPath()
                                : "").build());
            }
        };
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
     * htmlunit形式抓取政策文件首页
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
            webElementList.forEach(a -> crawlerLinkBoList.add(CrawlerLinkBo.builder()
                    .fileLink(a.getAttribute("href"))
                    .fileTime(transFullTime(a.findElement(By.className("time")).getText()))
                    .fileName(a.getText().trim().substring(10)).build()));
            nextPage.click();
            nextPage = driver.findElement(By.className("layui-laypage-next"));
        }
        driver.close();
        return crawlerLinkBoList;
    }

    private static List<CrawlerLinkBo> getCrawlerLinkBoListFromDocument(Document document) {
        Elements elements = document.getElementsByClass("pnwlst").get(0).getElementsByTag("a");
        List<CrawlerLinkBo> crawlerLinkBoList = new ArrayList<>();
        elements.forEach(a -> crawlerLinkBoList.add(CrawlerLinkBo.builder()
                .fileLink(a.attr("href"))
                .fileTime(transFullTime(a.getElementsByClass("time").get(0).text()))
                .fileName(a.text().split(" ")[1]).build()));
        return crawlerLinkBoList;
    }

    public List<InnerTbody> crawlerTBodyList() {
        List<InnerTbody> innerTbodyList = new ArrayList<>();
        crawlerFirstPageWithoutBrowser().forEach(a -> innerTbodyList.add(innerCrawler(a.getFileLink())));
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

    private IptContent readIpContentFromString(String content) {
        IptContent result;
        try {
            result = objectMapper.readValue(content, new TypeReference<IptContent>() {
            });
            result.setPath(basePath + result.getPath());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String transTimeMillisToString(long sendTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(sendTime * 1000L));
    }

    /**
     * 2023-12-18
     * 2023-08-28 20:36:49
     */
    private static String transFullTime(String time) {
        if (time == null || time.isEmpty()){
            return time;
        }
        String tm = time.trim();
        if (tm.length() == 10){
            return tm + " 00:00:00";
        }
        if (tm.length() > 19){
            return tm.substring(0,19);
        }
        return tm;
    }
}
