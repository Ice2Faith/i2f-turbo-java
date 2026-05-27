package i2f.design.pattern.structural.facade;

/**
 * 外观模式 —— 调用演示
 *
 * <p>演示外观模式的核心机制：客户端通过统一的外观接口（{@link SmartHomeFacade}）
 * 控制复杂的智能家居子系统，无需了解各个子系统的内部细节。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 外观模式核心演示 ====================
        System.out.println("====== 外观模式（Facade）演示 ======");
        System.out.println("场景：智能家居系统（Facade）通过统一接口控制多个子系统");
        System.out.println("      客户端无需了解灯光、空调、音响、窗帘的复杂操作\n");

        // 创建外观类实例
        SmartHomeFacade smartHome = new SmartHomeFacade();

        // ==================== 2. 回家模式 ====================
        System.out.println("────── 场景1：回家模式 ──────");
        smartHome.goHomeMode();

        // ==================== 3. 影院模式 ====================
        System.out.println("────── 场景2：影院模式 ──────");
        smartHome.cinemaMode();

        // ==================== 4. 睡眠模式 ====================
        System.out.println("────── 场景3：睡眠模式 ──────");
        smartHome.sleepMode();

        // ==================== 5. 起床模式 ====================
        System.out.println("────── 场景4：起床模式 ──────");
        smartHome.wakeUpMode();

        // ==================== 6. 离家模式 ====================
        System.out.println("────── 场景5：离家模式 ──────");
        smartHome.leaveHomeMode();

        System.out.println();

        // ==================== 7. 对比演示：直接操作子系统 vs 使用外观 ====================
        System.out.println("====== 对比演示：直接操作子系统 vs 使用外观 ======");
        System.out.println("\n【方式1：直接操作子系统 —— 繁琐且容易出错】");
        System.out.println("如果要手动实现影院模式，需要调用以下方法：");
        System.out.println("  light.setCinemaMode();");
        System.out.println("  light.turnOn();");
        System.out.println("  ac.setComfortMode();");
        System.out.println("  ac.turnOn();");
        System.out.println("  audio.setCinemaMode();");
        System.out.println("  audio.turnOn();");
        System.out.println("  audio.play();");
        System.out.println("  curtain.close();");
        System.out.println("  // 需要记住8个方法调用，且顺序不能错！\n");

        System.out.println("【方式2：使用外观 —— 简单优雅】");
        System.out.println("  smartHome.cinemaMode();");
        System.out.println("  // 只需1个方法调用，内部逻辑已封装！\n");

        // ==================== 8. 外观模式灵活性演示 ====================
        System.out.println("====== 外观模式灵活性：仍可访问子系统 ======");
        System.out.println("外观模式不限制客户端直接使用子系统：\n");

        System.out.println("通过外观类获取灯光子系统，进行自定义操作：");
        smartHome.getLightSystem().setBrightness(75);
        smartHome.getLightSystem().setColor("紫色");
        smartHome.getLightSystem().turnOn();

        System.out.println();

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 外观模式优势总结 ======");
        System.out.println("1. 降低耦合：客户端无需了解子系统内部实现，只与外观类交互");
        System.out.println("2. 简化接口：将复杂的子系统操作封装为简单的高层方法");
        System.out.println("3. 提高可维护性：子系统变化时，只需修改外观类，不影响客户端");
        System.out.println("4. 灵活性：客户端仍可直接访问子系统（如果需要精细控制）");
        System.out.println("5. 符合迪米特法则：减少对象之间的直接依赖");
    }
}
