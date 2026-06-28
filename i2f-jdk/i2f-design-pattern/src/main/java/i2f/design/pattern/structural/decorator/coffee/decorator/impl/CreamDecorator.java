package i2f.design.pattern.structural.decorator.coffee.decorator.impl;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import i2f.design.pattern.structural.decorator.coffee.decorator.CoffeeDecorator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 奶油装饰器（Concrete Decorator：CreamDecorator）
 *
 * <p><b>角色：</b>具体装饰器（Concrete Decorator）</p>
 *
 * <p><b>说明：</b>为咖啡添加奶油配料，增加价格和修改描述。
 * 展示装饰器模式如何动态叠加多种配料。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see CoffeeDecorator
 * @see Coffee
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreamDecorator extends CoffeeDecorator {

    public CreamDecorator(Coffee coffee) {
        super(coffee, "加奶油", 4.0);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + 奶油";
    }

    @Override
    public String toString() {
        return String.format("CreamDecorator{price=%.2f元, decorated=%s}", getPrice(), coffee.toString());
    }
}
