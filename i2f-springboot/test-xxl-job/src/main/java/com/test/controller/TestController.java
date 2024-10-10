package com.test.controller;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2024/10/10 23:58
 * @desc
 */
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("trace")
    public Object trace() {
        String traceId = AgentContextHolder.getTraceId();
        System.out.println("traceId:" + traceId);
        return traceId;
    }
}
