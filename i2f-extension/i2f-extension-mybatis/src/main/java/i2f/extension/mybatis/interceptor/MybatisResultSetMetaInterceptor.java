package i2f.extension.mybatis.interceptor;

import i2f.extension.mybatis.data.ColumnMeta;
import lombok.Data;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Method;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/6/30 20:58
 * @desc
 */
@Data
@Intercepts({
        @Signature(type = ResultSetHandler.class,
                method = MybatisResultSetMetaInterceptor.METHOD_RESULT_SSET,
                args = {Statement.class}),
        @Signature(type = ResultSetHandler.class,
                method = MybatisResultSetMetaInterceptor.METHOD_CURSOR_RESULT_SET,
                args = {Statement.class}),

})
public class MybatisResultSetMetaInterceptor implements Interceptor {
    public static final String METHOD_RESULT_SSET = "handleResultSets";
    public static final String METHOD_CURSOR_RESULT_SET = "handleCursorResultSets";

    protected Consumer<Object> infoLogger = System.out::println;
    protected Consumer<Object> errorLogger = System.err::println;

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

        ResultSetHandler executor = (ResultSetHandler) invocation.getTarget();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();

        Statement statement = (Statement) args[0];

        ResultSet rs = statement.getResultSet();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<ColumnMeta> columns = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i);
            String columnName = metaData.getColumnName(i);
            String columnTypeName = metaData.getColumnTypeName(i);
            int columnType = metaData.getColumnType(i);
            JDBCType jdbcType = null;
            try {
                jdbcType = JDBCType.valueOf(columnType);
            } catch (Exception e) {

            }
            int columnDisplaySize = metaData.getColumnDisplaySize(i);
            int precision = metaData.getPrecision(i);
            int scale = metaData.getScale(i);
            String className = metaData.getColumnClassName(i);
            Class<?> clazz = null;
            if (clazz == null) {
                try {
                    clazz = Class.forName(className);
                } catch (Exception e) {

                }
            }
            if (clazz == null) {
                try {
                    clazz = loader.loadClass(className);
                } catch (Exception e) {

                }
            }
            boolean autoIncrement = metaData.isAutoIncrement(i);
            boolean notNull = metaData.isNullable(i) == ResultSetMetaData.columnNoNulls;

            ColumnMeta meta = new ColumnMeta();
            meta.setName(columnLabel);
            meta.setColumnName(columnName);
            meta.setTypeName(columnTypeName);
            meta.setType(columnType);
            meta.setJdbcType(jdbcType);
            meta.setDisplaySize(columnDisplaySize);
            meta.setPrecision(precision);
            meta.setScale(scale);
            meta.setClassName(className);
            meta.setClazz(clazz);
            meta.setAutoIncrement(autoIncrement);
            meta.setNullable(!notNull);
            columns.add(meta);
        }

        MybatisHolder.setExecColumnsMeta(columns);

        return invocation.proceed();

    }

    public boolean enable(Invocation invocation) {
        return MybatisHolder.isRecordingMeta();
    }

}
