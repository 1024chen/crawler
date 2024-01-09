package com.example.demo.crawler.model.http.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppMsg {
    private String aid;
    private String title;
    private String cover;
    private String link;
    private String digest;
    @JsonProperty("appmsgid")
    private String appMsgId;
    @JsonProperty("itemidx")
    private String itemIdx;
    private String type;
    @JsonProperty("item_show_type")
    private int itemShowType;
    @JsonProperty("copyright_stat")
    private int copyrightStat;
    private String author;
    @JsonProperty("sendtime")
    private long sendTime;
}
