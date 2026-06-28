package i2f.design.pattern.behavioral.templateMethod.maker;

import i2f.design.pattern.behavioral.templateMethod.maker.impl.CoffeeMaker;
import i2f.design.pattern.behavioral.templateMethod.maker.impl.TeaMaker;

/**
 * 模板方法模式 —— 制作饮品（AbstractClass：BeverageMaker）
 *
 * <p><b>角色：</b>抽象类（Abstract Class）</p>
 *
 * <p><b>模式说明：</b>定义制作饮品的固定流程（模板方法 {@link #makeDrink()}），
 * 将具体步骤延迟到子类实现。这就是模板方法模式的核心：
 * <b>"定义算法骨架，将某些步骤延迟到子类"</b>。</p>
 *
 * <p><b>命名立意：</b>"饮品制作机"天然地充当"模板"角色——
 * 无论是制作咖啡还是茶，都遵循相同的流程：烧水→冲泡→倒入杯子→添加调料。
 * 但每一步的具体操作因饮品而异，由子类决定如何实现。</p>
 *
 * <p><b>方法分类：</b></p>
 * <pre>
 *  方法类型          特点                                    示例
 *  ─────────────   ──────────────────────────────────────   ────────────────────
 *  模板方法         定义算法骨架，调用其他步骤方法              makeDrink()
 *  抽象方法         子类必须实现的具体步骤                      brew()、pourInCup()、addCondiments()
 *  具体方法         所有子类共用的固定步骤                      boilWater()
 *  钩子方法         子类可选择性覆盖，控制流程                  customerWantsCondiments()
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  AbstractClass（抽象类）
 *    └─ makeDrink(): void                   ← 模板方法（final，防止子类修改算法骨架）
 *    ├─ boilWater(): void                   ← 具体方法（所有子类共用）
 *    ├─ brew(): void                        ← 抽象方法（子类必须实现）
 *    ├─ pourInCup(): void                   ← 抽象方法（子类必须实现）
 *    ├─ addCondiments(): void               ← 抽象方法（子类必须实现）
 *    └─ customerWantsCondiments(): boolean  ← 钩子方法（子类可选择覆盖）
 *
 *  ConcreteClass（具体类）
 *    ├─ CoffeeMaker → 实现咖啡制作的具体步骤
 *    └─ TeaMaker    → 实现茶制作的具体步骤
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see CoffeeMaker
 * @see TeaMaker
 */
public abstract class BeverageMaker {

    /**
     * 模板方法（Template Method）—— 制作饮品的固定流程。
     *
     * <p>该方法定义了制作饮品的完整算法骨架，按照固定顺序调用各个步骤。
     * 使用 <b>final</b> 修饰，防止子类修改算法结构，确保流程的一致性。</p>
     *
     * <p><b>好莱坞原则：</b>"Don't call us, we'll call you" ——
     * 子类不主动调用父类方法，而是由父类的模板方法在适当时机回调子类实现。</p>
     */
    public final void makeDrink() {
        // 步骤1：烧水（所有饮品共用）
        boilWater();

        // 步骤2：冲泡（由子类决定如何冲泡）
        brew();

        // 步骤3：倒入杯子（由子类决定倒入哪种杯子）
        pourInCup();

        // 步骤4：添加调料（钩子方法控制是否需要调料）
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    /**
     * 具体方法 —— 烧水。
     *
     * <p>所有饮品制作都需要烧水，这是共用步骤，直接在抽象类中实现。</p>
     */
    private void boilWater() {
        System.out.println("  ① 烧水：将水烧至100°C");
    }

    /**
     * 抽象方法 —— 冲泡。
     *
     * <p>不同饮品的冲泡方式不同：咖啡用热水冲泡咖啡粉，茶用热水浸泡茶叶。
     * 具体实现由子类提供。</p>
     */
    protected abstract void brew();

    /**
     * 抽象方法 —— 倒入杯子。
     *
     * <p>不同饮品使用的杯子可能不同：咖啡用马克杯，茶用陶瓷杯。
     * 具体实现由子类提供。</p>
     */
    protected abstract void pourInCup();

    /**
     * 抽象方法 —— 添加调料。
     *
     * <p>不同饮品的调料不同：咖啡加糖和牛奶，茶加柠檬。
     * 具体实现由子类提供。</p>
     */
    protected abstract void addCondiments();

    /**
     * 钩子方法（Hook Method）—— 顾客是否需要添加调料。
     *
     * <p>钩子方法提供了一种"可选覆盖"的机制：
     * 默认返回 true（需要调料），子类可以选择覆盖此方法来改变行为。
     * 这体现了模板方法模式的灵活性——在固定流程中允许局部定制。</p>
     *
     * @return true 表示需要添加调料，false 表示不需要
     */
    protected boolean customerWantsCondiments() {
        return true;
    }

    /**
     * 获取饮品名称。
     *
     * @return 饮品名称
     */
    public abstract String getDrinkName();
}
