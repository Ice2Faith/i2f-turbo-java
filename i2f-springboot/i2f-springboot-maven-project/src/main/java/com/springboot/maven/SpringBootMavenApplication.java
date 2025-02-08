package com.springboot.maven;

import com.springboot.maven.application.WarBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Ice2Faith
 * @date 2024/7/23 15:20
 * @desc
 */
@SpringBootApplication
public class SpringBootMavenApplication extends WarBootApplication {

    public static void main(String[] args) {
        startup(SpringBootMavenApplication.class, args);
    }
}
