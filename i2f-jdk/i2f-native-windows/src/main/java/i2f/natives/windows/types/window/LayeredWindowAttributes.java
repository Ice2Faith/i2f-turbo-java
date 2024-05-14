package i2f.natives.windows.types.window;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 9:32
 * @desc
 */
@Data
public class LayeredWindowAttributes {
    public int color;
    public byte alpha;
    public long flag;

    public LayeredWindowAttributes() {
    }

    public LayeredWindowAttributes(int color, byte alpha, long flag) {
        this.color = color;
        this.alpha = alpha;
        this.flag = flag;
    }
}
