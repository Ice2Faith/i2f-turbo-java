package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.parser.data.XmlNode;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:55
 * @desc
 */

public class ExecutorMethodProvider {

    public Class<?> load_class(String className) {
        return null;
    }

    public boolean is_debug() {
        return false;
    }

    public String env(String property) {
        return null;
    }

    public String env(String property, String defaultValue) {
        return null;
    }

    public Integer env_int(String property) {
        return null;
    }

    public Integer env_int(String property, Integer defaultValue) {
        return null;
    }

    public Long env_long(String property) {
        return null;
    }

    public Long env_long(String property, Long defaultValue) {
        return null;
    }

    public Double env_double(String property) {
        return null;
    }

    public Double env_double(String property, Double defaultValue) {
        return null;
    }

    public Boolean env_boolean(String property) {
        return null;
    }

    public Boolean env_boolean(String property, Boolean defaultValue) {
        return null;
    }

    public Object get_bean(String name) {
        return null;
    }

    public Object get_bean(Class<?> type) {
        return null;
    }

    public ProcedureMeta get_meta(String procedureId) {
        return null;
    }

    public String trace_location() {
        return null;
    }

    public String trace_file() {
        return null;
    }

    public int trace_line() {
        return -1;
    }

    public XmlNode trace_node() {
        return null;
    }

    public Throwable trace_error() {
        return null;
    }

    public String trace_errmsg() {
        return null;
    }

    public String tracking_comment() {
        return null;
    }

    public String tracking_comment(Integer lineNumber) {
        return null;
    }

    public String tracking_comment(boolean force) {
        return null;
    }

    public String tracking_comment(Integer lineNumber, boolean force) {
        return null;
    }

    public void log_debug(Object obj) {

    }

    public void log_info(Object obj) {

    }

    public void log_warn(Object obj) {

    }

    public void log_error(Object obj) {

    }
}
