package i2f.database.metadata.impl.impl;

import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum SqlServerType {
    BIGINT_IDENTITY("bigint identity", false, false, StdType.BIGINT),
    INT("int", false, false, StdType.INT),
    VARCHAR("varchar", true, false, StdType.VARCHAR),
    NVARCHAR("nvarchar", true, false, StdType.VARCHAR),
    BIGINT("bigint", false, false, StdType.BIGINT),
    DATETIME("datetime", false, false, StdType.DATETIME),
    DECIMAL("decimal", true, true, StdType.DECIMAL),
    CHAR("char", true, false, StdType.CHAR),
    NCHAR("nchar", true, false, StdType.CHAR),
    DOUBLE("double", true, true, StdType.DOUBLE),
    FLOAT("float", true, true, StdType.FLOAT),
    REAL("real", true, true, StdType.REAL),
    MONEY("money", true, true, StdType.DECIMAL),
    SMALL_MONEY("smallmoney", true, true, StdType.DECIMAL),
    TEXT("text", false, false, StdType.TEXT),
    BLOB("blob", false, false, StdType.BLOB),
    TIMESTAMP("timestamp", false, false, StdType.TIMESTAMP),
    INTEGER("integer", false, false, StdType.INTEGER),
    BIT("bit", false, false, StdType.BIT),
    BOOL("bool", false, false, StdType.BOOL),
    DATE("date", false, false, StdType.DATE),
    TIME("time", false, false, StdType.TIME),
    DATETIME2("datetime2", false, false, StdType.DATETIME),
    DATETIME_OFFSET("datetimeoffset", false, false, StdType.DATETIME),
    SMALL_DATETIME("smalldatetime", false, false, StdType.DATETIME),
    NUMERIC("numeric", true, true, StdType.NUMERIC),
    TINYINT("tinyint", false, false, StdType.TINYINT),
    SMALLINT("smallint", false, false, StdType.SMALLINT),
    MEDIUMINT("mediumint", false, false, StdType.MEDIUMINT),
    LONG("long", false, false, StdType.LONG),
    TINYTEXT("tinytext", false, false, StdType.TINYTEXT),
    MEDIUMTEXT("mediumtext", false, false, StdType.MEDIUMTEXT),
    LONGTEXT("longtext", false, false, StdType.LONGTEXT),
    TINYBLOB("tinyblob", false, false, StdType.TINYBLOB),
    MEDIUMBLOB("mediumblob", false, false, StdType.MEDIUMBLOB),
    LONGBLOB("longblob", false, false, StdType.LONGBLOB),
    BINARY("binary", false, false, StdType.BINARY),
    VARBINARY("varbinary", false, false, StdType.VARBINARY),
    YEAR("year", false, false, StdType.YEAR),
    ENUM("enum", false, false, StdType.ENUM),
    SET("set", false, false, StdType.SET),
    GEOMETRY("geometry", false, false, StdType.VARCHAR),
    POINT("point", false, false, StdType.VARCHAR),
    LINESTRING("linestring", false, false, StdType.VARCHAR),
    POLYGON("polygon", false, false, StdType.VARCHAR),
    MULTIPOINT("multipoint", false, false, StdType.VARCHAR),
    MULTILINESTRING("multilinestring", false, false, StdType.VARCHAR),
    MULTIPOLYGON("multipolygon", false, false, StdType.VARCHAR),
    GEOMETRYCOLLECTION("geometrycollection", false, false, StdType.VARCHAR),
    ;


    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;


    SqlServerType(String text, boolean precision, boolean scale, StdType stdType) {
        this.text = text;
        this.precision = precision;
        this.scale = scale;
        this.stdType = stdType;
    }

    public String text() {
        return text;
    }

    public boolean precision() {
        return precision;
    }

    public boolean scale() {
        return scale;
    }

    public StdType stdType() {
        return stdType;
    }

}
