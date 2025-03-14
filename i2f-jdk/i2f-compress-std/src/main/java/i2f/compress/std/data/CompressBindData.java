package i2f.compress.std.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/31 15:31
 * @desc
 */
@Data
@NoArgsConstructor
public class CompressBindData {
    private String fileName;
    private String directory;
    private InputStream inputStream;
    private long size = -1;

    public CompressBindData(String fileName, String directory, InputStream inputStream) {
        this.fileName = fileName;
        this.directory = directory;
        this.inputStream = inputStream;
    }

    public CompressBindData(String fileName, String directory, InputStream inputStream, long size) {
        this.fileName = fileName;
        this.directory = directory;
        this.inputStream = inputStream;
        this.size = size;
    }

    public static CompressBindData of(CompressBindFile bindFile) throws IOException {
        File file = bindFile.getFile();
        InputStream is = null;
        if (file.isFile()) {
            is = new FileInputStream(file);
        }
        return new CompressBindData(file.getName(), bindFile.getDirectory(), is, file.length());
    }

    public static CompressBindData instance() {
        return new CompressBindData();
    }
}
