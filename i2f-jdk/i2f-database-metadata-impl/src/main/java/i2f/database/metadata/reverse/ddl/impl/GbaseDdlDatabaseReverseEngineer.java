package i2f.database.metadata.reverse.ddl.impl;


import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.impl.mysql.MySqlType;
import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2025/7/30 11:10
 */
public class GbaseDdlDatabaseReverseEngineer extends DefaultDdlDatabaseReverseEngineer {
    public static final GbaseDdlDatabaseReverseEngineer INSTANCE = new GbaseDdlDatabaseReverseEngineer();

    public static final GbaseDdlDatabaseReverseEngineer CONVERT = new GbaseDdlDatabaseReverseEngineer() {

        @Override
        public String convertColumnType(ColumnMeta column) {
            Object rawDialectType = column.getRawDialectType();
            if (rawDialectType instanceof MySqlType) {
                return column.getColumnType();
            }
            StdType stdType = column.getRawStdType();
            MySqlType type = MySqlType.ofStd(stdType);
            if (type.precision() && type.scale()) {
                return type.text() + "(" + column.getPrecision() + ", " + column.getScale() + ")";
            } else if (type.precision()) {
                return type.text() + "(" + column.getPrecision() + ")";
            }
            return type.text();
        }


        @Override
        public String keyword(String str) {
            return str == null ? null : str.toLowerCase();
        }

        @Override
        public String tableName(String str) {
            return str == null ? null : str.toLowerCase();
        }

        @Override
        public String columnName(String str) {
            return str == null ? null : str.toLowerCase();
        }
    };
}
