package i2f.extension.mybatis.interceptor;

import i2f.bindsql.BindSql;
import i2f.bindsql.count.CountWrappers;
import i2f.bindsql.count.ICountWrapper;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.database.type.DatabaseType;
import i2f.page.ApiOffsetSize;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/7/5 15:01
 * @desc
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = MybaisPaginationInterceptor.QUERY, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class}),
                @Signature(type = Executor.class, method = MybaisPaginationInterceptor.QUERY, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class,
                        CacheKey.class,
                        BoundSql.class}),
                @Signature(type = Executor.class, method = MybaisPaginationInterceptor.QUERY_CURSOR, args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class}),
        }
)
public class MybaisPaginationInterceptor implements Interceptor {
    public static final String QUERY = "query";
    public static final String QUERY_CURSOR = "queryCursor";
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
        ApiOffsetSize page = getPage();
        if (page == null) {
            return invocation.proceed();
        }

        Executor executor = (Executor) invocation.getTarget();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];

        ResultHandler<?> resultHandler = null;
        CacheKey cacheKey = null;
        BoundSql boundSql = null;

        if (QUERY.equals(method.getName())) {
            if (args.length == 4) {
                resultHandler = (ResultHandler<?>) args[3];
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else if (args.length == 6) {
                resultHandler = (ResultHandler<?>) args[3];
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
        } else if (QUERY_CURSOR.equals(method.getName())) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, boundSql);
        }

        DatabaseType databaseType = detectDatabaseType(invocation, executor, ms);

        if (!MybatisPagination.isDisabledCount()) {
            ICountWrapper countWrapper = getCountWrapper(databaseType, invocation, executor, ms);

            long count = executeCount(countWrapper, executor, ms, parameter, boundSql, rowBounds, resultHandler);
            setTotal(count);
            if (count <= 0) {
                if (QUERY.equals(method.getName())) {
                    return new ArrayList<>();
                } else {

                    return EMPTY_CURSOR;
                }
            }
        }

        IPageWrapper pageWrapper = getPageWrapper(databaseType, invocation, executor, ms);


        if (QUERY.equals(method.getName())) {

            List<?> list = executePage(pageWrapper, page, executor, ms, parameter, boundSql, rowBounds, resultHandler, cacheKey);

            return list;
        } else {

            Cursor<?> list = executePageCursor(pageWrapper, page, executor, ms, parameter, boundSql, rowBounds, resultHandler, cacheKey);

            return list;
        }
    }

    public void setTotal(long count) {
        MybatisPagination.setTotal(count);
    }

    public ApiOffsetSize getPage() {
        return MybatisPagination.getPage();
    }

    public ICountWrapper getCountWrapper(DatabaseType databaseType, Invocation invocation, Executor executor, MappedStatement ms) {
        return CountWrappers.wrapper(databaseType);
    }

    public IPageWrapper getPageWrapper(DatabaseType databaseType, Invocation invocation, Executor executor, MappedStatement ms) {
        return PageWrappers.wrapper(databaseType);
    }

    public DatabaseType detectDatabaseType(Invocation invocation, Executor executor, MappedStatement ms) throws Throwable {
        DatabaseType type = MybatisPagination.getDatabaseType();
        if (type != null) {
            return type;
        }
        Connection connection = null;
        boolean closeConnection = false;

        try {
            if (connection == null) {
                Transaction transaction = executor.getTransaction();
                connection = transaction.getConnection();
                closeConnection = false;
            }
        } catch (Exception e) {

        }

        try {
            if (connection == null) {
                Environment environment = ms.getConfiguration().getEnvironment();
                DataSource dataSource = environment.getDataSource();
                connection = dataSource.getConnection();
                closeConnection = true;
            }
        } catch (Exception e) {

        }

        DatabaseType databaseType = DatabaseType.typeOfConnection(connection);

        return databaseType;
    }


    public Cursor<?> executePageCursor(IPageWrapper pageWrapper, ApiOffsetSize page,
                                       Executor executor, MappedStatement ms,
                                       Object parameter,
                                       BoundSql boundSql,
                                       RowBounds rowBounds,
                                       ResultHandler<?> resultHandler,
                                       CacheKey cacheKey) throws Throwable {
        String pageMsId = ms.getId() + "_pagination_page_cursor";
        String pageSql = wrapPageSql(pageWrapper, page, ms, boundSql, parameter, rowBounds, cacheKey);

        StaticSqlSource staticSqlSource = new StaticSqlSource(ms.getConfiguration(), pageSql, boundSql.getParameterMappings());

        MappedStatement.Builder pageMsBuilder = copyMappedStatement(ms, pageMsId, staticSqlSource, null);

        MappedStatement pageMs = pageMsBuilder.build();

        CacheKey countKey = executor.createCacheKey(pageMs, parameter, RowBounds.DEFAULT, boundSql);

        BoundSql pageBoundSql = new BoundSql(pageMs.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
        copyAdditionalParameters(boundSql, pageBoundSql);
        return executor.queryCursor(pageMs, parameter, RowBounds.DEFAULT);
    }


    public List<?> executePage(IPageWrapper pageWrapper, ApiOffsetSize page,
                               Executor executor, MappedStatement ms,
                               Object parameter,
                               BoundSql boundSql,
                               RowBounds rowBounds,
                               ResultHandler<?> resultHandler,
                               CacheKey cacheKey) throws Throwable {
        String pageMsId = ms.getId() + "_pagination_page";
        MappedStatement.Builder pageMsBuilder = copyMappedStatement(ms, pageMsId, null, null);
        MappedStatement pageMs = pageMsBuilder.build();

        CacheKey pageKey = executor.createCacheKey(pageMs, parameter, RowBounds.DEFAULT, boundSql);
        String pageSql = wrapPageSql(pageWrapper, page, pageMs, boundSql, parameter, rowBounds, pageKey);
        BoundSql pageBoundSql = new BoundSql(pageMs.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
        copyAdditionalParameters(boundSql, pageBoundSql);
        return executor.query(pageMs, parameter, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
    }

    public String wrapPageSql(IPageWrapper pageWrapper, ApiOffsetSize page,
                              MappedStatement ms, BoundSql boundSql, Object parameter, RowBounds rowBounds, CacheKey cacheKey) {
        cacheKey.update(page.getOffset());
        cacheKey.update(page.getSize());
        return pageWrapper.apply(boundSql.getSql(), page);
    }

    public long executeCount(ICountWrapper countWrapper, Executor executor, MappedStatement ms,
                             Object parameter,
                             BoundSql boundSql,
                             RowBounds rowBounds,
                             ResultHandler<?> resultHandler) throws Throwable {
        String countMsId = ms.getId() + "_pagination_count";
        MappedStatement.Builder countMsBuilder = copyMappedStatement(ms, countMsId, null, null);

        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, new ArrayList<>()).build();
        resultMaps.add(resultMap);
        countMsBuilder.resultMaps(resultMaps);

        MappedStatement countMs = countMsBuilder.build();


        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        String countSql = wrapCountSql(countWrapper, countMs, boundSql, parameter, rowBounds, countKey);
        BoundSql countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        copyAdditionalParameters(boundSql, countBoundSql);
        List<?> list = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        //某些数据（如 TDEngine）查询 count 无结果时返回 null
        if (list == null || list.isEmpty()) {
            return 0L;
        }
        return ((Number) (list).get(0)).longValue();
    }

    public String wrapCountSql(ICountWrapper countWrapper, MappedStatement ms, BoundSql boundSql, Object parameter, RowBounds rowBounds, CacheKey countKey) {
        return countWrapper.apply(new BindSql(boundSql.getSql())).getSql();
    }


    public static void copyAdditionalParameters(BoundSql srcSql, BoundSql dstSql) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> additionalParameters = getAdditionalParameters(srcSql);
        for (String key : additionalParameters.keySet()) {
            dstSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
    }


    public static Map<String, Object> getAdditionalParameters(BoundSql boundSql) throws NoSuchFieldException, IllegalAccessException {
        Field field = BoundSql.class.getDeclaredField("additionalParameters");
        field.setAccessible(true);
        Map<String, Object> additionalParameters = (Map<String, Object>) field.get(boundSql);
        return additionalParameters;
    }

    public MappedStatement.Builder copyMappedStatement(MappedStatement ms, String newMsId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), newMsId,
                (sqlSource == null) ? ms.getSqlSource() : sqlSource,
                (sqlCommandType == null) ? ms.getSqlCommandType() : sqlCommandType);
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder;
    }

    public static final Cursor<?> EMPTY_CURSOR = new Cursor() {
        private List<?> list = new ArrayList<>();

        @Override
        public Iterator<?> iterator() {
            return list.iterator();
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public boolean isConsumed() {
            return false;
        }

        @Override
        public int getCurrentIndex() {
            return 0;
        }
    };
}
