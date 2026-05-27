package i2f.design.pattern.behavioral.templateMethod;

import i2f.design.pattern.behavioral.templateMethod.maker.BeverageMaker;
import i2f.design.pattern.behavioral.templateMethod.maker.impl.CoffeeMaker;
import i2f.design.pattern.behavioral.templateMethod.maker.impl.TeaMaker;

/**
 * 模板方法模式 —— 调用演示
 *
 * <p>演示模板方法模式的核心机制：客户端调用抽象类的模板方法（{@link BeverageMaker#makeDrink()}），
 * 由具体子类决定每个步骤的具体实现，实现算法骨架与具体步骤的解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 模板方法核心演示 ====================
        System.out.println("====== 模板方法模式（Template Method）演示 ======");
        System.out.println("场景：饮品制作机（Template）通过固定流程制作不同饮品");
        System.out.println("      不同的饮品子类实现每个步骤的具体操作\n");

        // ==================== 2. 制作咖啡 ====================
        System.out.println("────── 制作经典拿铁咖啡 ──────");
        BeverageMaker coffeeMaker = new CoffeeMaker();
        System.out.println("饮品：" + coffeeMaker.getDrinkName());
        coffeeMaker.makeDrink();

        System.out.println();

        // ==================== 3. 制作茶（加柠檬） ====================
        System.out.println("────── 制作清香绿茶（加柠檬） ──────");
        BeverageMaker teaWithLemon = new TeaMaker(true);
        System.out.println("饮品：" + teaWithLemon.getDrinkName());
        teaWithLemon.makeDrink();

        System.out.println();

        // ==================== 4. 制作茶（不加柠檬）—— 钩子方法演示 ====================
        System.out.println("────── 制作清香绿茶（不加柠檬）—— 钩子方法演示 ──────");
        System.out.println("说明：顾客不需要调料，钩子方法返回 false，跳过添加调料步骤\n");
        BeverageMaker teaWithoutLemon = new TeaMaker(false);
        System.out.println("饮品：" + teaWithoutLemon.getDrinkName());
        teaWithoutLemon.makeDrink();

        System.out.println();

        // ==================== 5. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体制作器类型 ======");
        System.out.println("通过统一接口调用模板方法制作不同饮品：\n");

        BeverageMaker[] makers = {
            new CoffeeMaker(),
            new TeaMaker(true),
            new TeaMaker(false)
        };

        for (int i = 0; i < makers.length; i++) {
            System.out.println("订单 " + (i + 1) + "：");
            System.out.println("饮品：" + makers[i].getDrinkName());
            makers[i].makeDrink();
            System.out.println();
        }

        // ==================== 6. 验证模板方法的固定流程 ====================
        System.out.println("====== 验证：模板方法保证所有饮品遵循相同流程 ======");
        System.out.println("无论制作哪种饮品，都严格按照以下顺序执行：");
        System.out.println("  ① 烧水 → ② 冲泡 → ③ 倒杯 → ④ 调料（可选）");
        System.out.println("模板方法使用 final 修饰，子类无法修改算法骨架\n");

        // ==================== 7. 模式优势总结 ====================
        System.out.println("====== 模板方法模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增饮品只需新增子类，无需修改已有代码");
        System.out.println("2. 代码复用：公共步骤（烧水）在父类中实现，避免重复");
        System.out.println("3. 流程控制：模板方法使用 final 保证算法骨架不被修改");
        System.out.println("4. 钩子方法：提供可选覆盖点，在固定流程中允许局部定制");
        System.out.println("5. 好莱坞原则：父类控制流程，在适当时机回调子类方法");
    }
}
