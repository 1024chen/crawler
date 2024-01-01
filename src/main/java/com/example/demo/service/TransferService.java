package com.example.demo.service;

import com.example.demo.model.Host;
import com.example.demo.model.Sync;
import com.example.demo.util.DirAndFileUtil;
import com.example.demo.util.SftpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransferService {

    @Autowired
    private SftpUtil sftpUtil;

    @Autowired
    private Sync sync;

    @Autowired
    private DirAndFileUtil dirAndFileUtil;

    public boolean TransDirectory() {
        boolean result = true;
        if (dirAndFileUtil.zipFolder(sync.getSourcePath(), sync.getZipPath())) {
            for (Host host : sync.getHost()) {
                host.setPort(22);
                if (!sync.isUseKey()){
                    sftpUtil.loginByPassword(host.getUsername(), host.getPassword(), host.getIp(), host.getPort());
                }else {
                    String path = host.getPriKey();
                    host.setPriKey(dirAndFileUtil.loadStringFromFile(path));
                    sftpUtil.loginByPriKey(host.getUsername(), host.getPriKey(), host.getIp(), host.getPort());
                    host.setPriKey(path);
                }
                String[] zipSplit = sync.getZipPath().split("/");
                String targetZip = sync.getTargetPath() + "/" + zipSplit[zipSplit.length - 1];
                if (!sftpUtil.isFileOrDirExist(sync.getTargetPath())){
                    sftpUtil.createDir(sync.getTargetPath());
                }
                if (sftpUtil.sendFileToHost(sync.getZipPath(), targetZip)) {
                    String command = "unzip -o " + targetZip;
                    if (sftpUtil.exeSignalCommand(command).contains("unzip:")){
                        result = false;
                        log.error("远程解压工具不存在！解压失败");
                    }
                }
                sftpUtil.closeAll();
            }
        } else {
            result = false;
        }
        return result;
    }
}
