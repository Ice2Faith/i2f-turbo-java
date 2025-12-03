package i2f.rowset.std;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:12
 * @desc
 */
public interface IRowSet<T> extends Iterator<T>, Closeable {
    List<IRowHeader> getHeaders();

    @Override
    default void close() throws IOException {

    }
}
