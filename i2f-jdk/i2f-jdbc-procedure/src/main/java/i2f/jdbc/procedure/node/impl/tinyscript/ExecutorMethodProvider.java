package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:55
 * @desc
 */
@Data
@NoArgsConstructor
public class ExecutorMethodProvider {
    protected JdbcProcedureExecutor executor;

    public ExecutorMethodProvider(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    public Class<?> load_class(String className) {
        return executor.loadClass(className);
    }

    public boolean is_debug() {
        return executor.isDebug();
    }

    public String env(String property) {
        return executor.env(property);
    }

    public String env(String property, String defaultValue) {
        return executor.env(property, defaultValue);
    }

    public Integer env_int(String property) {
        return executor.envAs(property, Integer.class, null);
    }

    public Integer env_int(String property, Integer defaultValue) {
        return executor.envAs(property, Integer.class, defaultValue);
    }

    public Long env_long(String property) {
        return executor.envAs(property, Long.class, null);
    }

    public Long env_long(String property, Long defaultValue) {
        return executor.envAs(property, Long.class, defaultValue);
    }

    public Double env_double(String property) {
        return executor.envAs(property, Double.class, null);
    }

    public Double env_double(String property, Double defaultValue) {
        return executor.envAs(property, Double.class, defaultValue);
    }

    public Boolean env_boolean(String property) {
        return executor.envAs(property, Boolean.class, null);
    }

    public Boolean env_boolean(String property, Boolean defaultValue) {
        return executor.envAs(property, Boolean.class, defaultValue);
    }

    public Object get_bean(String name) {
        return executor.getBean(name);
    }

    public Object get_bean(Class<?> type) {
        return executor.getBean(type);
    }

    public ProcedureMeta get_meta(String procedureId) {
        return executor.getMeta(procedureId);
    }

    public String trace_location() {
        return ContextHolder.traceLocation();
    }

    public String trace_file() {
        return ContextHolder.TRACE_LOCATION.get();
    }

    public int trace_line() {
        return ContextHolder.TRACE_LINE.get();
    }

    public XmlNode trace_node() {
        return ContextHolder.TRACE_NODE.get();
    }

    public Throwable trace_error() {
        return ContextHolder.TRACE_ERROR.get();
    }

    public String trace_errmsg() {
        return ContextHolder.TRACE_ERRMSG.get();
    }

    public String tracking_comment() {
        return tracking_comment(false);
    }

    public String tracking_comment(Integer lineNumber) {
        return tracking_comment(lineNumber, false);
    }

    public String tracking_comment(boolean force) {
        return tracking_comment(null, force);
    }

    public String tracking_comment(Integer lineNumber, boolean force) {
        if (!force) {
            if (!executor.isDebug()) {
                return "";
            }
        }
        return AbstractExecutorNode.getTrackingComment(ContextHolder.TRACE_NODE.get(), lineNumber);
    }

    public void log_debug(Object obj) {
        executor.logger().logDebug(obj);
    }

    public void log_info(Object obj) {
        executor.logger().logInfo(obj);
    }

    public void log_warn(Object obj) {
        executor.logger().logWarn(obj);
    }

    public void log_error(Object obj) {
        executor.logger().logError(obj);
    }
}
