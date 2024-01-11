package com.example.crawler.model.http.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
public class Query {
    @Value("${lowCode.tenantId}")
    private String tenantId;
}
