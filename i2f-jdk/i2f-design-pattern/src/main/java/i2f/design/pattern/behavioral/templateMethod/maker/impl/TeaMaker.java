package i2f.design.pattern.behavioral.templateMethod.maker.impl;

import i2f.design.pattern.behavioral.templateMethod.maker.BeverageMaker;

/**
 * 模板方法模式 —— 茶制作器（ConcreteClass：TeaMaker）
 *
 * <p><b>角色：</b>具体类（Concrete Class）</p>
 *
 * <p><b>说明：</b>茶制作器继承 {@link BeverageMaker}，
 * 实现制作茶的具体步骤：用热水浸泡茶叶、倒入陶瓷杯、添加柠檬片。
 * 这体现了模板方法模式的核心——<b>子类在固定流程框架下实现具体步骤</b>。</p>
 *
 * <p><b>钩子方法演示：</b>茶制作器覆盖了钩子方法 {@link #customerWantsCondiments()}，
 * 演示如何在模板方法的固定流程中进行局部定制（某些顾客可能不需要加柠檬）。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see BeverageMaker
 * @see CoffeeMaker
 */
public class TeaMaker extends BeverageMaker {

    /**
     * 标识当前顾客是否需要添加调料。
     *
     * <p>通过构造器参数控制，演示钩子方法的动态定制能力。</p>
     */
    private final boolean wantsCondiments;

    /**
     * 创建茶制作器。
     *
     * @param wantsCondiments 是否需要添加调料（柠檬）
     */
    public TeaMaker(boolean wantsCondiments) {
        this.wantsCondiments = wantsCondiments;
    }

    /**
     * 实现茶的冲泡步骤。
     *
     * <p>茶使用热水浸泡茶叶，需要等待3分钟让茶香充分释放。</p>
     */
    @Override
    protected void brew() {
        System.out.println("  ② 冲泡：用热水浸泡绿茶茶叶，等待3分钟");
    }

    /**
     * 实现茶的倒杯步骤。
     *
     * <p>茶通常倒入精致的陶瓷杯中，保持茶香和温度。</p>
     */
    @Override
    protected void pourInCup() {
        System.out.println("  ③ 倒杯：将茶倒入陶瓷杯（200ml）");
    }

    /**
     * 实现茶的添加调料步骤。
     *
     * <p>经典茶饮配方：添加一片新鲜柠檬，增加清香口感。</p>
     */
    @Override
    protected void addCondiments() {
        System.out.println("  ④ 调料：添加1片新鲜柠檬");
    }

    /**
     * 覆盖钩子方法 —— 控制是否需要添加调料。
     *
     * <p>演示模板方法模式的灵活性：子类可以在固定流程中
     * 通过覆盖钩子方法来定制局部行为。</p>
     *
     * @return true 表示需要添加柠檬，false 表示不需要
     */
    @Override
    protected boolean customerWantsCondiments() {
        return wantsCondiments;
    }

    /**
     * 获取饮品名称。
     *
     * @return 饮品名称
     */
    @Override
    public String getDrinkName() {
        return "清香绿茶";
    }
}
