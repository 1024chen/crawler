package com.example.demo.crawler.model.http.weixin;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeiXinReceiveBo {
    private BaseResp base_resp;
    private List<AppMsg> appmsg_list;
    private int has_more;
}
