package com.test;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Ice2Faith
 * @date 2024/7/4 19:03
 * @desc
 */
@SpringBootApplication
public class TestXxlJobApplication {

    public static void main(String[] args) {
        AgentContextHolder.THROWABLE_LISTENER.add((thr) -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            thr.printStackTrace(ps);
            System.err.println("=============================\n====" + thr.hashCode() + "@" + new String(bos.toByteArray()));
            return true;
        });
        SpringApplication.run(TestXxlJobApplication.class, args);
    }
}
