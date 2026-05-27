package i2f.design.pattern.creational.abstractFactory.factory.impl;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;
import i2f.design.pattern.creational.abstractFactory.chair.impl.ClassicChair;
import i2f.design.pattern.creational.abstractFactory.table.impl.ClassicTable;

/**
 * 抽象工厂模式 —— 古典家具工厂（Concrete Factory：ClassicFurnitureFactory）
 *
 * <p><b>角色：</b>具体工厂（Concrete Factory）</p>
 *
 * <p><b>说明：</b>古典风格家具工厂，实现抽象工厂接口，
 * 返回风格一致的一整套古典家具——古典椅子（{@link ClassicChair}）
 * 搭配古典桌子（{@link ClassicTable}）。
 * 这体现了抽象工厂模式的核心——<b>保证产品族风格的一致性</b>。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"北欧风家具"，
 * 只需新增 {@code NordicFurnitureFactory} 子类并返回对应的北欧风产品族，
 * 无需修改现有任何代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see FurnitureFactory
 * @see ClassicChair
 * @see ClassicTable
 */
public class ClassicFurnitureFactory extends FurnitureFactory {

    /**
     * 创建椅子：古典雕花风格椅子。
     *
     * <p>配置实木雕花工艺、祥云纹饰椅背，古韵典雅，
     * 与古典风格桌子搭配协调统一。</p>
     *
     * @return 配置好的古典椅子实例
     */
    @Override
    public Chair createChair() {
        return new ClassicChair("太师椅", "红木", true, "祥云纹");
    }

    /**
     * 创建桌子：古典雕花风格桌子。
     *
     * <p>配置红木实木桌面、四根雕花桌腿，古朴厚重，
     * 与古典风格椅子搭配协调统一。</p>
     *
     * @return 配置好的古典桌子实例
     */
    @Override
    public Table createTable() {
        return new ClassicTable("八仙桌", "红木", 1.5, 4);
    }

    @Override
    public String getFactoryName() {
        return "檀韵阁 古典家具";
    }
}
