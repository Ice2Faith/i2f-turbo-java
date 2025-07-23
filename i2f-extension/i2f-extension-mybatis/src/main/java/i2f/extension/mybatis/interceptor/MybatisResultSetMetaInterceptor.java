package i2f.extension.mybatis.interceptor;

import i2f.extension.mybatis.proxy.adapter.MybatisInterceptorProxyInvocationHandlerAdapter;
import i2f.extension.mybatis.proxy.handler.MybatisResultSetMetaProxyHandler;
import lombok.Data;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;

/**
 * @author Ice2Faith
 * @date 2025/6/30 20:58
 * @desc
 */
@Data
@Intercepts({
        @Signature(type = ResultSetHandler.class,
                method = MybatisResultSetMetaProxyHandler.METHOD_RESULT_SSET,
                args = {Statement.class}),
        @Signature(type = ResultSetHandler.class,
                method = MybatisResultSetMetaProxyHandler.METHOD_CURSOR_RESULT_SET,
                args = {Statement.class}),

})
public class MybatisResultSetMetaInterceptor extends MybatisInterceptorProxyInvocationHandlerAdapter {
    public MybatisResultSetMetaInterceptor() {
        super(new MybatisResultSetMetaProxyHandler());
    }

    public MybatisResultSetMetaInterceptor(MybatisResultSetMetaProxyHandler handler) {
        super(handler);
    }
}
