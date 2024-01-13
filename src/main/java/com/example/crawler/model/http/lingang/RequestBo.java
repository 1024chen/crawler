package com.example.crawler.model.http.lingang;

import com.example.crawler.util.serializer.NullObjectToEmptySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
        @JsonSerialize(nullsUsing = NullObjectToEmptySerializer.class)
        private Empty inMap;

        private EqMap eqMap;
        private List<String> likeMap;
        @JsonSerialize(nullsUsing = NullObjectToEmptySerializer.class)
        private Empty map;
        @JsonProperty("file_code like")
        private List<String> file_code_like;
}
