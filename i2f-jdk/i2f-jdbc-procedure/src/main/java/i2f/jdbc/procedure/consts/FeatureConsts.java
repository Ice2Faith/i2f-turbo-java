package i2f.jdbc.procedure.consts;

/**
 * @author Ice2Faith
 * @date 2025/1/23 10:01
 */
public interface FeatureConsts {
    // 注解类组
    // 进出参
    String IN="in";
    String OUT="out";

    // 基础类型组
    String INT = "int";
    String DOUBLE = "double";
    String FLOAT = "float";
    String STRING = "string";
    String LONG = "long";
    String SHORT = "short";
    String CHAR = "char";
    String BYTE = "byte";
    String BOOLEAN = "boolean";
    String NULL = "null";

    // 复合类型组
    String DATE = "date";

    // 字符串组
    String RENDER = "render";
    String TRIM = "trim";
    String ALIGN = "align";
    String BODY_TEXT = "body-text";
    String BODY_XML = "body-xml";

    // 节点属性
    String LOCATION_FILE = "location-file";
    String LOCATION_LINE = "location-line";
    String LOCATION_TAG = "location-tag";
    String LOCATION = "location";

    String SPACING_LEFT = "spacing-left";
    String SPACING_RIGHT = "spacing-right";
    String SPACING = "spacing";

    // 处理转换组
    String VISIT = "visit";
    String EVAL = "eval";
    String TEST = "test";

    String EVAL_JAVA = "eval-java";
    String EVAL_JS = "eval-js";
    String EVAL_TINYSCRIPT = "eval-tinyscript";
    String EVAL_TS = "eval-ts";
    String EVAL_GROOVY = "eval-groovy";
    String CLASS = "class";

    String NOT = "not";

    String DIALECT = "dialect";

    // 主要用于test进行判断
    String IS_NULL = "is-null";
    String IS_NOT_NULL = "is-not-null";
    String IS_EMPTY = "is-empty";
    String IS_NOT_EMPTY = "is-not-empty";

    // 环境值
    String DATE_NOW = "date-now";
    String UUID = "uuid";
    String CURRENT_TIME_MILLIS = "current-time-millis";
    String SNOW_UID = "snow-uid";

    // 异常的处理
    String CAUSE_FIRST = "cause-first";
    String CAUSE_LAST = "cause-last";
    String CAUSE_RAW = "cause-raw";
    String CAUSE_ROOT = "cause-root";

    // 常量属性，表示不需要上下文且能保证多次运行的结果一致的属性修饰符
    String[] CONST_FEATURES = {
            INT, DOUBLE, FLOAT, STRING, LONG, SHORT, CHAR, BYTE, BOOLEAN, NULL,
            TRIM, ALIGN, BODY_TEXT, BODY_XML,
            LOCATION_FILE, LOCATION_LINE, LOCATION_TAG, LOCATION,
            SPACING, SPACING_LEFT, SPACING_RIGHT,
            CLASS,
            NOT,
            IS_NULL, IS_NOT_EMPTY, IS_EMPTY, IS_NOT_EMPTY
    };
}
