package i2f.springboot.jdbc.bql.components;

import i2f.bindsql.BindSql;
import i2f.extension.velocity.bindsql.VelocityResourceSqlTemplateResolver;
import i2f.extension.velocity.bindsql.VelocitySqlGenerator;
import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.jdbc.proxy.provider.AbstractProxyRenderSqlProvider;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/7 17:15
 * @desc
 */
public class VelocityProxyRenderSqlProvider extends AbstractProxyRenderSqlProvider {
    private VelocityResourceSqlTemplateResolver templateResolver;

    public VelocityProxyRenderSqlProvider(VelocityResourceSqlTemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    @Override
    public BindSql getScript(String methodId, Map<String, Object> params, Method method, Object[] args) {
        BindSql script = templateResolver.getScript(methodId);
        if (script != null) {
            return script;
        }
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
