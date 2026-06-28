package i2f.design.pattern.creational.abstractFactory.factory;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ClassicFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ModernFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;

/**
 * 抽象工厂模式 —— 家具工厂（Abstract Factory：FurnitureFactory）
 *
 * <p><b>角色：</b>抽象工厂（Abstract Factory）</p>
 *
 * <p><b>模式说明：</b>定义创建<b>一系列相关产品</b>的接口——
 * {@link #createChair()} 创建椅子、{@link #createTable()} 创建桌子，
 * 但不指定具体类型。具体工厂子类决定创建哪种风格的产品族，
 * 客户端面向此抽象工厂编程，即可获得风格一致的全套家具。</p>
 *
 * <p><b>命名立意：</b>"家具工厂"天然对应"抽象工厂"角色——
 * 现代家具工厂（{@link ModernFurnitureFactory}）生产现代风格的椅子与桌子，
 * 古典家具工厂（{@link ClassicFurnitureFactory}）生产古典风格的椅子与桌子。
 * 客户端只需告诉工厂"我要一套家具"，至于什么风格，由具体工厂决定。</p>
 *
 * <p><b>与工厂方法模式的区别：</b></p>
 * <pre>
 *  工厂方法（Factory Method）               抽象工厂（Abstract Factory）
 *  ─────────────────────────────────────   ─────────────────────────────────────
 *  一个工厂方法创建一种产品                   一个工厂接口创建一族相关产品
 *  关注单个产品的创建                        关注产品族的整体一致性
 *  通过继承扩展（子类重写工厂方法）            通过组合扩展（切换不同的工厂实例）
 *  新增产品类型 → 新增工厂子类                新增产品族 → 新增工厂实现；新增产品种类 → 修改接口（违反开闭）
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  AbstractFactory（抽象工厂）
 *    ├─ createChair(): Chair       ← 创建产品 A
 *    └─ createTable(): Table       ← 创建产品 B
 *
 *  ConcreteFactory（具体工厂）
 *    ├─ ModernFurnitureFactory   → 创建 ModernChair + ModernTable
 *    └─ ClassicFurnitureFactory  → 创建 ClassicChair + ClassicTable
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Chair
 * @see Table
 * @see ModernFurnitureFactory
 * @see ClassicFurnitureFactory
 */
public abstract class FurnitureFactory {

    /**
     * 创建椅子（产品 A）。
     *
     * <p>具体工厂子类实现此方法，决定创建哪种风格的椅子。</p>
     *
     * @return 具体风格的椅子实例
     */
    public abstract Chair createChair();

    /**
     * 创建桌子（产品 B）。
     *
     * <p>具体工厂子类实现此方法，决定创建哪种风格的桌子。</p>
     *
     * @return 具体风格的桌子实例
     */
    public abstract Table createTable();

    /**
     * 布置房间——使用一整套配套家具装饰指定房间。
     *
     * <p>该方法体现了抽象工厂模式的核心价值：
     * 通过同一工厂创建的椅子与桌子<b>风格必然一致</b>，
     * 客户端无需关心具体是哪种风格，只需调用此方法即可获得协调的家具搭配。</p>
     *
     * @param roomName 房间名称
     */
    public void furnishRoom(String roomName) {
        Chair chair = createChair();
        Table table = createTable();

        System.out.println("  房间：" + roomName);
        System.out.println("  工厂：" + getFactoryName());
        System.out.println("  椅子：" + chair + " | 风格：" + chair.getStyle());
        System.out.println("  桌子：" + table + " | 风格：" + table.getStyle());
        System.out.println("  落座体验：" + chair.sitDown());
        System.out.println("  桌面使用：" + table.placeItem("一杯咖啡"));
    }

    /**
     * 获取工厂名称。
     *
     * @return 工厂名称
     */
    public abstract String getFactoryName();
}
