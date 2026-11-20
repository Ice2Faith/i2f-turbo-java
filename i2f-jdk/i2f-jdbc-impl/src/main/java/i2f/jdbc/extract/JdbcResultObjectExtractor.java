package i2f.jdbc.extract;

import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.ResultSetContext;
import i2f.reference.Reference;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2026/7/2 10:55
 * @desc
 */
public interface JdbcResultObjectExtractor {
    boolean support(ResultSetContext rsCtx) throws SQLException;

    Reference<Object> extract(ResultSetContext rsCtx, int columnIndex, QueryColumn column) throws SQLException;
}
