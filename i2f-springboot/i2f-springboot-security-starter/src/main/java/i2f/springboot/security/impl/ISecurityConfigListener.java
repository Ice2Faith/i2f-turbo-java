package i2f.springboot.security.impl;

import i2f.springboot.security.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * @author Ice2Faith
 * @date 2023/7/3 17:49
 * @desc
 */
public interface ISecurityConfigListener {
    default boolean onBeforeWebConfig(WebSecurity web, SecurityAutoConfiguration config) {
        return true;
    }

    default void onAfterWebConfig(WebSecurity web, SecurityAutoConfiguration config) {

    }

    default boolean onBeforeHttpConfig(HttpSecurity http, SecurityAutoConfiguration config) {
        return true;
    }

    default void onAfterHttpConfig(HttpSecurity http, SecurityAutoConfiguration config) {

    }
}
