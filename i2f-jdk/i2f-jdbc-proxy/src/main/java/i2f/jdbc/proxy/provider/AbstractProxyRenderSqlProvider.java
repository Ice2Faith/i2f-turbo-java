package i2f.jdbc.proxy.provider;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.jdbc.proxy.basemapper.BaseMapper;
import i2f.jdbc.proxy.basemapper.BaseMapperSqlProvider;
import i2f.lru.LruMap;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:24
 * @desc
 */
public abstract class AbstractProxyRenderSqlProvider implements ProxyRenderSqlProvider {
    protected LruMap<String, String> scriptCache = new LruMap<>(2048);
    protected AtomicBoolean enableCache = new AtomicBoolean(true);

    @Override
    public BindSql render(String methodId, Map<String, Object> params, Method method, Object[] args) {
        BindSql preSql = preHandleRender(methodId, params, method, args);
        if (preSql != null) {
            return preSql;
        }
        String script = null;
        boolean cacheable = predicateCacheable(methodId, params, method, args);
        if (enableCache.get() && cacheable) {
            script = scriptCache.get(methodId);
        }
        if (script == null) {
            script = ReflectResolver.getAnnotationValue(method, SqlScript.class, "value");
            if (script == null || "".equals(script)) {
                script = getScript(methodId, params, method, args);
            }
        }
        if (enableCache.get() && cacheable) {
            if (script != null) {
                scriptCache.put(methodId, script);
            }
        }
        return renderSql(script, params, method, args);
    }

    private BindSql preHandleRender(String methodId, Map<String, Object> params, Method method, Object[] args) {
        if (!method.getDeclaringClass().equals(BaseMapper.class)) {
            return null;
        }
        return BaseMapperSqlProvider.parse(method, args);
    }

    public boolean predicateCacheable(String methodId, Map<String, Object> params, Method method, Object[] args) {
        return true;
    }

    public abstract String getScript(String methodId, Map<String, Object> params, Method method, Object[] args);

    public abstract BindSql renderSql(String script, Map<String, Object> params, Method method, Object args);
}
