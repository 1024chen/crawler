package com.example.crawler.util;

import com.example.crawler.model.http.request.ImageBo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Slf4j
//@Service
public class ImageUtil {

    public static ImageBo getCoverImage(String coverUrl) {
        return ImageBo.builder()
                .fileName(getFileNameFromCover(coverUrl))
                .encoded(installImageBase64(coverUrl)).build();
    }

    private static String installImageBase64(String coverUrl) {
        String result = "";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            URL url = new URL(coverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            if (connection.getResponseCode() != 200) {
                return "返回码错误!:" + coverUrl;
            }
            InputStream inputStream = connection.getInputStream();
            int byteRead;
            while ((byteRead = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, byteRead);
            }
            inputStream.close();
            result =new String(Base64.getEncoder().encode(outputStream.toByteArray()));
        } catch (IOException e) {
            log.error("图片连接出错！{}", e.getMessage(), e);
        }
        return result;
    }

    @NotNull
    private static String getFileNameFromCover(String coverUrl) {
        String[] splitArray = coverUrl.split("/");
        if (splitArray.length > 3) {
            return splitArray[splitArray.length - 2] + ".jpg";
        } else {
            return "";
        }
    }
}
