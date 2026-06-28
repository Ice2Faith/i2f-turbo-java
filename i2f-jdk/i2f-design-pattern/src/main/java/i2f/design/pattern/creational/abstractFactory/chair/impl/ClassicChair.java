package i2f.design.pattern.creational.abstractFactory.chair.impl;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ClassicFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.impl.ClassicTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 古典椅子（Concrete Product A2：ClassicChair）
 *
 * <p><b>角色：</b>具体产品 A2（Concrete Product A2）</p>
 *
 * <p><b>说明：</b>古典风格椅子，采用实木雕花工艺，椅背配有精美纹饰，
 * 展现传统匠心之美。由 {@link ClassicFurnitureFactory} 创建，
 * 与 {@link ClassicTable} 配套使用，确保风格一致。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Chair
 * @see ClassicFurnitureFactory
 * @see ClassicTable
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassicChair extends Chair {

    /**
     * 雕花图案描述。
     */
    private String carvingPattern;

    public ClassicChair(String name, String material, boolean hasArmrest, String carvingPattern) {
        super(name, material, hasArmrest);
        this.carvingPattern = carvingPattern;
    }

    @Override
    public String sitDown() {
        return String.format("[古典椅·%s] 坐上去感受到%s的温润质感，椅背「%s」雕花尽显古韵",
                getName(), getMaterial(), carvingPattern);
    }

    @Override
    public String getStyle() {
        return "古典雕花";
    }

    @Override
    public String toString() {
        return String.format("ClassicChair{name='%s', material='%s', armrest=%b, carving='%s'}",
                getName(), getMaterial(), isHasArmrest(), carvingPattern);
    }
}
