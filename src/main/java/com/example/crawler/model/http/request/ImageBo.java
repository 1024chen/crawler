package com.example.crawler.model.http.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageBo {
    private String fileName;
    private String encoded;
}
