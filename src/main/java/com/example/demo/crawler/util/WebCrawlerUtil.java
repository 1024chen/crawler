package com.example.demo.crawler.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class WebCrawlerUtil {
    /**
     * 请求超时时间,默认20000ms
     */
    private int timeout = 20000;
    /**
     * 等待异步JS执行时间,默认20000ms
     */
    private int waitForBackgroundJavaScript = 20000;

    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置请求超时时间
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getWaitForBackgroundJavaScript() {
        return waitForBackgroundJavaScript;
    }

    /**
     * 设置获取完整HTML页面时等待异步JS执行的时间
     */
    public void setWaitForBackgroundJavaScript(int waitForBackgroundJavaScript) {
        this.waitForBackgroundJavaScript = waitForBackgroundJavaScript;
    }

    /**
     * 将网页返回为解析后的文档格式
     */
    public Document parseHtmlToDoc(String html) {
        return removeHtmlSpace(html);
    }

    private Document removeHtmlSpace(String str) {
        Document doc = Jsoup.parse(str);
        String result = doc.html().replace("&nbsp;", "");
        return Jsoup.parse(result);
    }

    /**
     * 获取页面文档(等待异步JS执行)
     */
    public String getHtmlPageResponse(String url) {
        String result = "";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(timeout);
        webClient.setJavaScriptTimeout(timeout);

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            log.error("页面获取失败{}",e.getMessage(),e);
        }
        webClient.waitForBackgroundJavaScript(waitForBackgroundJavaScript);

        assert page != null;
        result = page.asXml();
        webClient.close();

        return result;
    }

    /**
     * 获取页面文档Document对象(等待异步JS执行)
     */
    public Document getHtmlPageResponseAsDocument(String url) {
        return parseHtmlToDoc(getHtmlPageResponse(url));
    }
}
