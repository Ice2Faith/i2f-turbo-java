package i2f.design.pattern.structural.flyweight.context;

import i2f.design.pattern.structural.flyweight.character.Char;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 享元模式 —— 文本上下文（Context：TextContext）
 *
 * <p><b>角色：</b>上下文（Context）</p>
 *
 * <p><b>模式说明：</b>存储享元对象的外部状态（不可共享的状态）。
 * 在享元模式中，外部状态由客户端管理，不存储在享元对象内部。
 * 对于文字排版场景，外部状态包括：字符的位置、颜色、字体、大小等。</p>
 *
 * <p><b>命名立意：</b>"文本上下文"保存每个字符的渲染信息——
 * 同一字符 'A' 可以在不同位置、以不同颜色/大小显示，
 * 这些差异由上下文管理，而非为每个变体创建新的享元对象。</p>
 *
 * <p><b>内部状态 vs 外部状态示例：</b></p>
 * <pre>
 *  享元对象（Character）持有：    上下文（TextContext）持有：
 *  ──────────────────────────   ──────────────────────────
 *  symbol = 'A'（内部状态）       x = 100, y = 200（位置）
 *                               color = "RED"（颜色）
 *                               fontSize = 14（字号）
 *                               fontFamily = "Arial"（字体）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Char
 */
@Data
@NoArgsConstructor
public class TextContext {

    /**
     * X 坐标位置。
     */
    private int x;

    /**
     * Y 坐标位置。
     */
    private int y;

    /**
     * 字符颜色。
     */
    private String color;

    /**
     * 字号（pt）。
     */
    private int fontSize;

    /**
     * 字体名称。
     */
    private String fontFamily;

    /**
     * 构造函数：创建文本上下文。
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param color 颜色
     * @param fontSize 字号
     * @param fontFamily 字体
     */
    public TextContext(int x, int y, String color, int fontSize, String fontFamily) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }

    /**
     * 构造函数：创建文本上下文（使用默认字体）。
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param color 颜色
     * @param fontSize 字号
     */
    public TextContext(int x, int y, String color, int fontSize) {
        this(x, y, color, fontSize, "Arial");
    }

    @Override
    public String toString() {
        return String.format("TextContext{x=%d, y=%d, color='%s', fontSize=%dpt, fontFamily='%s'}",
                x, y, color, fontSize, fontFamily);
    }
}
