package com.example.demo.crawler.model.http.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeiXinReceiveBo {
    @JsonProperty("base_resp")
    private BaseResp baseResp;
    @JsonProperty("appmsg_list")
    private List<AppMsg> appMsgList;
    @JsonProperty("has_more")
    private int hasMore;
}
