package i2f.jdbc.cursor;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/6/25 16:10
 */
public interface JdbcCursor<E> extends Closeable, Iterable<E> {

    Statement getStatement();

    ResultSet getResultSet();

    void dispose() throws SQLException;

    boolean hasRow() throws SQLException;

    E nextRow() throws SQLException;

    default List<E> nextCount(int maxCount) throws SQLException {
        return nextCount(maxCount, new LinkedList<>());
    }

    default <C extends Collection<E>> C nextCount(int maxCount, C collection) throws SQLException {
        int count=0;
        while ((maxCount<0 || count<maxCount) && hasRow()) {
            E elem = nextRow();
            collection.add(elem);
            count++;
        }

        return collection;
    }

    @Override
    default void close() throws IOException {
        try {
            dispose();
        } catch (SQLException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    default Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                try {
                    return hasRow();
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }

            @Override
            public E next() {
                try {
                    return nextRow();
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        };
    }
}
