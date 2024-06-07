package i2f.springboot.jdbc.bql.components;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.jdbc.proxy.provider.AbstractProxyRenderSqlProvider;
import i2f.velocity.bindsql.VelocitySqlGenerator;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/7 17:15
 * @desc
 */
public class VelocityProxyRenderSqlProvider extends AbstractProxyRenderSqlProvider {
    private List<URL> resourceList;

    public VelocityProxyRenderSqlProvider(List<URL> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public String getScript(String methodId, Map<String, Object> params, Method method, Object[] args) {
        throw new IllegalArgumentException("missing @" + (SqlScript.class.getSimpleName()) + " on method: " + methodId);
    }

    @Override
    public BindSql renderSql(String script, Map<String, Object> params, Method method, Object args) {
        try {
            return VelocitySqlGenerator.renderSql(script, params);
        } catch (Exception e) {
            throw new IllegalArgumentException("render script error on method: " + method.getDeclaringClass() + "." + method.getName() + " of script : \n" + script);
        }
    }
}
