package i2f.natives.windows.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 17:50
 * @desc
 */
@Data
public class RegEnumValueInfo {
    public int index;
    public String valueName;
    public int type;
    public String valueData;
}