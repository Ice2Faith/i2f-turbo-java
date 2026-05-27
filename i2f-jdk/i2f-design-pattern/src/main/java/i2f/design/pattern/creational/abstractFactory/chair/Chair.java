package i2f.design.pattern.creational.abstractFactory.chair;

import i2f.design.pattern.creational.abstractFactory.chair.impl.ClassicChair;
import i2f.design.pattern.creational.abstractFactory.chair.impl.ModernChair;
import i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 椅子（Abstract Product A：Chair）
 *
 * <p><b>角色：</b>抽象产品 A（Abstract Product A）</p>
 *
 * <p><b>模式说明：</b>定义"椅子"这一产品族成员的公共接口和通用属性。
 * 不同风格的具体工厂将创建不同风格的椅子实现，但客户端仅依赖此抽象类型，
 * 无需感知是现代椅子还是古典椅子。</p>
 *
 * <p><b>命名立意：</b>以"家具工厂"为场景——
 * 椅子（Chair）与桌子（{@link Table}）构成一组<b>相关产品族</b>，
 * 同一风格工厂生产的椅子与桌子在风格上必须协调一致，
 * 这正是抽象工厂模式解决的核心问题：<b>保证产品族的一致性</b>。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象产品 A             具体产品 A
 *  ─────────────────    ─────────────────────────────
 *  Chair                 ModernChair（现代椅子 —— 简约钢架）
 *                        ClassicChair（古典椅子 —— 雕花实木）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see ModernChair
 * @see ClassicChair
 * @see Table
 * @see FurnitureFactory
 */
@Data
@NoArgsConstructor
public abstract class Chair {

    /**
     * 椅子名称。
     */
    private String name;

    /**
     * 材质。
     */
    private String material;

    /**
     * 是否有扶手。
     */
    private boolean hasArmrest;

    public Chair(String name, String material, boolean hasArmrest) {
        this.name = name;
        this.material = material;
        this.hasArmrest = hasArmrest;
    }

    /**
     * 坐下——描述坐在椅子上的体验。
     *
     * <p>不同风格的椅子提供不同的落座体验，由子类提供具体实现。</p>
     *
     * @return 落座体验描述
     */
    public abstract String sitDown();

    /**
     * 获取椅子风格描述。
     *
     * @return 风格名称
     */
    public abstract String getStyle();
}
