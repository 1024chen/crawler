package com.example.crawler.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class TimeDateUtilTest {

    @Test
    void transFullTime() {
        Assertions.assertEquals("2023-11-21 21:21:23",TimeDateUtil.transFullTime("2023-11-21 21:21:23.0"));
    }

    @Test
    void transTimeMillisToString() {
        Assertions.assertEquals("2023-03-23 19:01:36",TimeDateUtil.transTimeMillisToString(1679569296));
    }

    @Test
    void isDateAfter() {
        String beginTime = "2021-10-01 10:22:33";
        String endTime = "2021-11-01 11:22:22";
        Assertions.assertTrue(TimeDateUtil.isDateAfter(beginTime,endTime));
    }
}