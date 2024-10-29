package com.example.demo.dameng.jdbc.service;

import org.apache.logging.log4j.util.Strings;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.MountableFile;

import java.sql.SQLException;
import java.util.Map;

public class DMServiceTest extends DMTestServer {

    private static DmCiService ciService;

    @BeforeClass
    public static void up() {
        DMTestServer.setUp();
        sourceInitSql();
        ciService();
    }

    @AfterClass
    public static void down() {
        DMTestServer.teardown();
    }

    private static void sourceInitSql() {

        String dst = "/tmp/citest_init.sql";
        srv.copyFileToContainer(MountableFile.forClasspathResource("init.sql"), dst);

        String cmd = "/tmp/citest_init_sql";
        String content = String.format("#!/bin/sh\n/opt/dmdbms/bin/disql %s/%s < %s", USER, PASSWORD, dst);
        int filemode = 000755; //can not be 755 in oct.
        srv.copyFileToContainer(Transferable.of(content, filemode), cmd);

        boolean ok = true;
        String msg = Strings.EMPTY;
        try {
            Container.ExecResult execResult = srv.execInContainer(cmd);
            if (execResult.getExitCode() != 0) {
                ok = false;
                msg = execResult.getStderr();
            }
            System.out.println(execResult.getStdout());
        } catch (Exception e) {
            ok = false;
            msg = e.getMessage();
        }
        if (!ok) {
            exit(128, "Source init.sql failed, cause:" + msg);
        }
    }

    private static void ciService() {
        DMService dmService = null;
        try {
            dmService = new DMService(getConfig());
        } catch (SQLException e) {
            exit(129, "connect to dm server failed, coz: " + e.getMessage());
        }
        ciService = new DmCiService(dmService);
    }

    private static void exit(int code, String msg) {
        System.err.println(msg);
        System.exit(code);
    }

    @Test
    public void info() {
        Map<String, String> info = ciService.info(1);
        Assert.assertEquals("id", "1", info.get("ci_id"));
        Assert.assertEquals("ci_key", "plugin", info.get("ci_key"));
        Assert.assertEquals("ci_val", "dameng-jdbc", info.get("ci_val"));
    }

    @Test
    public void add() {
        String key = "citest";
        String val = "add";
        ciService.add(key, val);
        long id = ciService.select(key, val);
        Assert.assertTrue("no row", id > 0);
    }

    @Test
    public void upKey() {
        int id = 2;
        Map<String, String> info = ciService.info(id);
        Assert.assertEquals("id", String.valueOf(id), info.get("ci_id"));
        Assert.assertEquals("key", "init", info.get("ci_key"));
        Assert.assertEquals("value", "updateKey", info.get("ci_val"));

        String key = "citest";
        ciService.updateKey(id, key);
        Map<String, String> info2 = ciService.info(id);
        Assert.assertEquals("id", String.valueOf(id), info2.get("ci_id"));
        Assert.assertEquals("key", key, info2.get("ci_key"));
        Assert.assertEquals("value", "updateKey", info2.get("ci_val"));
    }

    @Test
    public void upValueCall() {
        int id = 3;
        Map<String, String> info = ciService.info(id);
        Assert.assertEquals("id", String.valueOf(id), info.get("ci_id"));
        Assert.assertEquals("key", "ciUpdateValueCall", info.get("ci_key"));
        Assert.assertEquals("value", "init", info.get("ci_val"));

        String val = "citest";
        int cnt = ciService.updateValueCall(id, val);
        Map<String, String> info2 = ciService.info(id);
        Assert.assertEquals("id", String.valueOf(id), info2.get("ci_id"));
        Assert.assertEquals("key", "ciUpdateValueCall", info2.get("ci_key"));
        Assert.assertEquals("value", val, info2.get("ci_val"));
        Assert.assertEquals("count", 1, cnt);
    }

    @Test
    public void del() {
        String kv = "4del";
        long id = ciService.select(kv, kv);
        Assert.assertTrue("4del not exist", id > 0);

        ciService.delete(id);
        Map<String, String> info2 = ciService.info(id);
        Assert.assertTrue("still exist", info2 != null && info2.isEmpty());
    }

}
