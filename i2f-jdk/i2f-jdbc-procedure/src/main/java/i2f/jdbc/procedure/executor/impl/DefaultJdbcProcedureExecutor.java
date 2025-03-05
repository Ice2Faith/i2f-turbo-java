package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.extension.ognl.OgnlUtil;
import i2f.extension.velocity.VelocityGenerator;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.inflater.impl.OgnlMybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/22 10:06
 */
public class DefaultJdbcProcedureExecutor extends BasicJdbcProcedureExecutor {
    @Override
    public boolean innerTest(String test, Map<String, Object> params) {
        try {
            Object obj = OgnlUtil.evaluateExpression(test, params);
            return toBoolean(obj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object innerEval(String script, Map<String, Object> params) {
        try {
            return OgnlUtil.evaluateExpression(script, params);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object innerVisit(String script, Map<String, Object> params) {
        try {
            return OgnlUtil.evaluateExpression(script, params);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String innerRender(String script, Map<String, Object> params) {
        try {
            return VelocityGenerator.render(script, params);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public BindSql resolveSqlScript(String script, Map<String, Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = OgnlMybatisMapperInflater.INSTANCE.inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }
}
