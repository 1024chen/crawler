package com.example.demo.crawler.model;

import lombok.Builder;
import lombok.Data;

/**
 * 外层数据模型
 */
@Data
@Builder
public class CrawlerLinkBo {
    private String fileLink;
    private String fileTime;
    private String fileName;
}
