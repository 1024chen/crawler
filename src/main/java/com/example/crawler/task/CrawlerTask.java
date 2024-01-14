package com.example.crawler.task;

import com.example.crawler.service.SendService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CrawlerTask {
    @Value("${crawler.isInit}")
    private boolean isInit;

    @Resource
    private SendService sendService;

    /**
     * 每天23点
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void crawlerPolicyPerDay() {
        sendService.sendSomeGovFiles();
    }

    /**
     * 每周五
     */
    @Scheduled(cron = "0 0 22 ? * 5")
    public void crawlerAlliancePerWeek() {
        sendService.sendAlliance();
    }


    /**
     * 每周六
     */
    @Scheduled(cron = "0 0 21 ? * 5")
    public void crawlerSpecialCasePerWeek() {
        sendService.sendSpecialCase();
    }

    /**
     * 每周三0点
     */
    @Scheduled(cron = "0 0 0 * * * ")
    public void crawlerMediaFocusPerDay() {
        sendService.sendSomeMediaFocus();
    }

    @PostConstruct
    public void initPolicyCrawler() {
        if (isInit) {
            sendService.sendAllGovFiles();
        } else {
            crawlerPolicyPerDay();
        }
    }

    @PostConstruct
    public void initAllianceCrawler() {
        crawlerAlliancePerWeek();
    }


    @PostConstruct
    public void initSpecialCaseCrawler() {
        crawlerSpecialCasePerWeek();
    }

    @PostConstruct
    public void initMediaFocusCrawler() {
        if (isInit) {
            sendService.sendAllMediaFocus();
        } else {
            crawlerMediaFocusPerDay();
        }
    }
}