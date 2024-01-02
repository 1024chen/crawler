package com.example.demo.crawler.model;

import lombok.Data;

@Data
public class IptContent {
    //PDF 路径
    private String path;
    private String name;
    //html页面地址
    private String url;
}
