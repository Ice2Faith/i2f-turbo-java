package i2f.natives.windows.types.window;

import i2f.graphics.d2.shape.Rectangle;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/10 11:40
 * @desc
 */
@Data
public class WindowInfo {
    public int cbSize;
    public Rectangle rcWindow;
    public Rectangle rcClient;
    public long dwStyle;
    public long dwExStyle;
    public long dwWindowStatus;
    public long cxWindowBorders;
    public long cyWindowBorders;
    public int atomWindowType;
    public int wCreatorVersion;
}
