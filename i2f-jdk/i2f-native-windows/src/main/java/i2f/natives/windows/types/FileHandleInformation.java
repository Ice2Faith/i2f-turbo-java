package i2f.natives.windows.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/11 17:21
 * @desc
 */
@Data
public class FileHandleInformation {
    public long dwFileAttributes;
    public long ftCreationTime;
    public long ftLastAccessTime;
    public long ftLastWriteTime;
    public long nFileSize;
    public long dwVolumeSerialNumber;
    public long nNumberOfLinks;
    public long nFileIndex;
}
