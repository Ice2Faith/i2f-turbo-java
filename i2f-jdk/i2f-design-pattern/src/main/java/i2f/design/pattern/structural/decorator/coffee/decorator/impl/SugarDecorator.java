package i2f.design.pattern.structural.decorator.coffee.decorator.impl;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import i2f.design.pattern.structural.decorator.coffee.decorator.CoffeeDecorator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 糖装饰器（Concrete Decorator：SugarDecorator）
 *
 * <p><b>角色：</b>具体装饰器（Concrete Decorator）</p>
 *
 * <p><b>说明：</b>为咖啡添加糖配料，增加价格和修改描述。
 * 可以与其他装饰器组合使用，实现多种口味的咖啡。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see CoffeeDecorator
 * @see Coffee
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee, "加糖", 1.5);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + 糖";
    }

    @Override
    public String toString() {
        return String.format("SugarDecorator{price=%.2f元, decorated=%s}", getPrice(), coffee.toString());
    }
}
