package i2f.jdbc.extract.impl;

import i2f.database.type.DatabaseType;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.extract.DatabaseTypeResultObjectExtractor;
import i2f.reference.Reference;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Ice2Faith
 * @date 2026/7/2 11:12
 * @desc
 */
public class SqliteResultObjectExtractor extends DatabaseTypeResultObjectExtractor {
    public static final SqliteResultObjectExtractor INSTANCE = new SqliteResultObjectExtractor();

    @Override
    public boolean support(DatabaseType databaseType) {
        return DatabaseType.SQLITE == databaseType;
    }

    @Override
    public Reference<Object> extract(ResultSet rs, int columnIndex, QueryColumn column) throws SQLException {
        int jdbcType = column.getType();
        if (Types.BLOB == jdbcType) {
            return Reference.of(rs.getBytes(columnIndex));
        }
        return Reference.nop();
    }
}
