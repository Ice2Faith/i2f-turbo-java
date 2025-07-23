package i2f.extension.mybatis.interceptor;

import i2f.extension.mybatis.proxy.adapter.MybatisInterceptorProxyInvocationHandlerAdapter;
import i2f.extension.mybatis.proxy.handler.MybatisPaginationProxyHandler;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author Ice2Faith
 * @date 2025/7/5 15:01
 * @desc
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = MybatisPaginationProxyHandler.QUERY, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class}),
                @Signature(type = Executor.class, method = MybatisPaginationProxyHandler.QUERY, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class,
                        CacheKey.class,
                        BoundSql.class}),
                @Signature(type = Executor.class, method = MybatisPaginationProxyHandler.QUERY_CURSOR, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class}),
        }
)
public class MybaisPaginationInterceptor extends MybatisInterceptorProxyInvocationHandlerAdapter {
    public MybaisPaginationInterceptor() {
        super(new MybatisPaginationProxyHandler());
    }

    public MybaisPaginationInterceptor(MybatisPaginationProxyHandler handler) {
        super(handler);
    }
}
