package i2f.design.pattern.creational.abstractFactory.chair.impl;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ModernFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.impl.ModernTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 现代椅子（Concrete Product A1：ModernChair）
 *
 * <p><b>角色：</b>具体产品 A1（Concrete Product A1）</p>
 *
 * <p><b>说明：</b>现代风格椅子，采用不锈钢管框架搭配人造革坐垫，
 * 线条简约、功能至上。由 {@link ModernFurnitureFactory} 创建，
 * 与 {@link ModernTable} 配套使用，确保风格一致。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Chair
 * @see ModernFurnitureFactory
 * @see ModernTable
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModernChair extends Chair {

    /**
     * 是否支持旋转。
     */
    private boolean swivel;

    public ModernChair(String name, String material, boolean hasArmrest, boolean swivel) {
        super(name, material, hasArmrest);
        this.swivel = swivel;
    }

    @Override
    public String sitDown() {
        return String.format("[现代椅·%s] 坐上去感觉简约舒适，%s钢架支撑稳固，%s",
                getName(), getMaterial(),
                swivel ? "还能 360° 旋转" : "造型简洁大方");
    }

    @Override
    public String getStyle() {
        return "现代简约";
    }

    @Override
    public String toString() {
        return String.format("ModernChair{name='%s', material='%s', armrest=%b, swivel=%b}",
                getName(), getMaterial(), isHasArmrest(), swivel);
    }
}
