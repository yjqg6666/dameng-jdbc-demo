package com.example.demo.dameng.jdbc.controller;

import com.example.demo.dameng.jdbc.config.ActionResult;
import com.example.demo.dameng.jdbc.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
@SuppressWarnings("unused")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService svc;

    @Autowired
    BookController(BookService svc) {
        this.svc = svc;
    }

    @GetMapping(value = "/add")
    public ResponseEntity<ActionResult> add(@RequestParam String name, @RequestParam String author, @RequestParam String publisher, @RequestParam(name = "desc") String description) {
        try {
            svc.add(name, author, publisher, description);
            return ResponseEntity.ok(ActionResult.OK);
        } catch (SQLException e) {
            logger.error("Add book error, name:{}, author:{}, pub:{}, desc:{}", name, author, publisher, description, e);
            return ResponseEntity.ok(ActionResult.FAILED);
        }
    }

    @GetMapping(value = "/info/{id:\\d+}")
    public ResponseEntity<Map<String, String>> info(@PathVariable("id") long id) {
        try {
            Map<String, String> info = svc.info(id);
            return ResponseEntity.ok(info);
        } catch (SQLException e) {
            logger.error("Info book id:{} error", id, e);
            return ResponseEntity.ok(Collections.emptyMap());
        }
    }

    @GetMapping(value = "/update/{id:\\d+}")
    public ResponseEntity<ActionResult> update(@PathVariable("id") long id, @RequestParam String name) {
        try {
            svc.updateName(id, name);
            return ResponseEntity.ok(ActionResult.OK);
        } catch (SQLException e) {
            logger.error("Update book name, id:{}, name:{}, error", id, name, e);
            return ResponseEntity.ok(ActionResult.FAILED);
        }
    }

    @GetMapping(value = "/upcall/{id:\\d+}")
    public ResponseEntity<ActionResult> updateCall(@PathVariable("id") long id, @RequestParam String name) {
        try {
            svc.updateCall(id, name);
            return ResponseEntity.ok(ActionResult.OK);
        } catch (SQLException e) {
            logger.error("Update book name(by db call), id:{}, name:{}, error", id, name, e);
            return ResponseEntity.ok(ActionResult.FAILED);
        }
    }

    @GetMapping(value = "/del/{id:\\d+}")
    public ResponseEntity<ActionResult> del(@PathVariable("id") long id) {
        try {
            svc.delete(id);
            return ResponseEntity.ok(ActionResult.OK);
        } catch (SQLException e) {
            logger.error("Delete book, id:{}, error", id, e);
            return ResponseEntity.ok(ActionResult.FAILED);
        }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<Map<String, String>>> list() {
        try {
            List<Map<String, String>> all = svc.all();
            return ResponseEntity.ok(all);
        } catch (SQLException e) {
            logger.error("List book error,", e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

}
