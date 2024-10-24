package com.example.demo.dameng.jdbc.service;

import com.example.demo.dameng.jdbc.config.DMConfig;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DMService {

    private static final Logger logger = LoggerFactory.getLogger(DMService.class);

    private final Connection conn;

    static {
       loadDriver();
    }

    @Autowired
    public DMService(DMConfig c) throws SQLException {
        Assert.notNull(c, "DMConfig");
        conn = connect(c.toJdbc(), c.getUsername(), c.getPassword());
    }

    public PreparedStatement statement(String sql) throws SQLException {
            return conn.prepareStatement(sql);
    }

    public void update(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        statement.close();
    }

    public void update(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public List<Map<String, String>> query(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Map<String, String>> result = getResult(rs);
        rs.close();
        stmt.close();
        return result;
    }

    public CallableStatement call(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    public void call(CallableStatement stmt) throws SQLException {
        stmt.execute();
        stmt.close();
    }

    private static void loadDriver() {
        try {
            logger.debug("Loading JDBC Driver...");
            String driver = "dm.jdbc.driver.DmDriver";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("Load JDBC Driver Error : ", e);
        }
    }

    private Connection connect(String jdbc, String username, String password) throws SQLException {
        try {
            logger.info("Connecting to DM Server...");
            return DriverManager.getConnection(jdbc, username, password);
        } catch (SQLException e) {
            throw new SQLException("Connect to DM Server Error : " + e.getMessage());
        }
    }

    @PreDestroy
    public void close() throws SQLException {
        try {
            logger.info("Close dameng jdbc connection");
            conn.close();
        } catch (SQLException e) {
            throw new SQLException("close connection error : " + e.getMessage());
        }
    }

    private List<Map<String, String>> getResult(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int nCols = md.getColumnCount();
        String[] keys = new String[nCols];
        for (int i = 0; i < nCols; i++) {
            keys[i] = md.getColumnLabel(i + 1).toLowerCase(Locale.ENGLISH);
        }
        List<Map<String, String>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> entry = new HashMap<>();
            for (int i = 0; i < nCols; i++) {
                String key = keys[i];
                int n = i+1;
                if ("IMAGE".equals(md.getColumnTypeName(n))) {
                    entry.put(key, Strings.EMPTY);
                } else {
                    entry.put(key, rs.getString(n));
                }
            }
            list.add(entry);
        }
        return list;
    }

}
