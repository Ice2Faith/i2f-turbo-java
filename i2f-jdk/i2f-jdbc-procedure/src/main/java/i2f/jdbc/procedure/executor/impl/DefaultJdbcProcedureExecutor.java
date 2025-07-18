package i2f.jdbc.procedure.executor.impl;

import i2f.context.std.INamingContext;
import i2f.environment.std.IEnvironment;
import i2f.extension.ognl.OgnlUtil;
import i2f.extension.velocity.VelocityGenerator;
import i2f.extension.velocity.directives.common.ScriptDirective;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.inflater.impl.OgnlMybatisMapperInflater;
import i2f.lru.LruList;
import i2f.reflect.ReflectResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/22 10:06
 */
public class DefaultJdbcProcedureExecutor extends BasicJdbcProcedureExecutor {
    protected volatile MybatisMapperInflater mybatisMapperInflater;
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
            } else {
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
            } else {
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
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public String innerRender(String script, Object params) {
        try {
            Map<String, Object> renderMap = null;
            if (params instanceof Map) {
                renderMap = (Map<String, Object>) params;
            } else {
                renderMap = ReflectResolver.beanVirtual2map(params, new HashMap<>());
            }

            JdbcProcedureExecutor executor = this;
            LruList<EvalScriptProvider> providers = getEvalScriptProviders();
            List<ScriptDirective.VelocityScriptProvider> adapters = new ArrayList<>();
            for (EvalScriptProvider item : providers) {
                adapters.add(new ScriptDirective.VelocityScriptProvider() {
                    @Override
                    public boolean support(String lang) {
                        return item.support(lang);
                    }

                    @Override
                    public Object eval(String script, Object params) {
                        Map<String, Object> ctx = new HashMap<>();
                        if (params instanceof Map) {
                            ctx = (Map<String, Object>) params;
                        } else {
                            ctx.put("root", params);
                        }
                        return item.eval(script, ctx, executor);
                    }
                });
            }
            ScriptDirective.THREAD_PROVIDERS.set(adapters);
            return VelocityGenerator.render(script, renderMap);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public MybatisMapperInflater getMybatisMapperInflater() {
        if (mybatisMapperInflater != null) {
            return mybatisMapperInflater;
        }
        synchronized (this) {
            JdbcProcedureExecutor executor = this;
            mybatisMapperInflater = new OgnlMybatisMapperInflater() {
                @Override
                public Object runScript(String script, String lang, Map<String, Object> params, MybatisMapperNode node) {
                    if ("ognl".equalsIgnoreCase(lang)) {
                        try {
                            Object obj = OgnlUtil.evaluateExpression(script, params);
                            return obj;
                        } catch (Exception e) {

                        }
                    }
                    if ("visit".equalsIgnoreCase(lang)) {
                        try {
                            Object obj = executor.visit(script, params);
                            return obj;
                        } catch (Exception e) {

                        }
                    }
                    if ("test".equalsIgnoreCase(lang)) {
                        try {
                            Object obj = executor.test(script, params);
                            return obj;
                        } catch (Exception e) {

                        }
                    }
                    if ("render".equalsIgnoreCase(lang)) {
                        try {
                            Object obj = executor.render(script, params);
                            return obj;
                        } catch (Exception e) {

                        }
                    }
                    EvalScriptProvider provider = executor.getEvalScriptProviders().touchFirst(e -> e.support(lang));
                    if (provider == null) {
                        throw new ThrowSignalException("eval script provider not found for lang=" + lang);
                    }
                    executor.prepareParams(params);
                    Object ret = provider.eval(script, params, executor);
                    return ret;
                }
            };
        }
        return mybatisMapperInflater;
    }

}
