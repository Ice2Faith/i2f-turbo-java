package i2f.jdbc.proxy.provider.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.proxy.annotations.SqlScript;
import i2f.jdbc.proxy.provider.AbstractProxyRenderSqlProvider;
import i2f.match.regex.RegexUtil;
import i2f.reflect.vistor.Visitor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 10:39
 * @desc
 */
public class SimpleProxyRenderSqlProvider extends AbstractProxyRenderSqlProvider {
    @Override
    public BindSql getScript(String methodId, Map<String, Object> params, Method method, Object[] args) {
        throw new IllegalArgumentException("missing @" + (SqlScript.class.getSimpleName()) + " on method: " + methodId);
    }

    @Override
    public BindSql renderSql(String script, Map<String, Object> params, Method method, Object args) {
        List<Object> list = new ArrayList<>();
        script = RegexUtil.replace(script, "\\<\\?\\s*.+\\s*\\?\\>", (s, i) -> {
            s = s.substring(2, s.length() - 2);
            Visitor visitor = Visitor.visit(s, params);
            Object val = visitor.get();
            list.add(val);
            return "?";
        });
        return new BindSql(script, list);
    }
}
