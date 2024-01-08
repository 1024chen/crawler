package com.example.demo.crawler.model.http.lingang;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentData {
    private String content_title;
    private String file_url;
    private String file_code;
    private String sq_code;
    private String content_datetime;
    private String TIME;
    private String info_name;
    private String del;
    private String id;
    private String content_hit;
    //"[{\"path\":\"/erroritem/2024-01-05/8FFBA164-545D-0003-CAB4-5D8C89348A80.pdf\",\"name\":\"关于特斯拉充电桩项目准予变更行政许可决定书.pdf\",\"url\":\"/erroritem/2024-01-05/8FFBA164-545D-0003-CAB4-5D8C89348A80.pdf\"}]"
    private String content;
    private String public_type;
}
