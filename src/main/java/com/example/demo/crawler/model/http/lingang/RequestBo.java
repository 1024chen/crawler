package com.example.demo.crawler.model.http.lingang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class RequestBo {
        private int pageSize;
        private int pageNumber;
        private List<String> columns;
        private String tableName;
        private String orSql;
        private String orderBy;
        private List<BetweenMap> betweenMap;
        private InMap inMap;
        private EqMap eqMap;
        private List<String> likeMap;
        private EmptyMap map;
        @JsonProperty("file_code like")
        private List<String> file_code_like;
}
