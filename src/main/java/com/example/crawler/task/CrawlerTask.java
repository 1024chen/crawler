package com.example.crawler.task;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CrawlerTask {
    @Value("${crawler.isInit}")
    private boolean isInit;

    /**
     * 每天23点
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void crawlerPolicyPerDay() {
        System.out.println("爬取政策");
    }

    /**
     * 每周五
     */
    @Scheduled(cron = "0 0 22 ? * 5")
    public void crawlerAlliancePerWeek() {
        System.out.println("爬取动态");
    }


    /**
     * 每周六
     */
    @Scheduled(cron = "0 0 21 ? * 5")
    public void crawlerSpecialCasePerWeek() {
        System.out.println("爬取特色案例");
    }

    /**
     * 每周三0点
     */
    @Scheduled(cron = "0 0 0 * * * ")
    public void crawlerMediaFocusPerDay() {
        System.out.println("爬取媒体聚焦");
    }

    @PostConstruct
    public void initPolicyCrawler() {
        if (isInit) {
            // TODO: Init
        } else {
            crawlerPolicyPerDay();
        }
    }

    @PostConstruct
    public void initAllianceCrawler() {
        if (isInit) {
            // TODO: Init
        } else {
            crawlerAlliancePerWeek();
        }
    }


    @PostConstruct
    public void initSpecialCaseCrawler() {
        if (isInit) {
            // TODO: Init
        } else {
            crawlerSpecialCasePerWeek();
        }
    }

    @PostConstruct
    public void initMediaFocusCrawler() {
        if (isInit) {
            // TODO: Init
        } else {
            crawlerMediaFocusPerDay();
        }
    }
}