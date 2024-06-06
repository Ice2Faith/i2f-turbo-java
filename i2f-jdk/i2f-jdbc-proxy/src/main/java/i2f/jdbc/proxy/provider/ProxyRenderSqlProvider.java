package i2f.jdbc.proxy.provider;

import i2f.bindsql.BindSql;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:10
 * @desc
 */
public interface ProxyRenderSqlProvider {
    BindSql render(String methodId, Map<String, Object> params, Method method, Object[] args);
}
