package i2f.natives.windows.easyx.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/13 10:10
 * @desc
 */
@Data
public class MouseMsg {
    public int uMsg;                // 当前鼠标消息
    public boolean mkCtrl;        // Ctrl 键是否按下
    public boolean mkShift;        // Shift 键是否按下
    public boolean mkLButton;        // 鼠标左键是否按下
    public boolean mkMButton;        // 鼠标中键是否按下
    public boolean mkRButton;        // 鼠标右键是否按下
    public int x;                // 当前鼠标 x 坐标
    public int y;                // 当前鼠标 y 坐标
    public int wheel;            // 鼠标滚轮滚动值 (120 的倍数)
}
