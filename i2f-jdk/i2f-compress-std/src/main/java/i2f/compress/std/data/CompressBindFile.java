package i2f.compress.std.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2022/3/31 15:41
 * @desc
 */
@Data
@NoArgsConstructor
public class CompressBindFile {
    private File file;
    private String directory;

    public CompressBindFile(File file, String directory) {
        this.file = file;
        this.directory = directory;
    }

    public static CompressBindFile instance() {
        return new CompressBindFile();
    }
}
