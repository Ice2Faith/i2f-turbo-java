package i2f.database.metadata.impl.sqlite3;

import i2f.database.metadata.std.IColumnType;
import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum Sqlite3Type implements IColumnType {
    INTEGER("integer", false, false, StdType.DOUBLE),
    TEXT("text", false, false, StdType.VARCHAR),
    REAL("real", false, false, StdType.REAL),
    BLOB("blob", false, false, StdType.BLOB),
    NULL("null", false, false, StdType.VARCHAR),
    ;
    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;

    Sqlite3Type(String text, boolean precision, boolean scale, StdType stdType) {
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
