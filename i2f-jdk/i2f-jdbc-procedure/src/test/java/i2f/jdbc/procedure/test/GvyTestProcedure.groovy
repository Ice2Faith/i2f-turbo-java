package i2f.jdbc.procedure.test

import i2f.jdbc.procedure.executor.JdbcProcedureExecutor
import i2f.jdbc.procedure.executor.caller.InheritContextJdbcProcedureJavaCallerAdapter

/**
 * @author Ice2Faith
 * @date 2025/10/1 19:55
 * @desc
 */
class GvyTestProcedure extends InheritContextJdbcProcedureJavaCallerAdapter {
    static void main(String[] args) {
        new GvyTestProcedure().exec((JdbcProcedureExecutor) null, new HashMap<>())
    }

    @Override
    Object execute() throws Throwable {
        params.now = now()
        params.procedureId = uuid()
        params.nowStr = date_format(params.now, "yyyy-MM-dd HH:mm:ss")
        println("nowStr:${params.nowStr}, procedureId:${params.procedureId}")

        params.now1 = new Date()
        println("now1:${params.now1}")
        exec
        return null
    }
}
