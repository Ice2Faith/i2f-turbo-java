package i2f.design.pattern.structural.decorator.coffee.decorator;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.CreamDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.MilkDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.SugarDecorator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 咖啡装饰器（Decorator：CoffeeDecorator）
 *
 * <p><b>角色：</b>抽象装饰器（Abstract Decorator）</p>
 *
 * <p><b>模式说明：</b>持有 {@link Coffee} 组件的引用，实现与组件相同的接口。
 * 这是装饰器模式的关键：装饰器既是 Coffee（可以被客户使用），
 * 又包含 Coffee（可以装饰其他咖啡对象）。</p>
 *
 * <p><b>核心机制：</b></p>
 * <pre>
 *  1. 装饰器继承 Coffee，保证接口一致性
 *  2. 装饰器持有 Coffee 引用，实现功能委派
 *  3. 子类在委派前后添加额外行为（如增加价格、修改描述）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Coffee
 * @see MilkDecorator
 * @see SugarDecorator
 * @see CreamDecorator
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class CoffeeDecorator extends Coffee {

    /**
     * 被装饰的咖啡组件。
     *
     * <p>装饰器通过此引用将请求委派给被装饰对象，
     * 并在委派前后添加额外的职责。</p>
     */
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee, String name, double price) {
        super(name, price);
        this.coffee = coffee;
    }

    /**
     * 获取咖啡描述。
     *
     * <p>默认实现：先获取被装饰咖啡的描述，再追加自己的描述。
     * 子类可以覆盖此方法实现不同的描述策略。</p>
     *
     * @return 咖啡完整描述
     */
    @Override
    public String getDescription() {
        return coffee.getDescription() + " + " + getName();
    }

    /**
     * 计算咖啡总价。
     *
     * <p>默认实现：被装饰咖啡的价格 + 装饰配料的价格。
     * 这体现了装饰器模式"层层叠加"的特性。</p>
     *
     * @return 累计总价
     */
    @Override
    public double getCost() {
        return coffee.getCost() + getPrice();
    }

    @Override
    public String toString() {
        return String.format("Decorator{name='%s', price=%.2f元, decorated=%s}",
                getName(), getPrice(), coffee.toString());
    }
}
