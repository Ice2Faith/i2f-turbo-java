package com.test.controller;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2024/10/10 23:58
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("trace")
    public Object trace() {
        String traceId = AgentContextHolder.getTraceId();
        String traceSource = AgentContextHolder.threadTraceSource.get();
        System.out.println("traceId:" + traceId);
        System.out.println("traceSource:" + traceSource);
        log.info("mdc-trace-id:" + MDC.get("traceId"));
        if (Math.random() < 0.3) {
            int v = 1 / 0;
        }
        return traceId;
    }
}
