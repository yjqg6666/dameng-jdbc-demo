package com.example.demo.dameng.jdbc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("unused")
public class DMConfig {

    @Value("${dmHost:\"127.0.0.1\"}")
    private String host;

    @Value("${dmPort:\"5236\"}")
    private String port;

    @Value("${dmUser:\"SYSDBA\"}")
    private String username;

    @Value("${dmPassword:\"SYSDBA\"}")
    private String password;

    public String getHost() {
        return host;
    }

    public DMConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public DMConfig setPort(String port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DMConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DMConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "DMConfig{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
