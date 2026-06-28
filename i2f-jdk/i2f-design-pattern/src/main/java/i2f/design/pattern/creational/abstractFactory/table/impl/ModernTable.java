package i2f.design.pattern.creational.abstractFactory.table.impl;

import i2f.design.pattern.creational.abstractFactory.chair.impl.ModernChair;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ModernFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 现代桌子（Concrete Product B1：ModernTable）
 *
 * <p><b>角色：</b>具体产品 B1（Concrete Product B1）</p>
 *
 * <p><b>说明：</b>现代风格桌子，采用钢化玻璃台面搭配金属框架，
 * 通透简洁、几何感强。由 {@link ModernFurnitureFactory} 创建，
 * 与 {@link ModernChair} 配套使用，确保风格一致。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Table
 * @see ModernFurnitureFactory
 * @see ModernChair
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModernTable extends Table {

    /**
     * 是否支持高度调节。
     */
    private boolean adjustable;

    public ModernTable(String name, String material, double area, boolean adjustable) {
        super(name, material, area);
        this.adjustable = adjustable;
    }

    @Override
    public String placeItem(String item) {
        return String.format("[现代桌·%s] %.1f㎡ 的%s台面%s，放置「%s」显得科技感十足",
                getName(), getArea(), getMaterial(),
                adjustable ? "可升降调节" : "线条利落", item);
    }

    @Override
    public String getStyle() {
        return "现代简约";
    }

    @Override
    public String toString() {
        return String.format("ModernTable{name='%s', material='%s', area=%.1f㎡, adjustable=%b}",
                getName(), getMaterial(), getArea(), adjustable);
    }
}
