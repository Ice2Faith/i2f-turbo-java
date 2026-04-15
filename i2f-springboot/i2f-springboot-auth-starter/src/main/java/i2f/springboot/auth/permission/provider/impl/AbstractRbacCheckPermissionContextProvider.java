package i2f.springboot.auth.permission.provider.impl;

import i2f.springboot.auth.permission.IRabcLoginUser;
import i2f.springboot.auth.permission.helper.RbacCheckPermissionHelper;
import i2f.springboot.auth.permission.provider.CheckPermissionContextProvider;
import org.aspectj.lang.JoinPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/3 9:56
 * @desc
 */
public abstract class AbstractRbacCheckPermissionContextProvider implements CheckPermissionContextProvider {
    public static final String CONTEXT_JP = "jp";
    public static final String CONTEXT_ARGS = "args";
    public static final String CONTEXT_ARGS_PREFIX="p";
    public static final String CONTEXT_USER = "user";
    public static final String CONTEXT_AUTH = "auth";

    @Override
    public Map<String, Object> getContext(JoinPoint joinPoint) {
        Map<String, Object> ret = new HashMap<>();
        Object user = getRbacLoginUser(joinPoint);
        ret.put(CONTEXT_JP, joinPoint);
        ret.put(CONTEXT_USER, user);
        ret.put(CONTEXT_AUTH, getRbacAuthHelper(joinPoint, user));
        ret.put(CONTEXT_ARGS, joinPoint.getArgs());
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            ret.put(CONTEXT_ARGS_PREFIX+i,joinPoint.getArgs()[i]);
        }

        return ret;
    }

    public abstract Object getRbacLoginUser(JoinPoint joinPoint);

    public Object getRbacAuthHelper(JoinPoint joinPoint, Object user) {
        return new RbacCheckPermissionHelper((IRabcLoginUser) user);
    }
}
