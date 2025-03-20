package i2f.bindsql.stringify.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.stringify.BindSqlStringifier;
import i2f.convert.obj.ObjectConvertor;
import i2f.match.regex.RegexUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2025/3/20 14:56
 */
public class BasicBindSqlStringifier implements BindSqlStringifier {
    public static final BasicBindSqlStringifier INSTANCE = new BasicBindSqlStringifier();
    public static final DateTimeFormatter DATE_TYPE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String stringify(BindSql bql) {
        Iterator<Object> iterator = bql.getArgs().iterator();
        return RegexUtil.regexFindAndReplace(bql.getSql(), "\\?", (str) -> {
            Object obj = iterator.next();
            return paramToString(obj);
        });
    }


    public String paramToString(Object obj) {
        String ret = preParamToString(obj);
        if (ret != null) {
            return ret;
        }
        if (obj == null) {
            return nullToString();
        }
        Class<?> clazz = obj.getClass();
        if (ObjectConvertor.isNumericType(clazz)) {
            return numericToString(obj);
        }
        if (ObjectConvertor.isBooleanType(clazz)) {
            return booleanToString(obj);
        }
        if (ObjectConvertor.isDateType(clazz)) {
            return dateToString(obj);
        }
        if (CharSequence.class.isAssignableFrom(clazz)
                || Appendable.class.isAssignableFrom(clazz)) {
            return stringToString(obj);
        }

        return objectToString(obj);
    }

    public String escapeSqlString(String str) {
        if (str == null) {
            return null;
        }
        str = str.replace("'", "''");
        return "'" + str + "'";
    }

    public String preParamToString(Object obj) {
        return null;
    }


    public String nullToString() {
        return "null";
    }

    public String numericToString(Object obj) {
        return String.valueOf(obj);
    }

    public String booleanToString(Object obj) {
        Boolean ok = (Boolean) ObjectConvertor.tryConvertAsType(obj, Boolean.class);
        if (ok) {
            return "1";
        }
        return "0";
    }

    public String dateToString(Object obj) {
        LocalDateTime time = (LocalDateTime) ObjectConvertor.tryConvertAsType(obj, LocalDateTime.class);
        return escapeSqlString(DATE_TYPE_FORMATTER.format(time));
    }

    public String stringToString(Object obj) {
        return escapeSqlString(String.valueOf(obj));
    }

    public String objectToString(Object obj) {
        return escapeSqlString(String.valueOf(obj));
    }
}
