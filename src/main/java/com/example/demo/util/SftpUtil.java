package com.example.demo.util;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.PreDestroy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Slf4j
@Data
@Component
public class SftpUtil {
    private Session session;

    private ChannelExec channelExec;

    private ChannelSftp channelSftp;

    private ChannelShell channelShell;

    public void loginByPassword(String username, String password, String host, Integer port) {
        try {
            if (Objects.isNull(session) || !session.isConnected()) {
                JSch jsch = new JSch();
                session = jsch.getSession(username, host, port);
                session.setPassword(password);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
//                session.setTimeout(300);
            }
            if (!session.isConnected()) {
                session.connect();
            }
        } catch (Exception e) {
            throw new RuntimeException("连接失败：" + e.getMessage());
        }
    }

    public void loginByPriKey(String username, String priKey, String host, Integer port) {
        try {
            if (Objects.isNull(session) || !session.isConnected()) {
                JSch jsch = new JSch();
                jsch.addIdentity(priKey);
                session = jsch.getSession(username, host, port);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
//                session.setTimeout(300);
            }
            if (!session.isConnected()) {
                session.connect();
            }
        } catch (Exception e) {
            throw new RuntimeException("连接失败：" + e.getMessage());
        }
    }

    public boolean sendFileToHost(String sourcePath, String targetPath) {
        boolean result = true;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            result = false;
            log.error("sftp传输建立失败,{}",e.getMessage(),e);
        }
        try {
            channelSftp.put(sourcePath, targetPath,ChannelSftp.OVERWRITE);
        } catch (SftpException e) {
            result = false;
            log.error("传输文件失败,{}",e.getMessage(),e);
        }
        return result;
    }

    /**
     * 文件或文件夹是否存在
     */
    public boolean isFileOrDirExist(String fullPath) {
        boolean isExist;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.ls(fullPath);
            isExist = true;
        } catch (Exception e) {
            isExist = false;
            log.error("远程文件或文件夹不存在 :{}", e.getMessage(),e);
        }
        return isExist;
    }

    public boolean createDir(String dirPath) {
        boolean isCreated;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.mkdir(dirPath);
            isCreated = true;
        } catch (Exception e) {
            isCreated = false;
            log.error("远程文件夹创建失败 :{}", e.getMessage(),e);
        }
        return isCreated;
    }

    public String exeSignalCommand(String command) {
        StringBuilder result = new StringBuilder();
        BufferedReader buf = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            InputStream in = channelExec.getInputStream();
            InputStream errStream = channelExec.getErrStream();
            channelExec.connect();

            buf = new BufferedReader(new InputStreamReader(in));
            String msg;
            while ((msg = buf.readLine()) != null) {
                result.append(msg);
            }

            if (StringUtils.isBlank(result.toString())) {
                buf = new BufferedReader(new InputStreamReader(errStream));
                String msgErr;
                while ((msgErr = buf.readLine()) != null) {
                    result.append(msgErr);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("关闭连接失败（执行命令）：" + e.getMessage());
        } finally {
            if (Objects.nonNull(buf)) {
                try {
                    buf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public String exeMultiCommand(List<String> commands) {
        String result = "";
        try {
            channelShell = (ChannelShell) session.openChannel("shell");

            InputStream inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (String cmd : commands) {
                printWriter.println(cmd);
            }
            printWriter.flush();
            byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String s = new String(tmp, 0, i);
                    if (s.contains("--More--")) {
                        outputStream.write((" ").getBytes());
                        outputStream.flush();
                    }
                    System.out.println(s);
                }
                if (channelShell.isClosed()) {
                    log.info("exit-status:{}", channelShell.getExitStatus());
                    break;
                }
                //间隔0.5后再执行
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 完整路径
     */
    public String ls(String fullPath) {
        StringBuilder sb = new StringBuilder();
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            Vector ls = channelSftp.ls(fullPath);
            Iterator iterator = ls.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                sb.append(next);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sb.toString();
    }

    private void close() {
        if (Objects.nonNull(channelExec)) {
            channelExec.disconnect();
        }
        if (Objects.nonNull(channelSftp)) {
            channelSftp.disconnect();
        }
        if (Objects.nonNull(channelShell)) {
            channelShell.disconnect();
        }
    }

    @PreDestroy
    public void closeAll() {
        this.close();
        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }
}
