package i2f.jdbc.proxy.xml.mybatis.provider;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.provider.AbstractProxyRenderSqlProvider;
import i2f.jdbc.proxy.xml.mybatis.MybatisMapperContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/10/9 20:28
 * @desc
 */
@Data
@NoArgsConstructor
public class MybatisMapperProxyRenderSqlProvider extends AbstractProxyRenderSqlProvider {
    protected MybatisMapperContext context = new MybatisMapperContext();

    public MybatisMapperProxyRenderSqlProvider(MybatisMapperContext context) {
        this.context = context;
    }

    @Override
    public boolean predicateCacheable(String methodId, Map<String, Object> params, Method method, Object[] args) {
        if (context.getNodeMap().containsKey(methodId)) {
            return false;
        }
        return true;
    }

    @Override
    public BindSql inflateScript(String methodId, Map<String, Object> params, Method method, Object[] args) {
        return context.inflate(methodId, params);
    }

    @Override
    public BindSql getScript(String methodId, Map<String, Object> params, Method method, Object[] args) {
        return null;
    }

    @Override
    public BindSql renderSql(String script, Map<String, Object> params, Method method, Object args) {
        return null;
    }
}
