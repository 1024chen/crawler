package com.example.crawler.service;

import com.example.crawler.model.CrawlerLinkBo;
import com.example.crawler.model.InnerTbody;
import com.example.crawler.model.IptContent;
import com.example.crawler.model.http.lingang.*;
import com.example.crawler.model.http.request.NewAndSpecSaveBo;
import com.example.crawler.model.http.request.Query;
import com.example.crawler.model.http.request.Update;
import com.example.crawler.model.http.weixin.TitleBo;
import com.example.crawler.model.http.weixin.WeiXinReceiveBo;
import com.example.crawler.util.HttpCrawlerUtil;
import com.example.crawler.util.TimeDateUtil;
import com.example.crawler.util.WebCrawlerUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class CrawlerService {

    @Value("${backend.tenantId}")
    private String tenantId;

    @Value("${news.type.specialId}")
    private String specialId;
    @Value("${news.type.policyId}")
    private String policyId;
    @Value("${news.type.newsId}")
    private String newsId;
    @Value("${news.status.unpublishedId}")
    private String unpublished;
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
    @Value("${news.isExternalId}")
    private String isExternalId;
    final String[] reString = {"{\"pageSize\":90,\"pageNumber\":", ",\"columns\":[\"id\",\"content_title\",\"content_datetime\",\"content_hit\",\"time\",\"file_url\",\"content\",\"public_type\",\"info_name\",\"words\",\"file_code\",\"sq_code\",\"manuscripts\",\"del\"],\"tableName\":\"view_zhengcewenjian\",\"orSql\":\"\",\"orderBy\":\"content_datetime desc\",\"betweenMap\":[{\"begin\":\"2019-01-01\",\"end\":\"2100-01-01\",\"column\":\"content_datetime\"}],\"inMap\":{},\"eqMap\":{\"del\":0,\"content_display\":0},\"likeMap\":[],\"map\":{},\"file_code like\":[]}"};

    /**
     * 特色案例
     */
    public List<NewAndSpecSaveBo> getSpecialCase() {
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        crawler30WeiXinSpecialCase(false).forEach(transTitleListToSpecialList(allSaveBoList));
        return allSaveBoList;
    }

    /**
     * 联盟动态
     */
    public List<NewAndSpecSaveBo> getAlliance(){
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        crawler30WeiXinAlliance(false).forEach(transTitleListToAllianceList(allSaveBoList));
        return allSaveBoList;
    }

    public List<NewAndSpecSaveBo> getAllGovFiles(){
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        try {
            crawlerAllGovFilesWithHttpPage().forEach(transCrawlerListToFileList(allSaveBoList));
        } catch (JsonProcessingException e) {
            log.error("获取失败,{}",e.getMessage(),e);
        }
        return allSaveBoList;
    }

    /**
     * 政策文件
     */
    public List<NewAndSpecSaveBo> getSomeGovFiles(){
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        try {
            crawlerFirstGovFilesWithHttpString(false).forEach(transCrawlerListToFileList(allSaveBoList));
        } catch (JsonProcessingException e) {
            log.error("获取失败,{}",e.getMessage(),e);
        }
        return allSaveBoList;
    }

    public List<NewAndSpecSaveBo> getAllMediaFocus(){
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        crawlerAllMediaFocusSync().forEach(transCrawlerListToFocusList(allSaveBoList));
        return allSaveBoList;
    }

    /**
     * 媒体聚焦
     */
    public List<NewAndSpecSaveBo> getSomeMediaFocus(){
        List<NewAndSpecSaveBo> allSaveBoList = new ArrayList<>();
        crawlerFirstMediaFocusSync().forEach(transCrawlerListToFocusList(allSaveBoList));
        return allSaveBoList;
    }

    @NotNull
    private Consumer<TitleBo> transTitleListToAllianceList(List<NewAndSpecSaveBo> allSaveBoList) {
        return a ->
                allSaveBoList.add(NewAndSpecSaveBo.builder()
                        .query(getQuery())
                        .update(Update.builder()
                                .newsType(newsId)
                                .newsSrc("滴水湖金融湾")
                                .externalInfoSourceFlag(isExternalId)
                                .newsUrl(a.getLink())
                                .newsTiTle(a.getTitle())
                                .newsSrcAnnTime(a.getTime())
                                .specialCaseListChart(a.getCover()).build()).build());
    }

    @NotNull
    private Consumer<TitleBo> transTitleListToSpecialList(List<NewAndSpecSaveBo> allSaveBoList) {
        return a ->
                allSaveBoList.add(NewAndSpecSaveBo.builder()
                        .query(getQuery())
                        .update(Update.builder()
                                .newsType(specialId)
                                .newsSrc("滴水湖金融湾")
                                .externalInfoSourceFlag(isExternalId)
                                .specialCaseTitle(a.getTitle())
                                .specialCaseUrl(a.getLink())
                                .specialCaseAnnTime(a.getTime())
                                .specialCaseListChart(a.getCover()).build()).build());
    }

    @NotNull
    private Consumer<CrawlerLinkBo> transCrawlerListToFileList(List<NewAndSpecSaveBo> allSaveBoList) {
        return a -> allSaveBoList.add(
                NewAndSpecSaveBo.builder()
                        .query(getQuery())
                        .update(Update.builder()
                                .newsType(policyId)
                                .newsSrc("临港新片区管委会")
                                .externalInfoSourceFlag(isExternalId)
                                .policyTitle(a.getFileName())
                                .policySrc(a.getFileLink())
                                .policySrcAnnTime(a.getFileTime()).build()).build()
        );
    }

    @NotNull
    private Consumer<CrawlerLinkBo> transCrawlerListToFocusList(List<NewAndSpecSaveBo> allSaveBoList) {
        return a -> allSaveBoList.add(
                NewAndSpecSaveBo.builder()
                        .query(getQuery())
                        .update(Update.builder()
                                .newsType(newsId)
                                .newsSrc("管委会官网")
                                .externalInfoSourceFlag(isExternalId)
                                .newsTiTle(a.getFileName())
                                .newsUrl(a.getFileLink())
                                .newsSrcAnnTime(a.getFileTime()).build()).build()
        );
    }

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
                            .time(TimeDateUtil.transTimeMillisToString(a.getSendTime())).build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Collections.reverse(titleBoList);
        return titleBoList;
    }

    /**
     * htmlunit形式抓取媒体聚焦首页
     */
    public List<CrawlerLinkBo> crawlerFirstMediaFocusSync() {
        List<CrawlerLinkBo> crawlerLinkBoList = getCrawlerLinkBoListFromDocument(
                webCrawlerUtil.getHtmlPageDocumentSync(mediaUrl + "index.html"));
        Collections.reverse(crawlerLinkBoList);
        return crawlerLinkBoList;
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
        Collections.reverse(crawlerLinkBoList);
        return crawlerLinkBoList;
    }

    /**
     * 请求形式抓取政策文件全部
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
        receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
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
            receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
        }
        Collections.reverse(linkBoList);
        return linkBoList;
    }

    /**
     * String形式抓取政策文件首页
     */
    public List<CrawlerLinkBo> crawlerFirstGovFilesWithHttpString(boolean isProxyUsing) throws JsonProcessingException {
        List<CrawlerLinkBo> linkBoList = new ArrayList<>();
        String response = httpCrawlerUtil.linGangPageCrawler(reString[0] + 1 + reString[1], isProxyUsing);
        LinGangReceiveBo receiveBo;
        receiveBo = objectMapper.readValue(response, new TypeReference<LinGangReceiveBo>() {
        });
        receiveBo.getData().getList().forEach(getContentDataConsumer(linkBoList));
        Collections.reverse(linkBoList);
        return linkBoList;
    }

    /**
     * String形式抓取政策文件全部
     */
    public List<CrawlerLinkBo> crawlerAllGovFilesWithHttpString(boolean isProxyUsing) throws JsonProcessingException {
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
        Collections.reverse(linkBoList);
        return linkBoList;
    }

    @NotNull
    private Consumer<ContentData> getContentDataConsumer(List<CrawlerLinkBo> linkBoList) {
        return a -> {
            if (a.getDel().equals("0")) {
                linkBoList.add(CrawlerLinkBo.builder()
                        .fileName(a.getContent_title())
                        .fileTime(TimeDateUtil.transFullTime(a.getTIME()))
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
                .inMap(null).eqMap(EqMap.builder().del(0).content_display(0).build())
                .map(null).likeMap(new ArrayList<>()).file_code_like(new ArrayList<>()).build();
    }

    /**
     * htmlunit形式抓取政策文件首页，使用页面异步解析方式，时间较前两者长
     */
    public List<CrawlerLinkBo> crawlerFirstGovFilesPageWithoutBrowser() {
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
                    .fileTime(TimeDateUtil.transFullTime(a.findElement(By.className("time")).getText()))
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
                .fileTime(TimeDateUtil.transFullTime(a.getElementsByClass("time").get(0).text()))
                .fileName(a.text().split(" ")[1]).build()));
        return crawlerLinkBoList;
    }

    public List<InnerTbody> crawlerTBodyList() {
        List<InnerTbody> innerTbodyList = new ArrayList<>();
        crawlerFirstGovFilesPageWithoutBrowser().forEach(a -> innerTbodyList.add(innerCrawler(a.getFileLink())));
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
                case "索取号：" -> innerTbody.setClaimCode(content);
                case "文件编号：" -> innerTbody.setFileCode(content);
                case "关键词：" -> innerTbody.setKeyWords(content);
                case "信息名称：" -> innerTbody.setInfoName(content);
                case "公开类目：" -> innerTbody.setOpenCategory(content);
                case "附加文档：" -> innerTbody.setAttachedDocument(content);
                case "时间：" -> innerTbody.setTime(content);
                default -> {
                }
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

    private Query getQuery(){
        return Query.builder().tenantId(tenantId).build();
    }
}
