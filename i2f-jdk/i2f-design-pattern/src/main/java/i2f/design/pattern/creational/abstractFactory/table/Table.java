package i2f.design.pattern.creational.abstractFactory.table;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.impl.ClassicTable;
import i2f.design.pattern.creational.abstractFactory.table.impl.ModernTable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽象工厂模式 —— 桌子（Abstract Product B：Table）
 *
 * <p><b>角色：</b>抽象产品 B（Abstract Product B）</p>
 *
 * <p><b>模式说明：</b>定义"桌子"这一产品族成员的公共接口和通用属性。
 * 桌子与椅子（{@link Chair}）共同构成一组<b>相关产品族</b>，
 * 由同一抽象工厂（{@link FurnitureFactory}）统一创建，确保风格协调。</p>
 *
 * <p><b>命名立意：</b>以"家具工厂"为场景——
 * 现代风格工厂生产的桌子（{@link ModernTable}）搭配现代椅子，
 * 古典风格工厂生产的桌子（{@link ClassicTable}）搭配古典椅子，
 * 二者<b>不应混搭</b>——这正体现了抽象工厂保证产品族一致性的设计意图。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象产品 B             具体产品 B
 *  ─────────────────    ─────────────────────────────
 *  Table                 ModernTable（现代桌子 —— 玻璃台面）
 *                        ClassicTable（古典桌子 —— 红木雕花）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see ModernTable
 * @see ClassicTable
 * @see Chair
 * @see FurnitureFactory
 */
@Data
@NoArgsConstructor
public abstract class Table {

    /**
     * 桌子名称。
     */
    private String name;

    /**
     * 材质。
     */
    private String material;

    /**
     * 桌面面积（平方米）。
     */
    private double area;

    public Table(String name, String material, double area) {
        this.name = name;
        this.material = material;
        this.area = area;
    }

    /**
     * 摆放物品——描述在桌子上放置物品的效果。
     *
     * <p>不同风格的桌子有不同的使用场景与视觉效果，由子类提供具体实现。</p>
     *
     * @param item 要放置的物品
     * @return 放置效果描述
     */
    public abstract String placeItem(String item);

    /**
     * 获取桌子风格描述。
     *
     * @return 风格名称
     */
    public abstract String getStyle();
}
