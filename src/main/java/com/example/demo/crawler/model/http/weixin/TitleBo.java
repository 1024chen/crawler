package com.example.demo.crawler.model.http.weixin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TitleBo {
    private String title;
    private String cover;
    private String link;
}