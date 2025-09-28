package i2f.jdbc.procedure.context.impl;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.context.event.JdbcProcedureMetaMapRefreshedEvent;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/3/14 21:06
 */
public class ProcedureMetaMapGrammarReporterListener
        implements XProc4jEventListener {
    @Getter
    protected final AtomicBoolean reportOnBoot = new AtomicBoolean(true);
    protected final ExecutorService reportPool = new ThreadPoolExecutor(1, 3,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    protected JdbcProcedureExecutor executor;

    public ProcedureMetaMapGrammarReporterListener(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean support(XProc4jEvent event) {
        return event instanceof JdbcProcedureMetaMapRefreshedEvent;
    }

    @Override
    public boolean handle(XProc4jEvent event) {
        JdbcProcedureMetaMapRefreshedEvent evt = (JdbcProcedureMetaMapRefreshedEvent) event;
        Map<String, ProcedureMeta> metaMap = evt.getMetaMap();
        if (reportOnBoot.getAndSet(false)) {
            GrammarReporter.reportGrammar(executor, new HashMap<>(metaMap), (msg) -> executor.logger().logWarn(msg));
        } else {
            reportPool.submit(() -> {
                GrammarReporter.reportGrammar(executor, new HashMap<>(metaMap), (msg) -> executor.logger().logWarn(msg));
            });
        }
        return false;
    }
}
