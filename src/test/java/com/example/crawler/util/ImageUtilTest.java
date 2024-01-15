package com.example.crawler.util;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class ImageUtilTest {
    @Test
    void getCoverImageBo() {
        String coverUrl = "http://mmbiz.qpic.cn/sz_mmbiz_jpg/Ege2ibuzgpmg2Om68FibLvnX35ic0leR9q8nKIqAf49LZfGDEEHmKLiak7Yvoc8ohchOEnvp2C1sab3icmJl6auA2SA/0";
        var imageBo = ImageUtil.getCoverImage(coverUrl);
        Assertions.assertEquals("Ege2ibuzgpmg2Om68FibLvnX35ic0leR9q8nKIqAf49LZfGDEEHmKLiak7Yvoc8ohchOEnvp2C1sab3icmJl6auA2SA.jpg",imageBo.getFileName());
        System.out.println(imageBo.getEncoded());
    }
}