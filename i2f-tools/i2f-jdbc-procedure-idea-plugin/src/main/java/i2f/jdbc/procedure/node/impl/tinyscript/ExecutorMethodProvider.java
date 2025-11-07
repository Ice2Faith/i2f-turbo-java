package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:55
 * @desc
 */

public interface ExecutorMethodProvider {

    Class<?> load_class(String className);

    boolean is_debug();

    String env(String property);

    String env(String property, String defaultValue);

    Integer env_int(String property);

    Integer env_int(String property, Integer defaultValue);

    Long env_long(String property);

    Long env_long(String property, Long defaultValue);

    Double env_double(String property);

    Double env_double(String property, Double defaultValue);

    Boolean env_boolean(String property);

    Boolean env_boolean(String property, Boolean defaultValue);

    Object get_bean(String name);

    Object get_bean(Class<?> type);

    ProcedureMeta get_meta(String procedureId);

    String trace_location();

    String trace_file();

    int trace_line();

    XmlNode trace_node();

    Throwable trace_error();

    String trace_errmsg();

    String tracking_comment();

    String tracking_comment(Integer lineNumber);

    String tracking_comment(boolean force);

    String tracking_comment(Integer lineNumber, boolean force);

    void log_debug(Object obj);

    void log_info(Object obj);

    void log_warn(Object obj);

    void log_error(Object obj);
}
