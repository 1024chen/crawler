package com.example.crawler.model.http.response;

import lombok.Data;

@Data
public class OuterBo<T> {
    private boolean success;
    private String traceId;
    private T data;
}
