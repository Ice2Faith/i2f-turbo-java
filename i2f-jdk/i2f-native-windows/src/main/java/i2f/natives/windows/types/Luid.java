package i2f.natives.windows.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 9:24
 * @desc
 */
@Data
public class Luid {
    public long lowPart;
    public long highPart;

    public Luid() {
    }

    public Luid(long lowPart, long highPart) {
        this.lowPart = lowPart;
        this.highPart = highPart;
    }
}
