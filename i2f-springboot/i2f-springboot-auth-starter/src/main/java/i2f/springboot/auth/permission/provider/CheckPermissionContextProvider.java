package i2f.springboot.auth.permission.provider;

import org.aspectj.lang.JoinPoint;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/3 9:48
 * @desc
 */
@FunctionalInterface
public interface CheckPermissionContextProvider {
    Map<String, Object> getContext(JoinPoint joinPoint);
}
