package com.example.crawler.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateUtil {
    public static String transTimeMillisToString(long sendTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(sendTime * 1000L));
    }

    /**
     * 2023-12-18 -> 2023-12-18 00:00:00
     * 2023-08-28 20:36:49.0 -> 2023-08-28 20:36:49
     */
    public static String transFullTime(String time) {
        if (time == null || time.isEmpty()){
            return time;
        }
        String tm = time.trim();
        if (tm.length() == 10){
            return tm + " 00:00:00";
        }
        if (tm.length() > 19){
            return tm.substring(0,19);
        }
        return tm;
    }

    public static boolean isDateAfter(String oldTime,String newTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = df.parse(oldTime);
            endDate = df.parse(newTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return beginDate.before(endDate);
    }
}
