package com.example.demo.other;

import com.example.demo.crawler.model.CrawlerLinkBo;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CrawlerTest {

    @Test
    void crawlerSite() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Elements divElements = document.getElementsByClass("ifo_itv_tbl");
            Element element = divElements.first();

            System.out.println(element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void crawlerSite1() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/1740428650726068226.html";
        String pageXml = getPage(url);
        Document document = null;
        document = Jsoup.parse(pageXml);
        Elements divElements = document.getElementsByClass("ifo_itv_tbl");
        Element element = divElements.first();

        System.out.println(element);
    }

    @Test
    void crawlerDynamicWeb() {
        String url = "https://www.lingang.gov.cn/html/website/lg/index/government/file/index.html";
        String pageXml = getPage(url);

        Document document = Jsoup.parse(pageXml);
        Elements elements = document.getElementsByClass("pnwlst").get(0).getElementsByTag("a");

        List<CrawlerLinkBo> crawlerLinkBoList = new ArrayList<>();
        elements.forEach(a -> {
            crawlerLinkBoList.add(CrawlerLinkBo.builder()
                    .fileLink(a.attr("href"))
                    .fileTime(a.getElementsByClass("time").get(0).text())
                    .fileName(a.text().split(" ")[1]).build());
        });
        System.out.println(crawlerLinkBoList);
    }

    private static String getPage(String url) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            webClient.close();
        }

        webClient.waitForBackgroundJavaScript(30000);
        return page.asXml();
    }
}
