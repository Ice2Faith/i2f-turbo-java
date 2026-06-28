package i2f.design.pattern.behavioral.templateMethod.maker.impl;

import i2f.design.pattern.behavioral.templateMethod.maker.BeverageMaker;

/**
 * 模板方法模式 —— 咖啡制作器（ConcreteClass：CoffeeMaker）
 *
 * <p><b>角色：</b>具体类（Concrete Class）</p>
 *
 * <p><b>说明：</b>咖啡制作器继承 {@link BeverageMaker}，
 * 实现制作咖啡的具体步骤：用热水冲泡咖啡粉、倒入马克杯、添加糖和牛奶。
 * 这体现了模板方法模式的核心——<b>子类在固定流程框架下实现具体步骤</b>。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"果汁制作器"，
 * 只需新增 {@code JuiceMaker} 子类并实现相应的制作步骤，
 * 无需修改现有任何代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see BeverageMaker
 * @see TeaMaker
 */
public class CoffeeMaker extends BeverageMaker {

    /**
     * 实现咖啡的冲泡步骤。
     *
     * <p>咖啡使用热水冲泡咖啡粉，需要浸泡4分钟以充分萃取咖啡精华。</p>
     */
    @Override
    protected void brew() {
        System.out.println("  ② 冲泡：用热水冲泡咖啡粉，浸泡4分钟");
    }

    /**
     * 实现咖啡的倒杯步骤。
     *
     * <p>咖啡通常倒入大容量的马克杯中，方便添加牛奶和糖。</p>
     */
    @Override
    protected void pourInCup() {
        System.out.println("  ③ 倒杯：将咖啡倒入马克杯（350ml）");
    }

    /**
     * 实现咖啡的添加调料步骤。
     *
     * <p>经典咖啡配方：添加2勺糖和适量牛奶，使口感更顺滑。</p>
     */
    @Override
    protected void addCondiments() {
        System.out.println("  ④ 调料：添加2勺糖 + 适量牛奶");
    }

    /**
     * 获取饮品名称。
     *
     * @return 饮品名称
     */
    @Override
    public String getDrinkName() {
        return "经典拿铁咖啡";
    }
}
