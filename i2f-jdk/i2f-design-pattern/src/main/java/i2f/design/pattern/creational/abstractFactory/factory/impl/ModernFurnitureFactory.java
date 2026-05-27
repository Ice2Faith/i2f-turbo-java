package i2f.design.pattern.creational.abstractFactory.factory.impl;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.chair.impl.ModernChair;
import i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;
import i2f.design.pattern.creational.abstractFactory.table.impl.ModernTable;

/**
 * 抽象工厂模式 —— 现代家具工厂（Concrete Factory：ModernFurnitureFactory）
 *
 * <p><b>角色：</b>具体工厂（Concrete Factory）</p>
 *
 * <p><b>说明：</b>现代风格家具工厂，实现抽象工厂接口，
 * 返回风格一致的一整套现代家具——现代椅子（{@link ModernChair}）
 * 搭配现代桌子（{@link ModernTable}）。
 * 这体现了抽象工厂模式的核心——<b>保证产品族风格的一致性</b>。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"工业风家具"，
 * 只需新增 {@code IndustrialFurnitureFactory} 子类并返回对应的工业风产品族，
 * 无需修改现有任何代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see FurnitureFactory
 * @see ModernChair
 * @see ModernTable
 */
public class ModernFurnitureFactory extends FurnitureFactory {

    /**
     * 创建椅子：现代简约风格椅子。
     *
     * <p>配置不锈钢管框架、人造革坐垫，简约舒适，
     * 与现代风格桌子搭配协调统一。</p>
     *
     * @return 配置好的现代椅子实例
     */
    @Override
    public Chair createChair() {
        return new ModernChair("极简扶手椅", "不锈钢管", true, true);
    }

    /**
     * 创建桌子：现代简约风格桌子。
     *
     * <p>配置钢化玻璃台面、金属框架，通透简洁，
     * 与现代风格椅子搭配协调统一。</p>
     *
     * @return 配置好的现代桌子实例
     */
    @Override
    public Table createTable() {
        return new ModernTable("升降办公桌", "钢化玻璃", 1.2, true);
    }

    @Override
    public String getFactoryName() {
        return "MUJI 现代家具";
    }
}
