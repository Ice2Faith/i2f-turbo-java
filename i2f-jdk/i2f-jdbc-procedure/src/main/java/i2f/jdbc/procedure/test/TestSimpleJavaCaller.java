package i2f.jdbc.procedure.test;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/11 9:24
 */
@JdbcProcedure(
        value = "SP_SIMPLE",
        arguments = {
                "str.string",
                "return.null"
        }
)
public class TestSimpleJavaCaller implements JdbcProcedureJavaCaller {
    @Override
    public Object exec(ExecuteContext context, JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        System.out.println("hello java caller");
        return null;
    }
}
