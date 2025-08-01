package i2f.database.metadata.impl.dm;

import i2f.database.metadata.std.IColumnType;
import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc actual equals oracle
 */
public enum DmType implements IColumnType {
    NUMBER("NUMBER", true, true, StdType.DECIMAL),
    VARCHAR2("VARCHAR2", true, false, StdType.VARCHAR),
    CHAR("CHAR", true, false, StdType.CHAR),
    DATE("DATE", false, false, StdType.DATE),
    FLOAT("FLOAT", true, true, StdType.FLOAT),
    REAL("REAL", true, true, StdType.REAL),
    LONG("LONG", false, false, StdType.TEXT),
    BLOB("BLOB", false, false, StdType.BLOB),
    TIMESTAMP("TIMESTAMP", false, false, StdType.TIMESTAMP),
    CLOB("CLOB", false, false, StdType.BLOB),
    NCLOB("NCLOB", false, false, StdType.BLOB),
    NCHAR("NCHAR", true, false, StdType.CHAR),
    NVARCHAR2("NVARCHAR2", true, false, StdType.VARCHAR),
    ARRAY("ARRAY", false, false, StdType.VARCHAR),
    RAW("RAW", false, false, StdType.BLOB),
    REF("REF", false, false, StdType.VARCHAR),
    INTERVALDS("INTERVALDS", false, false, StdType.VARCHAR),
    INTERVALYM("INTERVALYM", false, false, StdType.VARCHAR),
    STRUCT("STRUCT", false, false, StdType.VARCHAR),
    ;
    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;

    DmType(String text, boolean precision, boolean scale, StdType stdType) {
        this.text = text;
        this.precision = precision;
        this.scale = scale;
        this.stdType = stdType;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public boolean precision() {
        return precision;
    }

    @Override
    public boolean scale() {
        return scale;
    }

    @Override
    public StdType stdType() {
        return stdType;
    }

}
