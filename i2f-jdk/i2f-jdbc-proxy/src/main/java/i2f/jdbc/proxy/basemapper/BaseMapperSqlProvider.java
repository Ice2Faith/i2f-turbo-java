package i2f.jdbc.proxy.basemapper;

import i2f.bindsql.BindSql;
import i2f.bql.core.bean.Bql;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 11:50
 * @desc
 */
public class BaseMapperSqlProvider {
    public static BindSql parse(Method method, Object[] args) {
        if (!method.getDeclaringClass().equals(BaseMapper.class)) {
            return null;
        }
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String name = method.getName();
        if ("insert".equals(name)) {
            if (parameterCount == 2) {
                return Bql.$bean()
                        .$mapInsert((String) args[0], (Map<String, Object>) args[1])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanInsert(args[0])
                        .$$();
            }
        } else if ("update".equals(name)) {
            if (parameterCount == 3) {
                return Bql.$bean()
                        .$mapUpdate((String) args[0], (Map<String, Object>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 2) {
                return Bql.$bean()
                        .$beanUpdate(args[0], args[1])
                        .$$();
            }
        } else if ("delete".equals(name)) {
            if (parameterCount == 2) {
                return Bql.$bean()
                        .$mapDelete((String) args[0], (Map<String, Object>) args[1])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanDelete(args[0])
                        .$$();
            }
        } else if ("listMap".equals(name)) {
            if (parameterCount == 3) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("list".equals(name)) {
            if (parameterCount == 3) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("findMap".equals(name)) {
            if (parameterCount == 3) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("find".equals(name)) {
            if (parameterCount == 3) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("count".equals(name)) {
            if (parameterCount == 2) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], Collections.singleton("count(1) as cnt"), (Map<String, Object>) args[1])
                        .$$();
            } else if (parameterCount == 1) {
                return Bql.$bean()
                        .$beanQuery(args[0], Collections.singleton("count(1) as cnt"))
                        .$$();
            }
        } else if ("pageMap".equals(name)) {
            if (parameterCount == 4) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 2) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("page".equals(name)) {
            if (parameterCount == 4) {
                return Bql.$bean()
                        .$mapQuery((String) args[0], (Collection<String>) args[1], (Map<String, Object>) args[2])
                        .$$();
            } else if (parameterCount == 2) {
                return Bql.$bean()
                        .$beanQuery(args[0])
                        .$$();
            }
        } else if ("executeUpdate".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeGet".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeListMap".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeList".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeFindMap".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeFind".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executePageMap".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executePage".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        } else if ("executeRaw".equals(name)) {
            if (parameterCount == 1) {
                return (BindSql) args[0];
            }
        }

        return null;
    }
}
