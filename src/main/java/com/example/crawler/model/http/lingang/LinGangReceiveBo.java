package com.example.crawler.model.http.lingang;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinGangReceiveBo {
    private boolean result;
    private int code;
    private ReceiveData data;
}
