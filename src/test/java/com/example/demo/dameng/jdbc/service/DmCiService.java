package com.example.demo.dameng.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DmCiService {

    private static final Logger logger = LoggerFactory.getLogger(DmCiService.class);

    private final DMService dmService;


    public DmCiService(DMService dmService) {
        this.dmService = dmService;
    }

    public void add(String key, String value) {
        String sql = "INSERT INTO citest (key, value) VALUES (?,?);";

        try {
            PreparedStatement pstmt = dmService.statement(sql);
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            dmService.update(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long select(String key, String value) {
        String sql = "SELECT ci_id FROM citest WHERE key=? and value=?";
        try {
            PreparedStatement pstmt = dmService.statement(sql);
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            List<Map<String, String>> list = dmService.query(pstmt);
            pstmt.close();
            return list != null && !list.isEmpty() ? Long.parseLong(list.get(0).get("ci_id")) : -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> info(long id) {
        String sql = "SELECT * FROM citest WHERE ci_id=" + id;
        try {
            List<Map<String, String>> list = dmService.query(sql);
            return list != null && !list.isEmpty() ? list.get(0) : Collections.emptyMap();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateKey(long id, String key) {
        if (!StringUtils.hasText(key)) {
            logger.warn("update key, id:{}, empty key, ignore request", id);
            return;
        }
        String sql = "UPDATE citest SET key = ? WHERE ci_id = ?;";
        try {
            PreparedStatement stmt = dmService.statement(sql);
            stmt.setString(1, key);
            stmt.setLong(2, id);

            dmService.update(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM citest WHERE ci_id = ?;";
        try {
            PreparedStatement stmt = dmService.statement(sql);
            stmt.setLong(1, id);

            dmService.update(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> all() {
        String sql = "SELECT * FROM citest";
        try {
            return dmService.query(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateValueCall(long id, String value) {
        if (!StringUtils.hasText(value)) {
            logger.warn("update value call, id:{}, empty value, ignore request", id);
            return;
        }
        String sql = "{ CALL ciUpdateValue(?,?) }";
        try {
            CallableStatement stmt = dmService.call(sql);
            stmt.setLong(1, id);
            stmt.setString(2, value);
            dmService.call(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
