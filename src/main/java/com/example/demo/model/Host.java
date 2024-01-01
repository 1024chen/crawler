package com.example.demo.model;

import lombok.Data;

@Data
public class Host {
    private String ip;
    private String username;
    private String password;
    private String priKey;
    private int port;
}
