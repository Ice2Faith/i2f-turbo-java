package i2f.natives.windows.types.file;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/11 17:09
 * @desc
 */
@Data
public class FileAttributeExInfo {
    public long dwFileAttributes;
    public long ftCreationTime;
    public long ftLastAccessTime;
    public long ftLastWriteTime;
    public long nFileSize;
}
