/**
 * 原型模式（Prototype）
 *
 * <p><b>定义：</b>用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象。</p>
 * <p><b>分类：</b>创建型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>创建对象成本较高（如初始化耗时长、依赖资源多），希望通过拷贝快速创建。</li>
 *   <li>需要避免使用复杂的构造函数体系。</li>
 *   <li>需要保留对象在某一时刻的状态（快照）。</li>
 * </ul>
 *
 * <p><b>本包演示：</b></p>
 * <ul>
 *   <li>{@link i2f.design.pattern.creational.prototype.Sheep} —— 克隆羊原型类（浅拷贝 + 深拷贝）</li>
 *   <li>{@link i2f.design.pattern.creational.prototype.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.creational.prototype.Sheep
 * @see i2f.design.pattern.creational.prototype.Test
 */
package i2f.design.pattern.creational.prototype;
