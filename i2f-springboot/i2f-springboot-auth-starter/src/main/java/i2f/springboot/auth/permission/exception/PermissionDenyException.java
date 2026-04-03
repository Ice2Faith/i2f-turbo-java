package i2f.springboot.auth.permission.exception;

import org.aspectj.lang.JoinPoint;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/3 9:52
 * @desc
 */
public class PermissionDenyException extends RuntimeException {
    protected JoinPoint joinPoint;
    protected Map<String, Object> context;

    public PermissionDenyException() {
    }

    public PermissionDenyException(String message) {
        super(message);
    }

    public PermissionDenyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDenyException(Throwable cause) {
        super(cause);
    }

    public PermissionDenyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JoinPoint getJoinPoint() {
        return joinPoint;
    }

    public PermissionDenyException joinPoint(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        return this;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public PermissionDenyException context(Map<String, Object> context) {
        this.context = context;
        return this;
    }
}
