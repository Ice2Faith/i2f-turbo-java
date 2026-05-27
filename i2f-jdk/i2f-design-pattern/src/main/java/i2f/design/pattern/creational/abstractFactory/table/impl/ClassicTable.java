package i2f.design.pattern.creational.abstractFactory.table.impl;

import i2f.design.pattern.creational.abstractFactory.chair.impl.ClassicChair;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ClassicFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 古典桌子（Concrete Product B2：ClassicTable）
 *
 * <p><b>角色：</b>具体产品 B2（Concrete Product B2）</p>
 *
 * <p><b>说明：</b>古典风格桌子，采用红木实木打造，桌面雕有祥云纹饰，
 * 古朴厚重、典雅大气。由 {@link ClassicFurnitureFactory} 创建，
 * 与 {@link ClassicChair} 配套使用，确保风格一致。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Table
 * @see ClassicFurnitureFactory
 * @see ClassicChair
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassicTable extends Table {

    /**
     * 桌腿数量。
     */
    private int legCount;

    public ClassicTable(String name, String material, double area, int legCount) {
        super(name, material, area);
        this.legCount = legCount;
    }

    @Override
    public String placeItem(String item) {
        return String.format("[古典桌·%s] %.1f㎡ 的%s桌面纹理自然，%d 根实木桌腿稳固沉稳，" +
                        "放置「%s」别有一番雅致",
                getName(), getArea(), getMaterial(), legCount, item);
    }

    @Override
    public String getStyle() {
        return "古典雕花";
    }

    @Override
    public String toString() {
        return String.format("ClassicTable{name='%s', material='%s', area=%.1f㎡, legs=%d}",
                getName(), getMaterial(), getArea(), legCount);
    }
}
