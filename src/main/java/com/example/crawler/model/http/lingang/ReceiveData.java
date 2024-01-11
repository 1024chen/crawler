package com.example.crawler.model.http.lingang;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiveData {
    private Object navigatepageNums;
    private int startRow;
    private boolean hasNextPage;
    private int prePage;
    private int nextPage;
    private int endRow;
    private int pageSize;
    private List<ContentData> list;
    private int pageNum;
    private int navigatePages;
    private int navigateFirstPage;
    private int total;
    private int pages;
    private int size;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean navigateLastPage;
    private boolean isFirstPage;
}
