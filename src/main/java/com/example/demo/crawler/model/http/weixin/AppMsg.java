package com.example.demo.crawler.model.http.weixin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppMsg {
    private String aid;
    private String title;
    private String cover;
    private String link;
    private String digest;
    private String appmsgid;
    private String itemidx;
    private String type;
    private int item_show_type;
    private int copyright_stat;
    private String author;
    private int sendtime;
}
