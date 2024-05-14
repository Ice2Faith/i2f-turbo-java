package i2f.natives.windows.types.file;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/12 18:15
 * @desc
 */
@Data
public class DiskFreeSpaceExInfo {
    public long freeBytesAvailableToCaller;
    public long totalNumberOfBytes;
    public long totalNumberOfFreeBytes;
}
