package com.example.crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InnerTbody {
    //索取号
    private String claimCode;
    //文件编号
    private String fileCode;
    //关键词
    private String keyWords;
    //信息名称
    private String infoName;
    //公开类目
    private String openCategory;
    //内容
    private String content;
    //附加文档
    private String attachedDocument;
    //时间
    private String time;
}
