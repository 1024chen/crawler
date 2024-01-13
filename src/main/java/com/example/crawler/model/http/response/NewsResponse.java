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
    private String newsContent;
    @JsonProperty("s_POLCY_SRC_ANNC_TME")
    private String policySrcAnnTime;
    @JsonProperty("s_SPCLZ_CASE_ANN_TME")
    private String specialCaseAnnTime;
    private String tenantId;
    private String name;
    private String createdTime;
    @JsonProperty("s_SPCLZ_CASE_TITLE")
    private String specialCaseTitle;
    private String id;
    private String enterpriseId;
    @JsonProperty("s_INF_RGSN_B0OK_STS")
    private EnumInnerBo infoReBookStatus;
    private String responsibleUserId;
    @JsonProperty("s_NEWS_TP")
    private EnumInnerBo newsType;
    @JsonProperty("NEWS_URL")
    private String newsUrl;
}
