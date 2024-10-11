package i2f.extension.agent.javassist.transformer.jdbc;

import java.util.function.BiPredicate;

/**
 * @author Ice2Faith
 * @date 2024/10/11 9:26
 */
public class SystemOutSqlPrintListener implements BiPredicate<String, Object> {
    public static final SystemOutSqlPrintListener INSTANCE = new SystemOutSqlPrintListener();

    @Override
    public boolean test(String sql, Object statement) {
        System.out.println("=============================\nsql====" + sql + "\n" + statement);
        return true;
    }
}
