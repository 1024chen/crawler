package com.example.crawler.model.http.lingang;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BetweenMap {
    private String begin;
    private String end;
    private String column;
}