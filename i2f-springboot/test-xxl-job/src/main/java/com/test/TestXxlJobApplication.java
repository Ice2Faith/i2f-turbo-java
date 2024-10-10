package com.test;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Ice2Faith
 * @date 2024/7/4 19:03
 * @desc
 */
@Slf4j
@SpringBootApplication
public class TestXxlJobApplication {

    public static void main(String[] args) {
        AgentContextHolder.THROWABLE_LISTENER.add((thr) -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            thr.printStackTrace(ps);
            log.warn("=============================\n====" + thr.hashCode() + "@" + new String(bos.toByteArray()));
            return true;
        });
        SpringApplication.run(TestXxlJobApplication.class, args);
    }
}
