package com.example.crawler.model.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Update {
    @JsonProperty("s_SPCLZ_CASE_LIST_CHART")
    private String specialCaseListChart;
    @JsonProperty("S_POLCY_TITLE")
    private String policyTitle;
    @JsonProperty("s_SPCLZ_CASE_ANNC_TME")
    private String specialCaseAnnTime;
    @JsonProperty("s_NEWS_SRC")
    private String newsSrc;
    @JsonProperty("s_NEWS_SRC_ANNC_TME")
    private String newsSrcAnnTime;
    @JsonProperty("s_POLCY_SRC_ANNC_TME")
    private String policySrcAnnTime;
    @JsonProperty("s_NEWS_TP")
    private String newsType;
    @JsonProperty("s_NEWS_TITLE")
    private String newsTiTle;
    @JsonProperty("s_POLCY_SRC")
    private String policySrc;
    @JsonProperty("s_INF_RGSN_BOOK_STS")
    private String infRgsnBookStatus;
    @JsonProperty("EXTR_INF_SRC_FLG")
    private String externalInfoSourceFlag;
    @JsonProperty("s_SPCLZ_CASE_TITLE")
    private String specialCaseTitle;
    @JsonProperty("s_SPCLZ_CASE_URL")
    private String specialCaseUrl;
    @JsonProperty("NEWS_URL")
    private String newsUrl;
}
