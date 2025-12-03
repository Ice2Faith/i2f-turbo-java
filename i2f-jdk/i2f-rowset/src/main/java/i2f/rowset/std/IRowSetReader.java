package i2f.rowset.std;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2025/12/3 15:15
 * @desc
 */
public interface IRowSetReader<T> {
    IRowSet<T> read(InputStream is) throws IOException;
    default IRowSet<T> read(File file) throws IOException{
        return read(new FileInputStream(file));
    }
}
