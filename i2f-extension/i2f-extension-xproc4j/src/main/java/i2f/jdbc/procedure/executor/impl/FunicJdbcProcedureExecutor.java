package i2f.jdbc.procedure.executor.impl;

import i2f.context.std.INamingContext;
import i2f.environment.std.IEnvironment;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

/**
 * @author Ice2Faith
 * @date 2026/6/18 15:52
 * @desc
 */
public class FunicJdbcProcedureExecutor extends DefaultJdbcProcedureExecutor {
    public FunicJdbcProcedureExecutor() {
    }

    public FunicJdbcProcedureExecutor(JdbcProcedureContext context) {
        super(context);
    }

    public FunicJdbcProcedureExecutor(JdbcProcedureContext context, IEnvironment environment, INamingContext namingContext) {
        super(context, environment, namingContext);
    }


    @Override
    public boolean innerTest(String test, Object params) {
        try {
            Object obj = Funic.script(test, params);
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
            return Funic.script(script, params);
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
            return Funic.script(script, params);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }
}
