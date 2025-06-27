package i2f.extension.mybatis.interceptor;

import i2f.database.type.DatabaseType;
import i2f.extension.mybatis.MybatisUtil;
import lombok.Data;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

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
@Data
@Intercepts({
        @Signature(type = Executor.class,
                method = MybatisRecordSqlInterceptor.METHOD_UPDATE,
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlInterceptor.METHOD_QUERY,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlInterceptor.METHOD_QUERY,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = MybatisRecordSqlInterceptor.METHOD_QUERY_CURSOR,
                args = {MappedStatement.class, Object.class, RowBounds.class}),

})
public class MybatisRecordSqlInterceptor implements Interceptor {
    public static final String METHOD_UPDATE = "update";
    public static final String METHOD_QUERY = "query";
    public static final String METHOD_QUERY_CURSOR = "queryCursor";

    protected Consumer<Object> infoLogger= System.out::println;
    protected Consumer<Object> errorLogger= System.err::println;

    protected boolean enablePrintSql = false;

    protected Properties properties = new Properties();

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!enable(invocation)) {
            return invocation.proceed();
        }

        Executor executor = (Executor) invocation.getTarget();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();

        String methodName = method.getName();
        BoundSql boundSql = null;
        MappedStatement ms = null;

        if (methodName.equals(METHOD_QUERY)) {
            ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];

            CacheKey cacheKey;
            //由于逻辑关系，只会进入一次
            if (args.length == 4) {
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
        } else if (methodName.equals(METHOD_UPDATE)) {
            ms = (MappedStatement) args[0];
            Object parameter = args[1];
            boundSql = ms.getBoundSql(parameter);
        } else if (methodName.equals(METHOD_QUERY_CURSOR)) {
            ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            boundSql = ms.getBoundSql(parameter);
            CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            return invocation.proceed();
        }

        try {
            recordSqlBefore(boundSql, ms, invocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long beginTs = System.currentTimeMillis();
        Throwable ex = null;
        try {
            Object rs = invocation.proceed();

            return rs;
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            long endTs = System.currentTimeMillis();

            long useTs = endTs - beginTs;
            try {
                recordSqlAfter(boundSql, ms, invocation, useTs, ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean enable(Invocation invocation) {
        return MybatisHolder.isRecordingSql();
    }

    public void recordSqlBefore(BoundSql boundSql, MappedStatement ms, Invocation invocation) {

    }

    public void recordSqlAfter(BoundSql boundSql, MappedStatement ms, Invocation invocation, long useTs, Throwable ex) {
        String rawSql = boundSql.getSql();

        Function<Object, String> typeStringifier = getTypeStringifier(boundSql, ms, invocation);

        String mergeSql = MybatisUtil.mergeBoundSql(boundSql, ms, typeStringifier);

        // 添加到 ThreadLocal 中
        MybatisHolder.addSql(mergeSql, boundSql, ms);

        if (enablePrintSql) {
            String id = ms.getId();
            String shortId = id;
            String[] arr = id.split("\\.");
            if (arr.length >= 2) {
                shortId = arr[arr.length - 2] + "." + arr[arr.length - 1];
            }

            if (ex == null) {
                if(infoLogger!=null) {
                    infoLogger.accept("mybatis record sql " + useTs + "(ms): " + shortId + " ==> " + mergeSql);
                }
            } else {
                if(errorLogger!=null) {
                    errorLogger.accept("mybatis record sql " + useTs + "(ms): " + shortId + " ==> " + mergeSql);
                }
            }
        }
    }

    /**
     * 根据自己的需要，可以根据URL判断数据库类型，实现自己的 typeStringifier
     * 以实现对特定数据库类型的 日期时间等特殊类型进行SQL转换
     *
     * @param boundSql
     * @param ms
     * @param invocation
     * @return
     */
    public Function<Object, String> getTypeStringifier(BoundSql boundSql, MappedStatement ms, Invocation invocation) {
        try {
            Executor executor = (Executor) invocation.getTarget();
            Connection conn = executor.getTransaction().getConnection();
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);

            if (databaseType == DatabaseType.ORACLE
                    || databaseType == DatabaseType.ORACLE_12C
                    || databaseType == DatabaseType.POSTGRE_SQL
                    || databaseType == DatabaseType.DM
                    || databaseType == DatabaseType.OCEAN_BASE) {
                return MybatisUtil::toSql4Oracle;
            }
        } catch (SQLException e) {

        }

        return null;
    }
}
