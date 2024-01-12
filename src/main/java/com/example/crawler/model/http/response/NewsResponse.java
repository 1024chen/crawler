package com.example.crawler.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewsResponse {
    private String modifiedTime;
    @JsonProperty("s_POLCY_SRC")
    private String policySrc;
    private String createUserId;
    @JsonProperty("s_POLCY_TITLE")
    private String policyTitle;
    @JsonProperty("s_NEWS_SRC")
    private String newsSrc;
    private String responsibleDepartmentId;
    private String modifyDepartmentId;
    @JsonProperty("EXTR_INF_SRC_FLG")
    private EnumInnerBo externalInfoSourceFlag;
    private String modifyUserId;
    private String createDepartmentId;
    @JsonProperty("s_SPCLZ_CASE_URL")
    private String specialCaseUrl;
    @JsonProperty("s_NEWS_TITLE")
    private String newsTitle;
    private String subObjId;
    private String companyId;
    @JsonProperty("s_NEWS_SRC_ANNC_TME")
    private String newsSrcAnnTime;
    private String isDeleted;
    @JsonProperty("s_NEWS_QNTNT")
    private String newscontent;0个用法@JsonProperty("s_POLCX_SRC_ANNC-TME"）private String policySrcAnnTime;0个用法@JsonProperty("s_SPCLZ-CASE ANNCTME")private String specialcaseAnnTime;0个用法private String tenantId;0个用法private String name;0个用法private String createdTime;0个用法@JsonProperty("s_SRCLZ_CASE_TITLE")private String specialCaseTitle;0个用法private String id;0个用法private String enterpriseId;信息登记薄状态/0个用法@JsonProperty("s_INF_RGSN-B0OK_STS")private EnumInnerBo 1nfoReBookStatus;0个用法private String responsibleuserId;0个用法@JsonProperty("s_NEWS_TP")private EnumInnerBo newsType;0个用法@JsonProperty("NEWS_URL")private String newsurl;I
}
