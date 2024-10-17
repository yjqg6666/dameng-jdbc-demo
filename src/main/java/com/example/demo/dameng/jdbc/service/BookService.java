package com.example.demo.dameng.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
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
        String sql = "INSERT INTO production.product(name, author, publisher, publishtime,"
                + "product_subcategoryid, productno, satetystocklevel, originalprice, nowprice, discount,"
                + "description, photo, type, papertotal, wordtotal, sellstarttime, sellendtime) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement pstmt = dmService.statement(sql);

        pstmt.setString(1, name);
        pstmt.setString(2, author);
        pstmt.setString(3, publisher);
        pstmt.setDate(4, Date.valueOf("2005-04-01"));
        pstmt.setInt(5, 4);
        pstmt.setString(6, String.valueOf(System.currentTimeMillis()));
        pstmt.setInt(7, 10);
        pstmt.setBigDecimal(8, new BigDecimal("19.0000"));
        pstmt.setBigDecimal(9, new BigDecimal("15.2000"));
        pstmt.setBigDecimal(10, new BigDecimal("8.0"));
        pstmt.setString(11, description);
        pstmt.setNull(12, java.sql.Types.BINARY);
        pstmt.setString(13, "25");
        pstmt.setInt(14, 943);
        pstmt.setInt(15, 93000);
        pstmt.setDate(16, Date.valueOf("2006-03-20"));
        pstmt.setDate(17, Date.valueOf("1900-01-01"));

        dmService.update(pstmt);
    }

    public Map<String, String> info(long id) throws SQLException {
        String sql = "SELECT name, author, publisher, description FROM production.product WHERE productid=" + id;
        List<Map<String, String>> list = dmService.query(sql);
        return list != null && !list.isEmpty() ? list.get(0) : Collections.emptyMap();
    }

    public void updateName(long id, String name) throws SQLException {
        if (!StringUtils.hasText(name)) {
            logger.warn("update book name, id:{}, empty name, ignore request", id);
            return;
        }
        String sql = "UPDATE production.product SET name = ? WHERE productid = ?;";
        PreparedStatement stmt = dmService.statement(sql);
        stmt.setString(1, name);
        stmt.setLong(2, id);

        dmService.update(stmt);
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM production.product WHERE productid = ?;";
        PreparedStatement stmt = dmService.statement(sql);
        stmt.setLong(1, id);

        dmService.update(stmt);
    }

    public List<Map<String, String>> all() throws SQLException {
        String sql = "SELECT productid,name,author,publisher FROM production.product";
        return dmService.query(sql);
    }

    public void updateCall(long id, String name) throws SQLException {
        if (!StringUtils.hasText(name)) {
            logger.warn("update book call, id:{}, empty name, ignore request", id);
            return;
        }
        String sql = "{ CALL production.updateProduct(?,?) }";
        CallableStatement stmt = dmService.call(sql);
        stmt.setLong(1, id);
        stmt.setString(2, name);
        dmService.call(stmt);
    }

}
