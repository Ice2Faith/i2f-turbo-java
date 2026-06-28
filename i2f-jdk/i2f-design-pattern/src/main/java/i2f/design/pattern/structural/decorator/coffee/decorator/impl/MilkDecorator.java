package i2f.design.pattern.structural.decorator.coffee.decorator.impl;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import i2f.design.pattern.structural.decorator.coffee.decorator.CoffeeDecorator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 牛奶装饰器（Concrete Decorator：MilkDecorator）
 *
 * <p><b>角色：</b>具体装饰器（Concrete Decorator）</p>
 *
 * <p><b>说明：</b>为咖啡添加牛奶配料，增加价格和修改描述。
 * 牛奶装饰器可以装饰任何 {@link Coffee} 对象，包括已被其他装饰器装饰过的咖啡。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see CoffeeDecorator
 * @see Coffee
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee, "加牛奶", 3.0);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + 牛奶";
    }

    @Override
    public String toString() {
        return String.format("MilkDecorator{price=%.2f元, decorated=%s}", getPrice(), coffee.toString());
    }
}
