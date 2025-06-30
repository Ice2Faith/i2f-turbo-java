package i2f.database.dialect.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.database.dialect.DatabaseObject2SqlStringifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2025/6/26 14:06
 */
public abstract class AbsDatabaseObject2SqlStringifier implements DatabaseObject2SqlStringifier {
    public static final DateTimeFormatter DATE_TYPE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String stringify(Object obj) {
        String str = preStringify(obj);
        if (str != null) {
            return str;
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
        return decorateAsSqlString(DATE_TYPE_FORMATTER.format(time));
    }

    public String stringToString(Object obj) {
        return decorateAsSqlString(String.valueOf(obj));
    }

    public String objectToString(Object obj) {
        return decorateAsSqlString(String.valueOf(obj));
    }

    public String preStringify(Object value) {
        return null;
    }

    public static String decorateAsSqlString(Object value) {
        String str = String.valueOf(value);
        str = str.replace("'", "''");
        return "'" + str + "'";
    }
}
