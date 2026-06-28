package i2f.design.pattern.structural.decorator.coffee;

import i2f.design.pattern.structural.decorator.coffee.decorator.CoffeeDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.CreamDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.MilkDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.SugarDecorator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 咖啡（Component：Coffee）
 *
 * <p><b>角色：</b>抽象组件（Abstract Component）</p>
 *
 * <p><b>模式说明：</b>定义咖啡的基本接口,装饰器和具体咖啡都实现此接口。
 * 装饰器模式的核心在于：装饰器持有组件引用，可以在调用组件方法前后添加额外行为，
 * 实现功能的动态叠加，而无需修改原有类。</p>
 *
 * <p><b>命名立意：</b>以"咖啡饮品"为场景——基础咖啡（浓缩咖啡）可以被各种配料装饰
 * （加牛奶、加糖、加奶油等），每添加一种配料，咖啡的价格和描述都会动态变化。
 * 这完美体现了装饰器模式"动态添加职责"的特点。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象组件              具体组件                 抽象装饰器            具体装饰器
 *  ─────────────────   ───────────────────────   ─────────────────   ─────────────────────────────
 *  Coffee               Espresso（浓缩咖啡）       CoffeeDecorator     MilkDecorator（加牛奶）
 *                                                CoffeeDecorator     SugarDecorator（加糖）
 *                                                CoffeeDecorator     CreamDecorator（加奶油）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Espresso
 * @see CoffeeDecorator
 * @see MilkDecorator
 * @see SugarDecorator
 * @see CreamDecorator
 */
@Data
@NoArgsConstructor
public abstract class Coffee {

    /**
     * 咖啡名称。
     */
    private String name;

    /**
     * 咖啡基础价格（元）。
     */
    private double price;

    public Coffee(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * 获取咖啡描述。
     *
     * <p>返回咖啡的完整描述信息，包括所有装饰的配料。</p>
     *
     * @return 咖啡描述
     */
    public abstract String getDescription();

    /**
     * 计算咖啡总价。
     *
     * <p>基础咖啡价格加上所有装饰配料的价格。</p>
     *
     * @return 总价
     */
    public abstract double getCost();
}
