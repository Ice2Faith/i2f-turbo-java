package com.test;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ice2Faith
 * @date 2024/7/4 19:03
 * @desc
 */
@Slf4j
@SpringBootApplication
public class TestXxlJobApplication {

    public static void main(String[] args) {
        AgentContextHolder.THROWABLE_LISTENER.add(0, (thr) -> {
            log.error("=== " + thr.hashCode() + "@" + thr, thr);
            return false;
        });
        SpringApplication.run(TestXxlJobApplication.class, args);
    }
}
