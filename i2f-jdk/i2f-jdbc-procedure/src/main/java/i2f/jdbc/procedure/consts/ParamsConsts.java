package i2f.jdbc.procedure.consts;

/**
 * @author Ice2Faith
 * @date 2025/1/23 10:09
 */
public interface ParamsConsts {

    // 全局部分
    String CONTEXT = "context";
    String ENVIRONMENT = "env";
    String BEANS = "beans";

    String DATASOURCES = "datasources";
    String DATASOURCES_MAPPING="datasourcesMapping";

    String GLOBAL = "global";

    String TRACE="trace";
    String LOCATION="location";
    String LINE="line";
    String ERRMSG ="errmsg";
    String NODE="node";

    String TRACE_LOCATION=TRACE+"."+LOCATION;
    String TRACE_LINE=TRACE+"."+LINE;
    String TRACE_ERRMSG =TRACE+"."+ ERRMSG;
    String TRACE_NODE=TRACE+"."+NODE;

    // 局部工具部分
    String EXECUTOR="executor";

    // LruMap对象
    String LRU ="lru";


    // 连接部分
    String CONNECTIONS = "connections";
    String DEFAULT_DATASOURCE = "primary";

    // 返回值
    String RETURN="return";
}
