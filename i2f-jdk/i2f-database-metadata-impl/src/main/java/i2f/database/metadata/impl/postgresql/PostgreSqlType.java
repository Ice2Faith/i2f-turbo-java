package i2f.database.metadata.impl.postgresql;

import i2f.database.metadata.std.IColumnType;
import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum PostgreSqlType implements IColumnType {
    INTEGER("integer", false, false, StdType.INT),
    VARCHAR("varchar", true, false, StdType.VARCHAR),
    BIGINT("bigint", false, false, StdType.BIGINT),
    TIMESTAMP("timestamp", false, false, StdType.TIMESTAMP),
    DECIMAL("decimal", true, true, StdType.DECIMAL),
    CHAR("char", true, false, StdType.CHAR),
    DOUBLE("double", true, true, StdType.DOUBLE),
    FLOAT4("float4", true, true, StdType.FLOAT),
    FLOAT8("float8", true, true, StdType.DOUBLE),
    REAL("real", true, true, StdType.REAL),
    TEXT("text", false, false, StdType.TEXT),
    BYTEA("bytea", false, false, StdType.BLOB),
    TIMESTAMPTZ("timestamptz", false, false, StdType.TIMESTAMP),
    INT8("int8", false, false, StdType.BIGINT),
    BIT("bit", false, false, StdType.BIT),
    BOOL("bool", false, false, StdType.BOOL),
    DATE("date", false, false, StdType.DATE),
    TIME("time", false, false, StdType.TIME),
    TIMETZ("timetz", false, false, StdType.TIME),
    NUMERIC("numeric", true, true, StdType.NUMERIC),
    SMALLINT("smallint", false, false, StdType.SMALLINT),
    INT2("int2", false, false, StdType.SMALLINT),
    INT4("int4", false, false, StdType.INT),
    SMALLSERIAL("smallserial", false, false, StdType.SMALLINT),
    SERIAL("serial", false, false, StdType.INT),
    BIGSERIAL("bigserial", false, false, StdType.BIGINT),
    MONEY("money", false, false, StdType.DECIMAL),
    JSON("json", false, false, StdType.VARCHAR),
    INTERVAL("interval", false, false, StdType.VARCHAR),
    UUID("uuid", false, false, StdType.VARCHAR),
    POINT("point", false, false, StdType.VARCHAR),
    LSEG("lseg", false, false, StdType.VARCHAR),
    PATH("path", false, false, StdType.VARCHAR),
    BOX("box", false, false, StdType.VARCHAR),
    POLYGON("polygon", false, false, StdType.VARCHAR),
    LINE("line", false, false, StdType.VARCHAR),
    CIRCLE("circle", false, false, StdType.VARCHAR),
    JSONB("jsonb", false, false, StdType.BLOB),
    TIME_STAMP("time_stamp", false, false, StdType.TIMESTAMP),
    YES_OR_NO("yes_or_no", false, false, StdType.VARCHAR),
    XML("xml", false, false, StdType.VARCHAR),
    XID8("xid8", false, false, StdType.VARCHAR),
    MACADDR("macaddr", false, false, StdType.VARCHAR),
    INET("inet", false, false, StdType.VARCHAR),
    CIDR("cidr", false, false, StdType.VARCHAR),
    OID("oid", false, false, StdType.VARCHAR),
    BPCHAR("bpchar", false, false, StdType.VARCHAR),
    TID("tid", false, false, StdType.VARCHAR),
    XID("xid", false, false, StdType.VARCHAR),
    CID("cid", false, false, StdType.VARCHAR),
    MACADDR8("macaddr8", false, false, StdType.VARCHAR),
    VARBIT("varbit", false, false, StdType.VARCHAR),
    JSONPATH("jsonpath", false, false, StdType.VARCHAR),
    ;


    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;


    PostgreSqlType(String text, boolean precision, boolean scale, StdType stdType) {
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


    public static PostgreSqlType ofStd(StdType type) {
        switch (type) {
            case INT:
                return PostgreSqlType.INTEGER;
            case VARCHAR:
                return PostgreSqlType.VARCHAR;
            case BIGINT:
                return PostgreSqlType.BIGINT;
            case DATETIME:
                return PostgreSqlType.TIMESTAMP;
            case DECIMAL:
                return PostgreSqlType.DECIMAL;
            case CHAR:
                return PostgreSqlType.CHAR;
            case DOUBLE:
                return PostgreSqlType.DOUBLE;
            case FLOAT:
                return PostgreSqlType.DOUBLE;
            case REAL:
                return PostgreSqlType.REAL;
            case TEXT:
                return PostgreSqlType.TEXT;
            case BLOB:
                return PostgreSqlType.TEXT;
            case TIMESTAMP:
                return PostgreSqlType.TIMESTAMP;
            case INTEGER:
                return PostgreSqlType.INTEGER;
            case BIT:
                return PostgreSqlType.BIT;
            case BOOL:
                return PostgreSqlType.BOOL;
            case DATE:
                return PostgreSqlType.DATE;
            case TIME:
                return PostgreSqlType.TIME;
            case NUMERIC:
                return PostgreSqlType.NUMERIC;
            case TINYINT:
                return PostgreSqlType.INT2;
            case SMALLINT:
                return PostgreSqlType.INT4;
            case MEDIUMINT:
                return PostgreSqlType.INT8;
            case LONG:
                return PostgreSqlType.BIGINT;
            case TINYTEXT:
                return PostgreSqlType.TEXT;
            case MEDIUMTEXT:
                return PostgreSqlType.TEXT;
            case LONGTEXT:
                return PostgreSqlType.TEXT;
            case TINYBLOB:
                return PostgreSqlType.TEXT;
            case MEDIUMBLOB:
                return PostgreSqlType.TEXT;
            case LONGBLOB:
                return PostgreSqlType.TEXT;
            case BINARY:
                return PostgreSqlType.TEXT;
            case VARBINARY:
                return PostgreSqlType.TEXT;
            case YEAR:
                return PostgreSqlType.VARCHAR;
            case ENUM:
                return PostgreSqlType.VARCHAR;
            case SET:
                return PostgreSqlType.VARCHAR;
            default:
                return PostgreSqlType.VARCHAR;
        }
    }
}
