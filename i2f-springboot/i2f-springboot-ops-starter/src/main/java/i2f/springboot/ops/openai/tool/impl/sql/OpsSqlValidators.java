package i2f.springboot.ops.openai.tool.impl.sql;

import i2f.reflect.ReflectResolver;
import i2f.springboot.ops.openai.tool.impl.sql.impl.OpsSimpleRegexSqlValidator;
import i2f.springboot.ops.openai.tool.impl.sql.impl.OpsSqlParserAstSqlValidator;

import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2026/6/4 10:56
 * @desc
 */
public class OpsSqlValidators {
    public static final String VALIDATOR_CLASS_NAME_PROPERTY = "ops.sql.validator";
    protected static final OpsSqlValidator sqlValidator;

    static {
        OpsSqlValidator validator = null;
        String property = System.getProperty(VALIDATOR_CLASS_NAME_PROPERTY);
        if (property != null && !property.isEmpty()) {
            if (validator == null) {
                ServiceLoader<OpsSqlValidator> list = ServiceLoader.load(OpsSqlValidator.class);
                for (OpsSqlValidator item : list) {
                    if (property.equals(item.getClass().getName())) {
                        validator = item;
                    }
                }
            }
            if (validator == null) {
                Class<?> clazz = ReflectResolver.loadClass(property);
                if (clazz != null) {
                    try {
                        validator = (OpsSqlValidator) ReflectResolver.getInstance(clazz);
                    } catch (Throwable e) {

                    }
                }
            }
        }

        if (validator == null) {
            Class<?> clazz = ReflectResolver.loadClass("net.sf.jsqlparser.parser.CCJSqlParserUtil");
            if (clazz != null) {
                validator = OpsSqlParserAstSqlValidator.INSTANCE;
            }
        }

        if (validator == null) {
            validator = OpsSimpleRegexSqlValidator.INSTANCE;
        }
        sqlValidator = validator;
    }

    public static OpsSqlValidator getSqlValidator() {
        return sqlValidator;
    }

}
