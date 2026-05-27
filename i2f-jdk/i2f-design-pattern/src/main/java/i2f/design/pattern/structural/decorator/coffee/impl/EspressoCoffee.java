package i2f.design.pattern.structural.decorator.coffee.impl;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 装饰器模式 —— 浓缩咖啡（Concrete Component：Espresso）
 *
 * <p><b>角色：</b>具体组件（Concrete Component）</p>
 *
 * <p><b>说明：</b>浓缩咖啡是基础咖啡，可以被各种配料装饰。
 * 它实现了 {@link Coffee} 接口，提供基础的咖啡描述和价格。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Coffee
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EspressoCoffee extends Coffee {

    public EspressoCoffee(String name, double price) {
        super(name, price);
    }

    @Override
    public String getDescription() {
        return "浓缩咖啡";
    }

    @Override
    public double getCost() {
        return getPrice();
    }

    @Override
    public String toString() {
        return String.format("Espresso{name='%s', price=%.2f元}", getName(), getPrice());
    }
}
