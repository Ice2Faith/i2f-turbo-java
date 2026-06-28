package i2f.design.pattern.creational.builder;

import i2f.design.pattern.creational.builder.impl.GamingComputerBuilder;
import i2f.design.pattern.creational.builder.impl.OfficeComputerBuilder;

/**
 * 建造者模式 —— 调用演示
 *
 * <p>演示建造者模式的核心机制：将复杂对象的构建与表示分离，
 * 同样的构建流程（指挥者定义）配合不同的建造者，产出不同配置的产品。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 建造者模式核心演示 ====================
        System.out.println("====== 建造者模式（Builder）演示 ======");
        System.out.println("场景：电脑组装流水线（Director）通过不同建造者（Builder）组装不同配置的电脑");
        System.out.println("      办公电脑建造者 → 低配办公电脑");
        System.out.println("      游戏电脑建造者 → 高配游戏电脑\n");

        // ==================== 2. 使用指挥者构建办公电脑 ====================
        System.out.println("────── 办公电脑 ──────");
        Director director = new Director();
        OfficeComputerBuilder officeBuilder = new OfficeComputerBuilder();
        Computer officeComputer = director.constructOfficeComputer(officeBuilder);
        System.out.println("  配置：" + officeComputer.getConfig());
        System.out.println("  详情：" + officeComputer);

        System.out.println();

        // ==================== 3. 使用指挥者构建游戏电脑 ====================
        System.out.println("────── 游戏电脑 ──────");
        GamingComputerBuilder gamingBuilder = new GamingComputerBuilder();
        Computer gamingComputer = director.constructGamingComputer(gamingBuilder);
        System.out.println("  配置：" + gamingComputer.getConfig());
        System.out.println("  详情：" + gamingComputer);

        System.out.println();

        // ==================== 4. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体建造者类型 ======");
        System.out.println("通过统一接口调度不同的建造者：\n");

        Builder[] builders = {new OfficeComputerBuilder(), new GamingComputerBuilder()};
        String[] computerTypes = {"办公电脑", "游戏电脑"};

        for (int i = 0; i < builders.length; i++) {
            System.out.println("构建任务 " + (i + 1) + "：");
            Computer computer = director.construct(builders[i]);
            System.out.println("  类型：" + computerTypes[i]);
            System.out.println("  配置：" + computer.getConfig());
            System.out.println();
        }

        // ==================== 5. 验证构建过程与表示分离 ====================
        System.out.println("====== 验证：同样的构建流程，不同的产品表示 ======");
        Builder builder1 = new OfficeComputerBuilder();
        Builder builder2 = new GamingComputerBuilder();

        Computer c1 = director.construct(builder1);
        Computer c2 = director.construct(builder2);

        System.out.println("使用相同构建流程（director.construct）：");
        System.out.println("  办公电脑 CPU: " + c1.getCpu());
        System.out.println("  游戏电脑 CPU: " + c2.getCpu());
        System.out.println("  办公电脑 内存: " + c1.getMemory() + "GB");
        System.out.println("  游戏电脑 内存: " + c2.getMemory() + "GB");
        System.out.println("  配置相同？ " + c1.getConfig().equals(c2.getConfig()));

        System.out.println();

        // ==================== 6. 验证每次构建新实例 ====================
        System.out.println("====== 验证：每次构建产生全新实例 ======");
        Builder b1 = new OfficeComputerBuilder();
        Builder b2 = new OfficeComputerBuilder();
        Computer pc1 = director.construct(b1);
        Computer pc2 = director.construct(b2);
        System.out.println("pc1: " + pc1);
        System.out.println("pc2: " + pc2);
        System.out.println("pc1 == pc2 ? " + (pc1 == pc2));

        System.out.println();

        // ==================== 7. 链式调用演示 ====================
        System.out.println("====== 链式调用演示 —— 建造者支持流式 API ======");
        Computer customComputer = new OfficeComputerBuilder()
                .buildCPU("Intel Core i7-13700K")
                .buildMemory(32)
                .buildStorage("1TB NVMe SSD")
                .buildGPU("NVIDIA RTX 4070")
                .buildMonitor(27.0)
                .buildSSD(true)
                .getComputer();
        System.out.println("  自定义配置：" + customComputer.getConfig());

        System.out.println();

        // ==================== 8. 模式优势总结 ====================
        System.out.println("====== 建造者模式优势总结 ======");
        System.out.println("1. 构建过程与表示分离：指挥者定义流程，建造者决定配置，职责清晰");
        System.out.println("2. 同样的构建流程可创建不同产品：办公电脑 vs 游戏电脑，流程相同配置不同");
        System.out.println("3. 客户端无需知道产品内部细节：只需选择建造者，调用指挥者方法即可");
        System.out.println("4. 支持链式调用：建造者方法返回自身，API 更流畅易读");
        System.out.println("5. 与工厂方法的区别：工厂方法关注创建哪种类型的对象，建造者关注如何分步骤构建复杂对象");
    }
}
