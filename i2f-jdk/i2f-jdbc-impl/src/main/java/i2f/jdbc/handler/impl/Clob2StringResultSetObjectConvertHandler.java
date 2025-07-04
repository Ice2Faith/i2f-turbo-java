package i2f.jdbc.handler.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.handler.ResultSetObjectConvertHandler;

import java.sql.Clob;

/**
 * @author Ice2Faith
 * @date 2025/4/2 9:29
 */
public class Clob2StringResultSetObjectConvertHandler implements ResultSetObjectConvertHandler {
    public static final Clob2StringResultSetObjectConvertHandler INSTANCE = new Clob2StringResultSetObjectConvertHandler();

    @Override
    public boolean support(Object obj) {
        return obj instanceof Clob;
    }

    @Override
    public Object convert(Object obj) {
        return ObjectConvertor.tryConvertAsType(obj, String.class);
    }
}
