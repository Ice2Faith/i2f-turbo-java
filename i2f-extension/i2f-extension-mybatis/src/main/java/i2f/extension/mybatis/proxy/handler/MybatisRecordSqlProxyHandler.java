package i2f.extension.mybatis.proxy.handler;

import i2f.database.type.DatabaseType;
import i2f.extension.mybatis.MybatisUtil;
import i2f.extension.mybatis.interceptor.MybatisHolder;
import i2f.invokable.IInvokable;
import i2f.invokable.Invocation;
import i2f.invokable.method.IMethod;
import i2f.proxy.std.IProxyInvocationHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/7/23 15:45
 */
@Data
@NoArgsConstructor
public class MybatisRecordSqlProxyHandler implements IProxyInvocationHandler {
    public static final String METHOD_UPDATE = "update";
    public static final String METHOD_QUERY = "query";
    public static final String METHOD_QUERY_CURSOR = "queryCursor";

    protected Consumer<Object> infoLogger = System.out::println;
    protected Consumer<Object> errorLogger = System.err::println;

    protected boolean enablePrintSql = false;

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof IMethod)) {
            throw new IllegalStateException("un-support proxy invokable type=" + invokable.getClass());
        }

        Invocation invocation = new Invocation(ivkObj, invokable, args);

        if (!enable(invocation)) {
            return invokable.invoke(ivkObj, args);
        }

        Executor executor = (Executor) invocation.getTarget();
        IMethod method = (IMethod) invokable;

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
            return invokable.invoke(ivkObj, args);
        }

        try {
            recordSqlBefore(boundSql, ms, invocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long beginTs = System.currentTimeMillis();
        Throwable ex = null;
        try {
            Object rs = invokable.invoke(ivkObj, args);

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
                if (infoLogger != null) {
                    infoLogger.accept("mybatis record sql " + useTs + "(ms): " + shortId + " ==> " + mergeSql);
                }
            } else {
                if (errorLogger != null) {
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
