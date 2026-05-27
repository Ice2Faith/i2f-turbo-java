/**
 * 享元模式（Flyweight）
 * <p>
 * 运用共享技术有效地支持大量细粒度的对象，通过共享内部状态减少内存占用。
 * 分类：结构型模式
 * </p>
 * 
 * <p>本包以"文字排版系统"为场景演示享元模式：</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.structural.flyweight.character.Char} - 抽象享元角色（字符）</li>
 *   <li>{@link i2f.design.pattern.structural.flyweight.character.impl.ConcreteChar} - 具体享元角色（具体字符）</li>
 *   <li>{@link i2f.design.pattern.structural.flyweight.factory.CharacterFactory} - 享元工厂（管理字符池）</li>
 *   <li>{@link i2f.design.pattern.structural.flyweight.context.TextContext} - 上下文（外部状态）</li>
 *   <li>{@link i2f.design.pattern.structural.flyweight.Test} - 调用演示</li>
 * </ul>
 * 
 * @author Ice2Faith
 * @date 2026/5/21
 */
package i2f.design.pattern.structural.flyweight;
