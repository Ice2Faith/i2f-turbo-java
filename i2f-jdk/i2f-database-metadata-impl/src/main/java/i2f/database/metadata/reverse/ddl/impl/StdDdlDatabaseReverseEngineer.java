package i2f.database.metadata.reverse.ddl.impl;

import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.std.StdType;

/**
 * @author Ice2Faith
 * @date 2025/7/30 10:49
 */
public class StdDdlDatabaseReverseEngineer extends DefaultDdlDatabaseReverseEngineer {
    public static final StdDdlDatabaseReverseEngineer INSTANCE = new StdDdlDatabaseReverseEngineer();

    @Override
    public String convertColumnType(ColumnMeta column) {
        StdType stdType = column.getRawStdType();
        if (stdType.precision() && stdType.scale()) {
            return stdType.text() + "(" + column.getPrecision() + ", " + column.getScale() + ")";
        } else if (stdType.precision()) {
            return stdType.text() + "(" + column.getPrecision() + ")";
        }
        return stdType.text();
    }

}
