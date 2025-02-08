package i2f.database.metadata.impl.dm;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;
import i2f.database.metadata.std.StdType;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:56
 * @desc actual equals oracle
 */
public class DmDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final DmDatabaseMetadataProvider INSTANCE = new DmDatabaseMetadataProvider();

    public static final String DRIVER_NAME = "dm.jdbc.driver.DmDriver";

    public static final String MAVEN_DEPENDENCY = "<dependency>\n" +
            "            <groupId>com.dameng</groupId>\n" +
            "            <artifactId>DmJdbcDriver18</artifactId>\n" +
            "            <version>8.1.1.193</version>\n" +
            "        </dependency>";


    @Override
    public ColumnMeta parseColumn(Map<String, Object> row) {
        ColumnMeta col = new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
        col.setName(asString(row.get("COLUMN_NAME"), null));
        col.setType(asString(row.get("TYPE_NAME"), null));
        if (col.getType() != null) {
            col.setType(col.getType().split("\\(", 2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"), null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"), 0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"), 0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"), null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"), null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"), null))));
        col.setDefaultValue(asString(row.get("COLUMN_DEF"), null));


        StdType type = StdType.VARCHAR;
        DmType dmType = null;
        for (DmType item : DmType.values()) {
            if (item.text().equalsIgnoreCase(col.getType())) {
                type = item.stdType();
                dmType = item;
                break;
            }
        }

        if (dmType == DmType.NUMBER) {
            if (col.getScale() <= 0) {
                int prec = col.getPrecision();
                if (prec <= 3) {
                    type = StdType.TINYINT;
                } else if (prec <= 5) {
                    type = StdType.SMALLINT;
                } else if (prec <= 10) {
                    type = StdType.INT;
                } else if (prec <= 20) {
                    type = StdType.BIGINT;
                }
            }
        }

        String columnType = col.getType();
        if (dmType != null) {
            if (dmType.precision()) {
                columnType += "(" + col.getPrecision();
                if (dmType.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        } else {
            if (type.precision()) {
                columnType += "(" + col.getPrecision();
                if (type.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        }
        col.setColumnType(columnType);

        col.setJavaType(type.javaType().getSimpleName());
        col.setJdbcType(type.jdbcType().getName());
        col.setStdType(type.text());

        col.setLooseJavaType(type.looseJavaType().getSimpleName());
        col.setLooseJdbcType(type.looseJdbcType().getName());
        return col;
    }


}
