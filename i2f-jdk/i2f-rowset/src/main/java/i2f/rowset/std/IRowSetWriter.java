package i2f.rowset.std;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:05
 * @desc
 */
public interface IRowSetWriter<T> {
    void write(IRowSet<T> rowSet, OutputStream os) throws IOException;

    default void write(IRowSet<T> rowSet, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        write(rowSet, fos);
        fos.close();
    }
}
