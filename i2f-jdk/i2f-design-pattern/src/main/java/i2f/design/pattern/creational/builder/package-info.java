/**
 * 建造者模式（Builder）
 *
 * <p><b>定义：</b>将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。</p>
 * <p><b>分类：</b>创建型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>创建对象需要很多步骤，且步骤顺序固定但参数可变。</li>
 *   <li>构造函数参数过多，希望通过链式调用提升可读性。</li>
 *   <li>同一构建过程可生成不同的产品表示。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"电脑组装"为场景——指挥者（{@link i2f.design.pattern.creational.builder.Director}）定义标准组装流程，
 * 不同建造者（{@link Builder} 的实现类）决定具体配置，
 * 同样的组装流程（装CPU → 装内存 → 装硬盘 → 装显卡 → 装显示器 → 装SSD）
 * 可产出不同配置的电脑（办公电脑 vs 游戏电脑），体现"构建过程与表示分离"的核心思想。</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.creational.builder.Computer} —— 产品（被建造的复杂对象）</li>
 *   <li>{@link i2f.design.pattern.creational.builder.Builder} —— 抽象建造者（定义构建步骤接口）</li>
 *   <li>{@link i2f.design.pattern.creational.builder.impl.OfficeComputerBuilder} —— 具体建造者（办公电脑）</li>
 *   <li>{@link i2f.design.pattern.creational.builder.impl.GamingComputerBuilder} —— 具体建造者（游戏电脑）</li>
 *   <li>{@link i2f.design.pattern.creational.builder.Director} —— 指挥者（定义构建流程）</li>
 *   <li>{@link i2f.design.pattern.creational.builder.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.creational.builder.Computer
 * @see i2f.design.pattern.creational.builder.Builder
 * @see i2f.design.pattern.creational.builder.Director
 * @see i2f.design.pattern.creational.builder.Test
 */
package i2f.design.pattern.creational.builder;
