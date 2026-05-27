package i2f.design.pattern.structural.decorator;

import i2f.design.pattern.structural.decorator.coffee.Coffee;
import i2f.design.pattern.structural.decorator.coffee.impl.EspressoCoffee;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.MilkDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.SugarDecorator;
import i2f.design.pattern.structural.decorator.coffee.decorator.impl.CreamDecorator;

/**
 * 装饰器模式 —— 调用演示
 *
 * <p>演示装饰器模式的核心机制：客户端面向抽象组件（{@link Coffee}）编程，
 * 通过装饰器动态地为咖啡添加各种配料，实现功能的灵活叠加。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 装饰器模式核心演示 ====================
        System.out.println("====== 装饰器模式（Decorator）演示 ======");
        System.out.println("场景：咖啡店（Component）通过装饰器动态添加配料（Decorator）");
        System.out.println("      不同的配料可以组合使用，灵活搭配口味\n");

        // ==================== 2. 基础咖啡 —— 浓缩咖啡 ====================
        System.out.println("────── 基础咖啡：浓缩咖啡 ──────");
        Coffee espresso = new EspressoCoffee("浓缩咖啡", 12.0);
        System.out.println("  咖啡描述：" + espresso.getDescription());
        System.out.println("  咖啡价格：" + String.format("%.2f元", espresso.getCost()));
        System.out.println("  咖啡详情：" + espresso);

        System.out.println();

        // ==================== 3. 单层装饰 —— 加牛奶 ====================
        System.out.println("────── 装饰 1 层：浓缩咖啡 + 牛奶 ──────");
        Coffee milkEspresso = new MilkDecorator(espresso);
        System.out.println("  咖啡描述：" + milkEspresso.getDescription());
        System.out.println("  咖啡价格：" + String.format("%.2f元", milkEspresso.getCost()));
        System.out.println("  咖啡详情：" + milkEspresso);

        System.out.println();

        // ==================== 4. 多层装饰 —— 加牛奶 + 糖 ====================
        System.out.println("────── 装饰 2 层：浓缩咖啡 + 牛奶 + 糖 ──────");
        Coffee sweetMilkEspresso = new SugarDecorator(milkEspresso);
        System.out.println("  咖啡描述：" + sweetMilkEspresso.getDescription());
        System.out.println("  咖啡价格：" + String.format("%.2f元", sweetMilkEspresso.getCost()));
        System.out.println("  咖啡详情：" + sweetMilkEspresso);

        System.out.println();

        // ==================== 5. 多层装饰 —— 加牛奶 + 糖 + 奶油 ====================
        System.out.println("────── 装饰 3 层：浓缩咖啡 + 牛奶 + 糖 + 奶油 ──────");
        Coffee premiumCoffee = new CreamDecorator(sweetMilkEspresso);
        System.out.println("  咖啡描述：" + premiumCoffee.getDescription());
        System.out.println("  咖啡价格：" + String.format("%.2f元", premiumCoffee.getCost()));
        System.out.println("  咖啡详情：" + premiumCoffee);

        System.out.println();

        // ==================== 6. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体装饰器类型 ======");
        System.out.println("通过统一接口调度不同的咖啡配置：\n");

        Coffee[] menu = {
                new EspressoCoffee("浓缩咖啡", 12.0),
                new MilkDecorator(new EspressoCoffee("浓缩咖啡", 12.0)),
                new SugarDecorator(new MilkDecorator(new EspressoCoffee("浓缩咖啡", 12.0))),
                new CreamDecorator(new SugarDecorator(new MilkDecorator(new EspressoCoffee("浓缩咖啡", 12.0))))
        };

        for (int i = 0; i < menu.length; i++) {
            System.out.println("菜单项 " + (i + 1) + "：");
            System.out.println("  描述：" + menu[i].getDescription());
            System.out.println("  价格：" + String.format("%.2f元", menu[i].getCost()));
            System.out.println();
        }

        // ==================== 7. 验证装饰器每次创建新实例 ====================
        System.out.println("====== 验证：装饰器每次包装创建全新实例 ======");
        Coffee base1 = new EspressoCoffee("浓缩咖啡", 12.0);
        Coffee base2 = new EspressoCoffee("浓缩咖啡", 12.0);
        Coffee decorated1 = new MilkDecorator(base1);
        Coffee decorated2 = new MilkDecorator(base2);
        System.out.println("decorated1: " + decorated1);
        System.out.println("decorated2: " + decorated2);
        System.out.println("decorated1 == decorated2 ? " + (decorated1 == decorated2));
        System.out.println("decorated1.equals(decorated2) ? " + decorated1.equals(decorated2));

        System.out.println();

        // ==================== 8. 验证装饰顺序不影响最终结果 ====================
        System.out.println("====== 验证：装饰顺序对价格无影响（对描述有影响） ======");
        Coffee order1 = new SugarDecorator(new MilkDecorator(new EspressoCoffee("浓缩咖啡", 12.0)));
        Coffee order2 = new MilkDecorator(new SugarDecorator(new EspressoCoffee("浓缩咖啡", 12.0)));
        System.out.println("顺序 1（先奶后糖）：" + order1.getDescription() + "，价格：" + String.format("%.2f元", order1.getCost()));
        System.out.println("顺序 2（先糖后奶）：" + order2.getDescription() + "，价格：" + String.format("%.2f元", order2.getCost()));
        System.out.println("价格相同？" + (order1.getCost() == order2.getCost()));

        System.out.println();

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 装饰器模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增配料只需新增装饰器类，无需修改已有代码");
        System.out.println("2. 动态添加职责：可以在运行时灵活组合不同的装饰器");
        System.out.println("3. 避免类爆炸：N 种配料只需 N 个装饰器类，而非 2^N 个子类");
        System.out.println("4. 客户端面向抽象编程：无需依赖具体装饰器类，降低耦合");
        System.out.println("5. 装饰器可嵌套使用：实现功能的层层叠加，保持接口一致性");
    }
}
