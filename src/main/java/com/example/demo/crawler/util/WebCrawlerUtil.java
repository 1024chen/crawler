package com.example.demo.crawler.util;

import com.gargoylesoftware.htmlunit.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Setter
@Getter
@Slf4j
@Service
public class WebCrawlerUtil {
    /**
     * 请求超时时间,默认20000ms
     */
    private int timeout = 20000;
    /**
     * 等待异步JS执行时间,默认20000ms
     * 设置获取完整HTML页面时等待异步JS执行的时间
     */
    private int waitForBackgroundJavaScript = 20000;

    @Value("${crawler.proxy.host}")
    private String proxyHost;
    @Value("${crawler.proxy.port}")
    private int proxyPort;

    @Value("${crawler.driver.chrome.linux}")
    private String chromeLinuxDriver;
    @Value("${crawler.driver.chrome.windows}")
    private String chromeWindowsDriver;

    @Value("${crawler.driver.firefox.windows}")
    private String firefoxWindowsDriver;

    @Value("${crawler.driver.firefox.linux}")
    private String firefoxLinuxDriver;

    /**
     * 将网页返回为解析后的文档格式
     */
    public Document parseHtmlToDoc(String html) {
        return transHtmlToDocumentRemoveSpace(html);
//        return transHtmlToDocument(html);
    }

    private Document transHtmlToDocumentRemoveSpace(String str) {
        Document doc = Jsoup.parse(str);
        String result = doc.html().replace("&nbsp;", "");
        return Jsoup.parse(result);
    }

    private Document transHtmlToDocument(String str) {
        return Jsoup.parse(str);
    }

    /**
     * 获取页面文档(异步JS执行)
     */
    public String getHtmlPageResponseAsync(String url) {
        return getPage(url, false).asXml();
    }

    /**
     * 获取页面文档(异步JS执行+代理)
     */
    public String getHtmlPageResponseAsyncUsingProxy(String url) {
        return getPage(url, true).asXml();
    }

    public HtmlPage getPage(String url, boolean isProxyUsing) {
        final WebClient webClient = getWebClient(isProxyUsing);
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            log.error("页面获取失败{}", e.getMessage(), e);
        }
        webClient.waitForBackgroundJavaScript(waitForBackgroundJavaScript);
        webClient.close();
        assert page != null;
        return page;
    }

    public WebClient getWebClient(boolean isProxyUsing) {
        final WebClient webClient = isProxyUsing ? new WebClient(BrowserVersion.CHROME, proxyHost, proxyPort)
                : new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(timeout);
        webClient.setJavaScriptTimeout(timeout);
        return webClient;
    }

    /**
     * 获取页面文档Document对象(等待异步JS执行)
     */
    public Document getHtmlPageDocumentAsync(String url) {
        return parseHtmlToDoc(getHtmlPageResponseAsync(url));
    }

    /**
     * 获取页面文档Document对象(同步+代理)
     */
    public Document getHtmlPageDocumentSyncUsingProxy(String url) {
        try {
            return Jsoup.connect(url).proxy(proxyHost, proxyPort).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取页面文档Document对象(同步直连)
     */
    public Document getHtmlPageDocumentSync(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void firefoxDriverPropertySet() {
        System.setProperty("webdriver.gecko.driver", isWinCurrentSystem() ? firefoxWindowsDriver : firefoxLinuxDriver);
//        if (!isWinCurrentSystem()){
//            System.setProperty("webdriver.firefox.bin", "/usr/bin/firefox");
//        }
    }

    public void chromeDriverPropertySet() {
        System.setProperty("webdriver.chrome.driver", isWinCurrentSystem() ? chromeWindowsDriver : chromeLinuxDriver);
        System.setProperty("webdriver.chrome.whitelistedIps", "");
    }

    public WebDriver getDriver(String driver,boolean isProxyUsing) {
        if (driver.equals("chrome")) {
            return getChromeDriver(isProxyUsing);
        } else if (driver.equals("firefox")) {
            return getFirefoxDriver(isProxyUsing);
        } else {
            return null;
        }
    }

    private WebDriver getFirefoxDriver(boolean isProxyUsing) {
        firefoxDriverPropertySet();
        return new FirefoxDriver(getFirefoxOptions(isProxyUsing));
    }

    private WebDriver getChromeDriver(boolean isProxyUsing) {
        chromeDriverPropertySet();
        return new ChromeDriver(getChromeOptions(isProxyUsing));
    }


    public ChromeOptions getChromeOptions(boolean isProxyUsing) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("lang=zh_CN.UTF-8");
        chromeOptions.addArguments("window-size=1920x1080");
        chromeOptions.addArguments("--remote-allow-origins=*");
        if (isProxyUsing){
            chromeOptions.setProxy(getBrowserProxy());
        }
        return chromeOptions;
    }

    public FirefoxOptions getFirefoxOptions(boolean isProxyUsing) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");
        firefoxOptions.addArguments("--disable-gpu");
        if (isProxyUsing){
            firefoxOptions.setProxy(getBrowserProxy());
        }
        return firefoxOptions;
    }

    private Proxy getBrowserProxy(){
        String proxy = proxyHost + ":" + proxyPort;
        return new Proxy().setHttpProxy(proxy).setSslProxy(proxy);
    }

    private boolean isWinCurrentSystem() {
        return System.getProperty("os.name").toLowerCase()
                .contains("windows");
    }
}
