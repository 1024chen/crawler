package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private long getDateDiffTwoString(String startDate,String endDate){
        try {
            Date oldDate = dateFormat.parse(startDate);
            Date nowDate = dateFormat.parse(endDate);
            return (nowDate.getTime() - oldDate.getTime()) / 3600 / 24 / 1000;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNowDate(){
        return dateFormat.format(new Date());
    }

    public boolean isBeyondOneYearToNow(String startDate){
        return getDateDiffTwoString(startDate,getNowDate()) >= 365;
    }
}
