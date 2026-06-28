package i2f.design.pattern.behavioral.mediator;

import i2f.design.pattern.behavioral.mediator.component.impl.AirConditioner;
import i2f.design.pattern.behavioral.mediator.component.impl.Curtain;
import i2f.design.pattern.behavioral.mediator.component.impl.Light;
import i2f.design.pattern.behavioral.mediator.component.impl.SecuritySystem;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;
import i2f.design.pattern.behavioral.mediator.mediator.impl.SmartBuildingController;

/**
 * 中介者模式 —— 调用演示
 *
 * <p>演示中介者模式的核心机制：多个同事对象（灯光、空调、窗帘、安防）通过中介者（楼宇控制器）
 * 进行协调通信，避免彼此之间的直接依赖，实现"网状依赖 → 星形依赖"的解耦转换。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 中介者模式核心演示 ====================
        System.out.println("====== 中介者模式（Mediator）演示 ======");
        System.out.println("场景：智能楼宇中，灯光、空调、窗帘、安防等子系统通过楼宇控制器协调工作");
        System.out.println("      各子系统互不直接依赖，全部通过中介者进行联动\n");

        // ==================== 2. 创建中介者和同事对象 ====================
        System.out.println("────── 初始化智能楼宇系统 ──────");
        Mediator mediator = new SmartBuildingController();

        Light light = new Light(mediator);
        AirConditioner airConditioner = new AirConditioner(mediator);
        Curtain curtain = new Curtain(mediator);
        SecuritySystem securitySystem = new SecuritySystem(mediator);

        // 注册所有组件到中介者
        ((SmartBuildingController) mediator).registerComponents(light, airConditioner, curtain, securitySystem);
        System.out.println("✅ 所有子系统已注册到楼宇控制器\n");

        // ==================== 3. 场景一：用户开启灯光 ====================
        System.out.println("====== 场景一：用户开启灯光 → 自动关闭窗帘避免眩光 ======");
        light.turnOn();

        System.out.println();

        // ==================== 4. 场景二：用户开启空调 ====================
        System.out.println("====== 场景二：用户开启空调 → 自动关闭窗帘保温节能 ======");
        airConditioner.turnOn();

        System.out.println();

        // ==================== 5. 场景三：安防系统启动 ====================
        System.out.println("====== 场景三：安防系统启动 → 关闭窗帘 + 开启外围灯光 ======");
        securitySystem.arm();

        System.out.println();

        // ==================== 6. 场景四：报警触发 ====================
        System.out.println("====== 场景四：报警触发 → 全系统联动响应 ======");
        securitySystem.triggerAlarm();

        System.out.println();

        // ==================== 7. 无中介者 vs 有中介者对比 ====================
        System.out.println("====== 对比：没有中介者 vs 使用中介者 ======");
        System.out.println("没有中介者（网状依赖）：");
        System.out.println("  Light → AirConditioner, Curtain, SecuritySystem");
        System.out.println("  AirConditioner → Light, Curtain, SecuritySystem");
        System.out.println("  Curtain → Light, AirConditioner, SecuritySystem");
        System.out.println("  SecuritySystem → Light, AirConditioner, Curtain");
        System.out.println("  依赖关系数量：4 × 3 = 12 条\n");

        System.out.println("使用中介者（星形依赖）：");
        System.out.println("  Light → Mediator");
        System.out.println("  AirConditioner → Mediator");
        System.out.println("  Curtain → Mediator");
        System.out.println("  SecuritySystem → Mediator");
        System.out.println("  依赖关系数量：4 条（降低 67%）\n");

        // ==================== 8. 验证各组件独立性 ====================
        System.out.println("====== 验证：各组件之间无直接依赖 ======");
        System.out.println("Light 类中是否引用 AirConditioner？ " + hasField(Light.class, "airConditioner"));
        System.out.println("Light 类中是否引用 Curtain？ " + hasField(Light.class, "curtain"));
        System.out.println("Light 类中是否引用 SecuritySystem？ " + hasField(Light.class, "securitySystem"));
        System.out.println("结论：各同事对象仅持有 Mediator 引用，彼此互不依赖\n");

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 中介者模式优势总结 ======");
        System.out.println("1. 降低耦合度：将网状依赖转化为星形依赖，遵循迪米特法则");
        System.out.println("2. 集中控制：复杂协作逻辑集中在中介者中，便于维护和修改");
        System.out.println("3. 简化对象协议：同事对象只需与中介者通信，无需了解其他对象");
        System.out.println("4. 提高复用性：同事对象可独立于其他对象复用");
        System.out.println("5. 符合单一职责：协作逻辑从各组件中剥离，由中介者统一管理");

        System.out.println();
        System.out.println("====== 中介者模式适用场景 ======");
        System.out.println("1. 一组对象以复杂的方式进行通信，导致依赖关系混乱");
        System.out.println("2. 一个对象引用很多其他对象并直接通信，难以复用");
        System.out.println("3. 希望定制一个分布在多个类中的行为，而不想生成太多子类");
        System.out.println("4. GUI 中多个组件相互协作（如按钮、文本框、下拉框联动）");
        System.out.println("5. 工作流引擎、任务调度系统中的任务协调");
    }

    /**
     * 检查类是否包含指定字段（用于验证解耦效果）。
     *
     * @param clazz     类
     * @param fieldName 字段名
     * @return 是否包含该字段
     */
    private static boolean hasField(Class<?> clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
