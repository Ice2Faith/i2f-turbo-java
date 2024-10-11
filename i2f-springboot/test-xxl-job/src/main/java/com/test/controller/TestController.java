package com.test.controller;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/10/10 23:58
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("trace")
    public Object trace() {
        String traceId = AgentContextHolder.getTraceId();
//        String traceSource = AgentContextHolder.threadTraceSource.get();
        System.out.println("traceId:" + traceId);
//        System.out.println("traceSource:" + traceSource);
        log.info("mdc-trace-id:" + MDC.get("traceId"));
        if (Math.random() < 0.3) {
            int v = 1 / 0;
        }
        return traceId;
    }

    @RequestMapping("sql")
    public Object sql() {
        jdbcTemplate.execute("create table sys_user(id varchar(300),name varchar(300))");

        String id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        System.out.println("id:" + id);

        jdbcTemplate.update("insert into sys_user(id,name) values (?,?)", id, id);

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from sys_user");
        System.out.println("list:" + list);

        jdbcTemplate.update("delete from sys_user where id=?", id);

        list = jdbcTemplate.queryForList("select * from sys_user");
        System.out.println("list:" + list);

        jdbcTemplate.execute("drop table sys_user");

        return list;
    }
}
