package com.example.crawler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {
    @GetMapping("crawler/allNews")
    public String crawlerAllNews(){
        return null;
    }
    @GetMapping("crawler/allPolicy")
    public String crawlerAllPolicy(){
        return null;
    }
    @GetMapping("crawler/allSpecial")
    public String crawlerAllSpecial(){
        return null;
    }
}
