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
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final DMService dmService;


    public BookService(DMService dmService) {
        this.dmService = dmService;
    }

    public void add(String name, String author, String publisher, String description) throws SQLException {
        String sql = "INSERT INTO book (name, author, publisher, description) VALUES (?,?,?,?);";

        PreparedStatement pstmt = dmService.statement(sql);

        pstmt.setString(1, name);
        pstmt.setString(2, author);
        pstmt.setString(3, publisher);
        pstmt.setString(4, description);

        dmService.update(pstmt);
    }

    public Map<String, String> info(long id) throws SQLException {
        String sql = "SELECT name, author, publisher, description FROM book WHERE book_id=" + id;
        List<Map<String, String>> list = dmService.query(sql);
        return list != null && !list.isEmpty() ? list.get(0) : Collections.emptyMap();
    }

    public void updateName(long id, String name) throws SQLException {
        if (!StringUtils.hasText(name)) {
            logger.warn("update book name, id:{}, empty name, ignore request", id);
            return;
        }
        String sql = "UPDATE book SET name = ? WHERE book_id = ?;";
        PreparedStatement stmt = dmService.statement(sql);
        stmt.setString(1, name);
        stmt.setLong(2, id);

        dmService.update(stmt);
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM book WHERE book_id = ?;";
        PreparedStatement stmt = dmService.statement(sql);
        stmt.setLong(1, id);

        dmService.update(stmt);
    }

    public List<Map<String, String>> all() throws SQLException {
        String sql = "SELECT book_id, name, author, publisher, description FROM book";
        return dmService.query(sql);
    }

    public void updateCall(long id, String name) throws SQLException {
        if (!StringUtils.hasText(name)) {
            logger.warn("update book call, id:{}, empty name, ignore request", id);
            return;
        }
        String sql = "{ CALL bookUpdateName(?,?) }";
        CallableStatement stmt = dmService.call(sql);
        stmt.setLong(1, id);
        stmt.setString(2, name);
        dmService.call(stmt);
    }

}
