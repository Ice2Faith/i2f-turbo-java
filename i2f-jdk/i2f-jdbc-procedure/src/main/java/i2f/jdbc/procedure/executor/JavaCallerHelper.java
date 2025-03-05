package i2f.jdbc.procedure.executor;

import i2f.container.builder.map.MapBuilder;
import i2f.jdbc.procedure.caller.JdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.caller.impl.DefaultJdbcProcedureExecutorCaller;
import i2f.jdbc.procedure.context.ContextFunctions;
import i2f.jdbc.procedure.context.ExecuteContext;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/19 16:10
 */
@NoArgsConstructor
public class JavaCallerHelper extends ContextFunctions implements JdbcProcedureExecutorCaller {
    protected ExecuteContext context;
    protected JdbcProcedureExecutor executor;
    protected transient JdbcProcedureExecutorCaller caller;

    public JavaCallerHelper(ExecuteContext context, JdbcProcedureExecutor executor) {
        this.context = context;
        this.executor = executor;
        this.caller = new DefaultJdbcProcedureExecutorCaller(executor, context);
    }

    public <T> T visit(String expression) {
        return (T) executor.visit(expression, context.getParams());
    }

    public boolean test(String expression) {
        return executor.test(expression, context.getParams());
    }

    public <T> T eval(String expression) {
        return (T) executor.eval(expression, context.getParams());
    }

    public String render(String expression) {
        return executor.render(expression, context.getParams());
    }

    public <T> T get(String expression) {
        return visit(expression);
    }

    public void set(String expression, Object value) {
        executor.setParamsObject(context.getParams(), expression, value);
    }

    public JdbcProcedureExecutorCaller caller() {
        return this.caller;
    }

    @Override
    public <T> T invoke(String procedureId, Map<String, Object> params) {
        return this.caller.invoke(procedureId, params);
    }

    @Override
    public Map<String,Object> call(String procedureId, Map<String, Object> params) {
        return this.caller.call(procedureId, params);
    }

    @Override
    public <T> T invoke(String procedureId, List<Object> args) {
        return this.caller.invoke(procedureId,args);
    }

    @Override
    public Map<String, Object> call(String procedureId, List<Object> args) {
        return this.caller.call(procedureId,args);
    }

    public MapBuilder<String, Object, ? extends Map<String, Object>> newHashMap() {
        return new MapBuilder<>(new HashMap<>(), String.class, Object.class);
    }
}
