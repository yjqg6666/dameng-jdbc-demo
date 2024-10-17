package com.example.demo.dameng.jdbc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@Data
public class DMConfig {

    @Value("${dmHost:\"127.0.0.1\"}")
    private String host;

    @Value("${dmPort:\"5236\"}")
    private String port;

    @Value("${dmUser:\"SYSDBA\"}")
    private String username;

    @Value("${dmPassword:\"SYSDBA\"}")
    private String password;
}
