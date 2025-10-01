package i2f.jdbc.procedure.executor.caller;

/**
 * @author Ice2Faith
 * @date 2025/10/1 22:04
 * @desc
 */
public class SampleContextJdbcProcedureJavaCaller extends AbstractContextJdbcProcedureJavaCaller {
    @Override
    public Object execute() throws Throwable {
        print(1, true, 1.123);
        return now();
    }
}
