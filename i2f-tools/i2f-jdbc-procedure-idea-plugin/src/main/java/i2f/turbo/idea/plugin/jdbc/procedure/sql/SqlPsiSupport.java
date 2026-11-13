package i2f.turbo.idea.plugin.jdbc.procedure.sql;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;

import java.lang.reflect.Method;

/**
 * SQL PSI 能力的隔离入口。
 * <p>
 * 本类刻意不引用 com.intellij.sql.* 中的任何类型：com.intellij.database（Database Tools and SQL）
 * 属于 Ultimate 订阅功能，自 2025.3 Unified 发行版起，没有订阅时该插件会被禁用，
 * 此时如果直接链接 SQL 类会抛 NoClassDefFoundError，进而拖垮整个插件的类加载。
 * 因此所有 SQL 相关实现都放在 {@link SqlCompletionSupport} 中，仅在 database 插件可用时反射加载。
 *
 * @author Ice2Faith
 * @desc SQL 能力探针与反射桥接，保证无 database 插件时插件本体仍可加载
 */
public final class SqlPsiSupport {
    public static final Logger log = Logger.getInstance(SqlPsiSupport.class);

    public static final String DATABASE_PLUGIN_ID = "com.intellij.database";
    public static final String SQL_PARAMETER_CLASS = "com.intellij.sql.psi.SqlParameter";
    public static final String SQL_COMPLETION_SUPPORT_CLASS = "i2f.turbo.idea.plugin.jdbc.procedure.sql.SqlCompletionSupport";

    private static volatile boolean initialized = false;
    private static volatile Class<? extends PsiElement> sqlParameterType = null;
    private static volatile Method completionMethod = null;

    private SqlPsiSupport() {
    }

    /**
     * SQL PSI（database 插件）是否可用，不可用时 SQL 相关补全/注入直接跳过
     */
    public static boolean isAvailable() {
        if (!initialized) {
            init();
        }
        return sqlParameterType != null && completionMethod != null;
    }

    /**
     * SQL 参数节点类型（com.intellij.sql.psi.SqlParameter），不可用时返回 null
     */
    public static Class<? extends PsiElement> sqlParameterType() {
        if (!initialized) {
            init();
        }
        return sqlParameterType;
    }

    /**
     * 尝试进行 SQL 参数补全，返回 true 表示已经处理，调用方应结束本次补全
     */
    public static boolean tryComplete(CompletionParameters parameters, PsiElement position, CompletionResultSet result) {
        if (!isAvailable()) {
            return false;
        }
        try {
            return (Boolean) completionMethod.invoke(null, parameters, position, result);
        } catch (Throwable e) {
            log.warn("sql-completion error:" + e.getClass() + " : " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static synchronized void init() {
        if (initialized) {
            return;
        }
        try {
            ClassLoader classLoader = SqlPsiSupport.class.getClassLoader();
            Class<?> parameterType = Class.forName(SQL_PARAMETER_CLASS, false, classLoader);
            sqlParameterType = (Class<? extends PsiElement>) parameterType;
            Class<?> supportType = Class.forName(SQL_COMPLETION_SUPPORT_CLASS, true, classLoader);
            completionMethod = supportType.getDeclaredMethod("tryComplete",
                    CompletionParameters.class, PsiElement.class, CompletionResultSet.class);
        } catch (Throwable e) {
            sqlParameterType = null;
            completionMethod = null;
            log.info("sql psi support unavailable (plugin " + DATABASE_PLUGIN_ID + " not loaded), "
                    + "sql completion and sql parameter injection are disabled: " + e);
        } finally {
            initialized = true;
        }
    }
}
