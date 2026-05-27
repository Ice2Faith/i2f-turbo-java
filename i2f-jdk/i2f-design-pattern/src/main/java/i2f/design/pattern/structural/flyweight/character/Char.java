package i2f.design.pattern.structural.flyweight.character;

import i2f.design.pattern.structural.flyweight.context.TextContext;
import i2f.design.pattern.structural.flyweight.character.impl.ConcreteChar;

/**
 * 享元模式 —— 字符（Flyweight：Character）
 *
 * <p><b>角色：</b>抽象享元（Flyweight）</p>
 *
 * <p><b>模式说明：</b>定义享元对象的公共接口，所有具体享元都实现此接口。
 * 享元模式通过共享技术减少重复对象的数量——相同的字符（如字母 'A'）
 * 在文档中可能出现成千上万次，但内存中只保留一份实例。</p>
 *
 * <p><b>命名立意：</b>以"文字排版系统"为场景——文档中每个字符都是享元对象，
 * 相同字符共享同一实例（内部状态），而位置、颜色、字体等外部状态由上下文管理。
 * 这大幅减少了内存占用，特别适用于富文本编辑器、文档处理器等场景。</p>
 *
 * <p><b>内部状态 vs 外部状态：</b></p>
 * <pre>
 *  内部状态（Intrinsic State）  外部状态（Extrinsic State）
 *  ─────────────────────────   ─────────────────────────
 *  存储在享元对象内部             由客户端传入，不存储在享元中
 *  可以共享（字符本身）           不可共享（位置、颜色、大小）
 *  创建后不会改变                随使用场景变化
 * </pre>
 *
 * <p><b>与工厂方法的区别：</b></p>
 * <pre>
 *  工厂方法（Factory Method）           享元模式（Flyweight）
 *  ────────────────────────────────   ────────────────────────────────
 *  每次调用都创建新实例                  相同键值返回共享实例
 *  关注对象创建的解耦                    关注对象共享与内存优化
 *  产品通常不可共享                      享元必须可共享
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see ConcreteChar
 * @see TextContext
 */
public abstract class Char {

    /**
     * 渲染字符到指定位置。
     *
     * <p>此方法体现了享元模式的核心特征：
     * 享元对象本身不包含外部状态（位置、颜色等），
     * 这些状态由调用方通过参数传入。</p>
     *
     * @param context 文本上下文（包含位置、颜色、字体等外部状态）
     */
    public abstract void render(TextContext context);

    /**
     * 获取字符本身（内部状态）。
     *
     * @return 字符值
     */
    public abstract char getSymbol();
}
