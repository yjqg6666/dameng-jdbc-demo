package com.example.demo.dameng.jdbc.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@SpringBootTest
public class ContainerTest {

    @Rule
    public GenericContainer srv = new GenericContainer(DockerImageName.parse("testcontainers/helloworld:1.1.0"))
            .withExposedPorts(8080, 8081)
            .withEnv("DELAY_START_MSEC", "2000")
            .waitingFor(Wait.forListeningPort());

    @Before
    public void setUp() {
        String host = srv.getHost();
        Integer port = srv.getFirstMappedPort();

        // Now we have an address and port for container, no matter where it is running
        System.out.println("Host:" + host);
        System.out.println("Port:" + port);
    }

    @Test
    public void info() {
        try {
            String uri = "http://" + srv.getHost() + ":" + srv.getFirstMappedPort() + "/ping";
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            int responseCode = con.getResponseCode();
            Assert.assertEquals("status code not 200", 200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String body = in.lines().collect(Collectors.joining());
            Assert.assertEquals("body not PONG", "PONG", body);
        } catch (Exception e) {
            Assert.fail("test error, cause: " + e.getMessage());
        }
    }
}
