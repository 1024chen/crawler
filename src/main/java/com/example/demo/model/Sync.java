package com.example.demo.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sync")
public class Sync {
    private boolean useKey;
    private String sourcePath;
    private String targetPath;
    private String zipPath;
    private List<Host> host;
}
