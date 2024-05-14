package i2f.natives.windows.types.register;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/11 9:01
 * @desc
 */
@Data
public class RegEnumKeyExInfo {
    public int index;
    public String keyName;
    public String className;
    public long lastWriteTime;
}
