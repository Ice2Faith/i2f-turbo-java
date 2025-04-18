package i2f.jdbc.procedure.context.impl;

import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.JdbcProcedureContextRefreshListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/3/14 21:06
 */
public class DefaultJdbcProcedureContextRefreshListener implements JdbcProcedureContextRefreshListener {
    @Getter
    protected final AtomicBoolean reportOnBoot =new AtomicBoolean(true);
    protected final ExecutorService reportPool= new ThreadPoolExecutor(1, 3,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    protected JdbcProcedureExecutor executor;

    public DefaultJdbcProcedureContextRefreshListener(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void accept(JdbcProcedureContext context) {

        if(reportOnBoot.getAndSet(false)){
            GrammarReporter.reportGrammar(executor, new HashMap<>(context.getMetaMap()), (msg) -> executor.logWarn( msg));
        }else {
            reportPool.submit(() -> {
                GrammarReporter.reportGrammar(executor, new HashMap<>(context.getMetaMap()), (msg) -> executor.logWarn(msg));
            });
        }
    }
}
