package i2f.jdbc.procedure.executor.caller;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.reflect.ReflectResolver;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 适用于子类继承实现
 * 此父类使用反射进行初始化内部的 supplier
 * 从而实现自动创建一个实现类的副本，保证了上下文的隔离
 *
 * @author Ice2Faith
 * @date 2025/10/1 21:37
 * @desc
 */
public abstract class InheritContextJdbcProcedureJavaCallerAdapter extends AbstractContextJdbcProcedureJavaCaller implements JdbcProcedureJavaCaller {
    private Supplier<ContextJdbcProcedureJavaCaller> supplier;

    {
        this.supplier = getSupplier();
    }

    public Supplier<ContextJdbcProcedureJavaCaller> getSupplier() {
        Class<?> clazz = getClass();
        return () -> {
            try {
                Object obj = ReflectResolver.getInstance(clazz);
                return (InheritContextJdbcProcedureJavaCallerAdapter) obj;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        };
    }

    @Override
    public final Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        ContextJdbcProcedureJavaCaller caller = supplier.get();
        caller.init(executor, params);
        return caller.execute();
    }
}
