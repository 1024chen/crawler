package com.example.demo.crawler.model.http.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewAndSpecSaveBo {
    private Query query;
    private Update update;
}
