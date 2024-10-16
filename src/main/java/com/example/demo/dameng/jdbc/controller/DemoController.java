package com.example.demo.dameng.jdbc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @GetMapping(value = "/code/{code:\\d+}")
    public void code(@PathVariable("code") Integer code, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(code);
    }

    @GetMapping(value = "/stack/")
    public void stack(HttpServletResponse httpServletResponse) {
        try {
            throw new RuntimeException("User triggered exception.");
        } catch (RuntimeException e) {
            logger.error("Got error at " + System.currentTimeMillis(), e);
        }
    }

    @GetMapping(value = "/hello/{name:[a-zA-Z\\d]+}")
    public void hello(@PathVariable("name") String name, HttpServletResponse httpServletResponse) {
        logger.info("Request user, name:{}", name);
        try {
            httpServletResponse.getWriter().write("Hello, " + name + "!");
        } catch (IOException e) {
            logger.error("Write response error", e);
        }
    }

    @GetMapping(value = "/sleep/{time:\\d+}")
    public String sleep(@PathVariable("time") long time) throws InterruptedException {
        Thread.sleep(time);
        return time + "@" + System.currentTimeMillis();
    }

    @RequestMapping(value = "/log/{id:\\d+}", method = {RequestMethod.GET, RequestMethod.POST})
    public String logger(@PathVariable("id") Long id, HttpServletRequest request) {
         logger.info("Logger request, id:{}, request:{}", id, request);
        return String.valueOf(id) + "\n";
    }

}
