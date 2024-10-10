package com.test.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2024/10/10 20:34
 * @desc
 */
@Component
public class TestRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                if (Math.random() < 0.3) {
                    System.out.println("err");
                    try {
                        if (Math.random() < 0.3) {
                            int val = ((int) (Math.random() * 100)) / ((int) (Math.random() * 10));
                        } else {
                            throw new RuntimeException("err");
                        }
                    } catch (Exception e) {

                    }
                } else {
                    System.out.println("info");
                }
            }
        }).start();
    }
}
