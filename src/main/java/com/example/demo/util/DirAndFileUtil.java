package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class DirAndFileUtil {
    /**
     * 打包压缩文件夹
     */
    public boolean zipFolder(String folderPath, String zipFilePath) {
        boolean result = true;
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream;
        try {
            fileOutputStream = new FileOutputStream(zipFilePath);
        } catch (FileNotFoundException e) {
            result = false;
            log.error("文件流打开失败！{}", e.getMessage(), e);
        }
        if (fileOutputStream != null) {
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            try {
                addFolderToZip("", new File(folderPath), zipOutputStream);
            } catch (IOException e) {
                result = false;
                log.error("文件压缩失败！{}", e.getMessage(), e);
            }
            try {
                zipOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("流关闭失败！{}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 将文件夹及其中的文件递归添加到压缩流中
     */
    private void addFolderToZip(String parentPath, File folder, ZipOutputStream zos) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToZip(parentPath + folder.getName() + "/", file, zos);
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);

                    // 新建Zip条目并将输入流加入到Zip包中
                    ZipEntry zipEntry = new ZipEntry(parentPath + folder.getName() + "/" + file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
        }
    }

    public String loadStringFromFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}