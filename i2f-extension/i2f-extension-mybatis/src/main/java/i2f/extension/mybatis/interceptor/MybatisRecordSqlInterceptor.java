package i2f.extension.mybatis.interceptor;

import i2f.extension.mybatis.proxy.adapter.MybatisInterceptorProxyInvocationHandlerAdapter;
import i2f.extension.mybatis.proxy.handler.MybatisRecordSqlProxyHandler;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 提供一个可以用于记录SQL执行语句的拦截器
 * 在springboot中使用时，编写一个bean
 * 继承本类，并根据需要，重写recordSql*类方法
 * 实现自己的记录SQL日志
 * 同时，需要将本类上的 @Intercepts 注解完全加到继承的子类上
 * 同时继承的子类需要注册为bean，也就是添加 @Component 注解
 * --------------------------------
 * 当然，本类也提供了一些基础的能力
 * 也可以将本类直接配置为一个bean
 * 也就是在 @Configuration 修饰的配置类中
 * 使用 @Bean 修饰一个公共方法返回本类实例
 * ---------------------------------
 * 在平常开发中获取刚才执行的SQL
 * 可以借助 ThreadLocal 的特性使用，借助和 PageHelper 相同的思路
 * 先调用 MybatisHolder.startRecordSql() 然后开始记录执行的SQL列表
 * 然后，在需要的地方获取当前 ThreadLocal 中的最后一条SQL MybatisHolder.getLastSqlAndClear()
 * 建议在获取完之后进行 clear 清空, 特别是在大量SQL执行的情况，避免导致OOM
 * 最后，在使用完之后，使用 MybatisHolder.stopRecordSql(true) 停止记录并清空
 *
 * @author Ice2Faith
 * @date 2025/6/26 9:53
 */
@Intercepts({
        @Signature(type = Executor.class,
                method = MybatisRecordSqlProxyHandler.METHOD_UPDATE,
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlProxyHandler.METHOD_QUERY,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlProxyHandler.METHOD_QUERY,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlProxyHandler.METHOD_QUERY_CURSOR,
                args = {MappedStatement.class, Object.class, RowBounds.class}),

})
public class MybatisRecordSqlInterceptor extends MybatisInterceptorProxyInvocationHandlerAdapter {
    public MybatisRecordSqlInterceptor() {
        super(new MybatisRecordSqlProxyHandler());
    }

    public MybatisRecordSqlInterceptor(MybatisRecordSqlProxyHandler handler) {
        super(handler);
    }
}
