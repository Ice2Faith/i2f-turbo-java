package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.context.std.INamingContext;
import i2f.environment.std.IEnvironment;
import i2f.extension.ognl.OgnlUtil;
import i2f.extension.velocity.VelocityGenerator;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.impl.OgnlMybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;
import i2f.reflect.ReflectResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/22 10:06
 */
public class DefaultJdbcProcedureExecutor extends BasicJdbcProcedureExecutor {
    public DefaultJdbcProcedureExecutor() {
    }

    public DefaultJdbcProcedureExecutor(JdbcProcedureContext context) {
        super(context);
    }

    public DefaultJdbcProcedureExecutor(JdbcProcedureContext context, IEnvironment environment, INamingContext namingContext) {
        super(context, environment, namingContext);
    }

    @Override
    public boolean innerTest(String test, Object params) {
        try {
            Object obj = OgnlUtil.evaluateExpression(test, params);
            return toBoolean(obj);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Object innerEval(String script, Object params) {
        try {
            return OgnlUtil.evaluateExpression(script, params);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Object innerVisit(String script, Object params) {
        try {
            return OgnlUtil.evaluateExpression(script, params);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public String innerRender(String script, Object params) {
        try {
            Map<String,Object> renderMap=null;
            if(params instanceof Map){
                renderMap=(Map<String,Object>)params;
            }else{
                renderMap = ReflectResolver.beanVirtual2map(params, new HashMap<>());
            }
            return VelocityGenerator.render(script, renderMap);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            }else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public BindSql resolveSqlScript(String script, Map<String, Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = OgnlMybatisMapperInflater.INSTANCE.inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }
}
