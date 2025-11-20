package i2f.jdbc.procedure.consts;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2025/1/23 10:09
 */
public interface ParamsConsts {

    // 全局部分
    String STACK_LOCK ="stack_lock";

    String CONTEXT = "context";
    String ENVIRONMENT = "env";
    String BEANS = "beans";

    String DATASOURCES = "datasources";
    String DATASOURCES_MAPPING = "datasourcesMapping";

    String GLOBAL = "global";

    String TRACE = "trace";
    String LOCATION = "location";
    String FILE = "file";
    String LINE = "line";
    String TAG = "tag";
    String ERRMSG = "errmsg";
    String NODE = "node";
    String STACK = "stack";
    String CALLS = "calls";
    String ERRORS = "errors";
    String ERROR = "error";
    String LAST_SQL="last_sql";
    String LAST_SQL_EFFECT_COUNT ="last_sql_effect_count";
    String LAST_SQL_USE_TIME ="last_sql_use_time";

    String TRACE_LOCATION = TRACE + "." + LOCATION;
    String TRACE_FILE = TRACE + "." + FILE;
    String TRACE_LINE = TRACE + "." + LINE;
    String TRACE_TAG = TRACE + "." + TAG;
    String TRACE_ERRMSG = TRACE + "." + ERRMSG;
    String TRACE_NODE = TRACE + "." + NODE;
    String TRACE_STACK = TRACE + "." + STACK;
    String TRACE_CALLS = TRACE + "." + CALLS;
    String TRACE_ERRORS = TRACE + "." + ERRORS;
    String TRACE_ERROR = TRACE + "." + ERROR;
    String TRACE_LAST_SQL = TRACE + "." + LAST_SQL;
    String TRACE_LAST_SQL_EFFECT_COUNT = TRACE + "." + LAST_SQL_EFFECT_COUNT;
    String TRACE_LAST_SQL_USE_TIME = TRACE + "." + LAST_SQL_USE_TIME;

    // 局部工具部分
    String EXECUTOR = "executor";

    // LruMap对象
    // 回话级别LRU对象
    String LRU = "lru";
    // 执行器实例级别LRU对象
    String EXECUTOR_LRU = "executorLru";
    // 静态变量级别LRU对象
    String STATIC_LRU = "staticLru";


    // 连接部分
    String CONNECTIONS = "connections";
    String DEFAULT_DATASOURCE = "primary";

    // 返回值
    String RETURN = "return";

    // 动态节点/局部节点
    String METAS = "metas";

    // 这些值，将会始终保存在上下文中，也就是每个params中都应该存在这些键值
    String[] KEEP_NAMES = {
            STACK_LOCK,

            CONTEXT,
            ENVIRONMENT,
            BEANS,

            DATASOURCES,
            DATASOURCES_MAPPING,

            CONNECTIONS,

            GLOBAL,
            METAS,

            TRACE,

            EXECUTOR,

            LRU,
            EXECUTOR_LRU,
            STATIC_LRU,
    };


    Set<String> KEEP_NAME_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(KEEP_NAMES)));
}
