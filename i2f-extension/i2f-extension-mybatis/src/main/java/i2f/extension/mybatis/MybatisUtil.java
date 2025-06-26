package i2f.extension.mybatis;

import i2f.match.regex.RegexUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/6/26 11:50
 */
public class MybatisUtil {

    public static final Configuration DEFAULT_CONFIGURATION = new Configuration();

    public static final ThreadLocal<SimpleDateFormat> sfmt = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String decorateAsSqlString(Object value) {
        String str = String.valueOf(value);
        str = str.replace("'", "''");
        return "'" + str + "'";
    }


    public static void fillStatementParameters(PreparedStatement ps, BoundSql boundSql) {
        fillStatementParameters(ps, boundSql, DEFAULT_CONFIGURATION);
    }

    public static void fillStatementParameters(PreparedStatement ps, BoundSql boundSql, MappedStatement ms) {
        fillStatementParameters(ps, boundSql, ms.getConfiguration());
    }

    public static void fillStatementParameters(PreparedStatement ps, BoundSql boundSql, Configuration configuration) {
        List<Map.Entry<ParameterMapping, Object>> parameters = extraParameters(boundSql, configuration);
        if (parameters != null && !parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                Map.Entry<ParameterMapping, Object> entry = parameters.get(i);

                ParameterMapping parameterMapping = entry.getKey();
                Object value = entry.getValue();

                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                JdbcType jdbcType = parameterMapping.getJdbcType();
                if (value == null && jdbcType == null) {
                    jdbcType = configuration.getJdbcTypeForNull();
                }

                try {
                    typeHandler.setParameter(ps, i + 1, value, jdbcType);
                } catch (SQLException | TypeException var11) {
                    Exception e = var11;
                    throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
                }
            }
        }
    }


    public static String mergeBoundSql(BoundSql boundSql, MappedStatement ms) {
        return mergeBoundSql(boundSql, ms.getConfiguration(), null);
    }

    public static String mergeBoundSql(BoundSql boundSql, MappedStatement ms, Function<Object, String> typeStringifier) {
        return mergeBoundSql(boundSql, ms.getConfiguration(), typeStringifier);
    }

    public static String mergeBoundSql(BoundSql boundSql, Configuration configuration) {
        return mergeBoundSql(boundSql, configuration, null);
    }

    public static String mergeBoundSql(BoundSql boundSql, Configuration configuration, Function<Object, String> typeStringifier) {

        List<Map.Entry<ParameterMapping, Object>> parameters = extraParameters(boundSql, configuration);
        int size = parameters.size();
        String sql = boundSql.getSql();
        AtomicInteger idx = new AtomicInteger(0);
        sql = RegexUtil.regexFindAndReplace(sql, "\\?", (s) -> {
            int i = idx.getAndIncrement();
            if (i < size) {
                Map.Entry<ParameterMapping, Object> entry = parameters.get(i);
                ParameterMapping parameterMapping = entry.getKey();
                Object value = entry.getValue();
                if (typeStringifier != null) {
                    String str = typeStringifier.apply(value);
                    if (str != null) {
                        return str;
                    }
                }
                if (value == null) {
                    return "null";
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();
                if (jdbcType != null) {
                    if (JdbcType.NULL == jdbcType) {
                        return "null";
                    }
                    if (Arrays.asList(
                            JdbcType.TINYINT,
                            JdbcType.SMALLINT,
                            JdbcType.INTEGER,
                            JdbcType.BIGINT,

                            JdbcType.FLOAT,
                            JdbcType.REAL,
                            JdbcType.DOUBLE,
                            JdbcType.NUMERIC,
                            JdbcType.DECIMAL,

                            JdbcType.ROWID
                    ).contains(jdbcType)) {
                        return String.valueOf(value);
                    }
                    if (Arrays.asList(
                            JdbcType.CHAR,
                            JdbcType.VARCHAR,
                            JdbcType.LONGVARCHAR,
                            JdbcType.LONGNVARCHAR,
                            JdbcType.NVARCHAR,
                            JdbcType.NCHAR,
                            JdbcType.SQLXML
                    ).contains(jdbcType)) {
                        return decorateAsSqlString(value);
                    }
                    if (Arrays.asList(
                            JdbcType.CLOB,
                            JdbcType.NCLOB
                    ).contains(jdbcType)) {
                        // 不应该在此处处理，有可能用户后续还要使用这些变量
                        // 如果此处处理了例如 Reader 这类只能读取一次的对象
                        // 可能会导致后续用户使用时出现问题
                        // 因此，直接进行valueOf就行
                        return decorateAsSqlString(value);
                    }
                }
                if (value instanceof Number) {
                    return String.valueOf(value);
                }
                if (value instanceof Date) {
                    String str = sfmt.get().format((Date) value);
                    return decorateAsSqlString(str);
                }
                if (value instanceof LocalDate) {
                    String str = dateFormatter.format((LocalDate) value);
                    return decorateAsSqlString(str);
                }
                if (value instanceof LocalTime) {
                    String str = timeFormatter.format((LocalTime) value);
                    return decorateAsSqlString(str);
                }
                if (value instanceof LocalDateTime) {
                    String str = dateTimeFormatter.format((LocalDateTime) value);
                    return decorateAsSqlString(str);
                }
                return decorateAsSqlString(value);
            }
            return s;
        });
        return sql;
    }

    public static List<Map.Entry<ParameterMapping, Object>> extraParameters(BoundSql boundSql) {
        return extraParameters(boundSql, DEFAULT_CONFIGURATION);
    }

    public static List<Map.Entry<ParameterMapping, Object>> extraParameters(BoundSql boundSql, MappedStatement ms) {
        return extraParameters(boundSql, ms.getConfiguration());
    }

    public static List<Map.Entry<ParameterMapping, Object>> extraParameters(BoundSql boundSql, Configuration configuration) {
        List<Map.Entry<ParameterMapping, Object>> ret = new ArrayList<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = null;
            Object parameterObject = boundSql.getParameterObject();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            for (int i = 0; i < parameterMappings.size(); ++i) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    String propertyName = parameterMapping.getProperty();
                    Object value;
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        if (metaObject == null) {
                            metaObject = configuration.newMetaObject(parameterObject);
                        }

                        value = metaObject.getValue(propertyName);
                    }

                    ret.add(new AbstractMap.SimpleEntry<>(parameterMapping, value));
                }
            }
        }
        return ret;
    }

    public static String toSql4Oracle(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.util.Date) {
            String str = sfmt.get().format((java.util.Date) value);
            return toDate4Oracle(str, "yyyy-MM-dd HH24:mi:ss");
        }
        if (value instanceof LocalDate) {
            String str = dateFormatter.format((LocalDate) value);
            return toDate4Oracle(str, "yyyy-MM-dd");
        }
        if (value instanceof LocalTime) {
            String str = timeFormatter.format((LocalTime) value);
            return toDate4Oracle(str, "HH24:mi:ss");
        }
        if (value instanceof LocalDateTime) {
            String str = dateTimeFormatter.format((LocalDateTime) value);
            return toDate4Oracle(str, "yyyy-MM-dd HH24:mi:ss");
        }
        // 不处理
        return null;
    }

    public static String toDate4Oracle(String date, String oraPattern) {
        return "TO_DATE(" + decorateAsSqlString(date) + "," + decorateAsSqlString(oraPattern) + ")";
    }
}
