/**
 * 抽象工厂模式（Abstract Factory）
 *
 * <p><b>定义：</b>提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。</p>
 * <p><b>分类：</b>创建型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>系统需要独立于产品的创建、组合和表示。</li>
 *   <li>系统需要一组相关产品对象进行组合使用。</li>
 *   <li>强调一系列相关产品对象的设计以便联合使用。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"家具工厂"为场景——不同风格的家具工厂创建风格一致的一整套家具，
 * 现代工厂创建现代风格的椅子 + 桌子，古典工厂创建古典风格的椅子 + 桌子，
 * 客户端面向抽象工厂编程，无需关心具体产品类型，确保产品族风格协调一致。</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.chair.Chair} —— 抽象产品 A（椅子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.table.Table} —— 抽象产品 B（桌子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.chair.impl.ModernChair} —— 具体产品 A1（现代椅子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.chair.impl.ClassicChair} —— 具体产品 A2（古典椅子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.table.impl.ModernTable} —— 具体产品 B1（现代桌子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.table.impl.ClassicTable} —— 具体产品 B2（古典桌子）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory} —— 抽象工厂（家具工厂）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.factory.impl.ModernFurnitureFactory} —— 具体工厂（现代家具）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.factory.impl.ClassicFurnitureFactory} —— 具体工厂（古典家具）</li>
 *   <li>{@link i2f.design.pattern.creational.abstractFactory.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.creational.abstractFactory.chair.Chair
 * @see i2f.design.pattern.creational.abstractFactory.table.Table
 * @see i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory
 * @see i2f.design.pattern.creational.abstractFactory.Test
 */
package i2f.design.pattern.creational.abstractFactory;
