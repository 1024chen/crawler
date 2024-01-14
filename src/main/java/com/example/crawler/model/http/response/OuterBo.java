package com.example.crawler.model.http.response;

import lombok.Data;

@Data
public class OuterBo {
    private boolean success;
    private String traceId;
    private NewsResponse data;
}
