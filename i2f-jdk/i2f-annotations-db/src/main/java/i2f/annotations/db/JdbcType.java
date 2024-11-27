package i2f.annotations.db;

import i2f.annotations.core.doc.Comment;

import java.lang.annotation.*;
import java.sql.JDBCType;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "JDBC类型",
        "用于表示jdbc中对应的数据类型"
})
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,

        ElementType.TYPE_PARAMETER,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PACKAGE,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JdbcType {
    JDBCType value();
}
