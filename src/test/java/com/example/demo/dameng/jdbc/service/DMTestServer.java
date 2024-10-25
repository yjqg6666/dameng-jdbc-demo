package com.example.demo.dameng.jdbc.service;

import com.example.demo.dameng.jdbc.config.DMConfig;
import org.junit.ClassRule;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class DMTestServer {

    protected static final String USER = "SYSDBA";
    protected static final String PASSWORD = "SYSDBA001";
    private static final int PORT = 5236;
    @ClassRule
    public static GenericContainer<?> srv = new GenericContainer(DockerImageName.parse("yjqg6666/dameng-server:v8-20240715"))
            .withExposedPorts(PORT)
            .withPrivilegedMode(true)
            .withReuse(false)
            .withEnv("LD_LIBRARY_PATH", "/opt/dmdbms/bin")
            .withEnv("UNICODE_FLAG", "1")
            .withEnv("INSTANCE_NAME", "DM8CI")
            .waitingFor(Wait.forLogMessage(".*SYSTEM IS READY.*\\n", 1));
    private static DMConfig config;

    protected static void setUp() {
        System.out.println("===== dm server info start=====");
        System.out.println("Host:" + srv.getHost());
        System.out.println("Port:" + srv.getMappedPort(PORT));
        System.out.println("===== dm server info end=====");
        //srv.copyFileToContainer();
        try {
            Container.ExecResult date = srv.execInContainer("date");
            System.out.println(date);
        } catch (Exception e) {
            System.out.println(e);
        }
        config = new DMConfig().setHost(srv.getHost())
                .setPort(String.valueOf(srv.getMappedPort(PORT)))
                .setUsername(USER)
                .setPassword(PASSWORD);
    }

    protected static void teardown() {
        if (srv != null && srv.isRunning()) {
            srv.stop();
        }
    }

    protected static String getJdbc() {
        return "jdbc:dm://" + srv.getHost() + ":" + srv.getMappedPort(PORT);
    }

    protected static DMConfig getConfig() {
        return config;
    }

}
