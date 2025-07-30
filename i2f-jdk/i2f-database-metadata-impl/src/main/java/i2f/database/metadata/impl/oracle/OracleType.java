package i2f.database.metadata.impl.oracle;

import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum OracleType {
    NUMBER("NUMBER", true, true, StdType.DECIMAL),
    VARCHAR2("VARCHAR2", true, false, StdType.VARCHAR),
    CHAR("CHAR", true, false, StdType.CHAR),
    DATE("DATE", false, false, StdType.DATETIME),
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

    OracleType(String text, boolean precision, boolean scale, StdType stdType) {
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


    public static OracleType ofStd(StdType type) {
        switch (type) {
            case INT:
                return OracleType.NUMBER;
            case VARCHAR:
                return OracleType.VARCHAR2;
            case BIGINT:
                return OracleType.NUMBER;
            case DATETIME:
                return OracleType.DATE;
            case DECIMAL:
                return OracleType.NUMBER;
            case CHAR:
                return OracleType.CHAR;
            case DOUBLE:
                return OracleType.NUMBER;
            case FLOAT:
                return OracleType.FLOAT;
            case REAL:
                return OracleType.REAL;
            case TEXT:
                return OracleType.CLOB;
            case BLOB:
                return OracleType.BLOB;
            case TIMESTAMP:
                return OracleType.TIMESTAMP;
            case INTEGER:
                return OracleType.NUMBER;
            case BIT:
                return OracleType.NUMBER;
            case BOOL:
                return OracleType.NUMBER;
            case DATE:
                return OracleType.DATE;
            case TIME:
                return OracleType.DATE;
            case NUMERIC:
                return OracleType.NUMBER;
            case TINYINT:
                return OracleType.NUMBER;
            case SMALLINT:
                return OracleType.NUMBER;
            case MEDIUMINT:
                return OracleType.NUMBER;
            case LONG:
                return OracleType.NUMBER;
            case TINYTEXT:
                return OracleType.CLOB;
            case MEDIUMTEXT:
                return OracleType.CLOB;
            case LONGTEXT:
                return OracleType.CLOB;
            case TINYBLOB:
                return OracleType.BLOB;
            case MEDIUMBLOB:
                return OracleType.BLOB;
            case LONGBLOB:
                return OracleType.BLOB;
            case BINARY:
                return OracleType.BLOB;
            case VARBINARY:
                return OracleType.BLOB;
            case YEAR:
                return OracleType.VARCHAR2;
            case ENUM:
                return OracleType.VARCHAR2;
            case SET:
                return OracleType.VARCHAR2;
            default:
                return OracleType.VARCHAR2;
        }
    }
}
