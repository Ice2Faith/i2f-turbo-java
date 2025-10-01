package i2f.jdbc.procedure.test;

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.caller.InheritContextJdbcProcedureJavaCallerAdapter;

import java.util.HashMap;

/**
 * @author Ice2Faith
 * @date 2025/10/1 20:52
 * @desc
 */
public class TestContextJavaCaller extends InheritContextJdbcProcedureJavaCallerAdapter {
    public static void main(String[] args) throws Throwable {
        new TestContextJavaCaller().exec((JdbcProcedureExecutor) null, new HashMap<>());
    }

    @Override
    public Object execute() throws Throwable {
        System.out.println("aac");
        return null;
    }
}
