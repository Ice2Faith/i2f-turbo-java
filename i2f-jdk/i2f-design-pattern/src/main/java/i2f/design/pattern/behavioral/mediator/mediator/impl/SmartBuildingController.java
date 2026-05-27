package i2f.design.pattern.behavioral.mediator.mediator.impl;

import i2f.design.pattern.behavioral.mediator.component.Component;
import i2f.design.pattern.behavioral.mediator.component.impl.AirConditioner;
import i2f.design.pattern.behavioral.mediator.component.impl.Curtain;
import i2f.design.pattern.behavioral.mediator.component.impl.Light;
import i2f.design.pattern.behavioral.mediator.component.impl.SecuritySystem;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 智能楼宇控制器（SmartBuildingController）
 *
 * <p><b>角色：</b>具体中介者（Concrete Mediator）</p>
 *
 * <p><b>核心职责：</b>封装灯光、空调、窗帘、安防等多个子系统之间的复杂交互逻辑。
 * 当某个子系统状态变化时，由楼宇控制器统一协调其他子系统的响应动作，
 * 避免各子系统之间直接耦合。</p>
 *
 * <p><b>协作规则（业务逻辑）：</b></p>
 * <pre>
 *  事件                  中介者协调动作
 *  ─────────────────   ─────────────────────────────────────────────
 *  灯光开启              → 自动关闭窗帘（避免眩光）
 *  灯光关闭              → 无联动
 *  空调开启              → 自动关闭窗帘（保温节能）
 *  空调关闭              → 无联动
 *  窗帘关闭              → 自动开启灯光（补充照明）
 *  窗帘打开              → 无联动
 *  安防启动              → 关闭窗帘 + 开启外围灯光
 *  安防解除              → 无联动
 *  报警触发              → 开启所有灯光 + 关闭窗帘（威慑+保护隐私）
 * </pre>
 *
 * <p><b>模式价值：</b>如果没有这个中介者，每个子系统都需要引用其他所有子系统，
 * 形成 N×(N-1) 条依赖关系。使用中介者后，依赖关系降为 N 条（每个子系统只依赖中介者），
 * 大幅降低系统复杂度。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Mediator
 * @see Light
 * @see AirConditioner
 * @see Curtain
 * @see SecuritySystem
 */
public class SmartBuildingController extends Mediator {

    /**
     * 灯光系统。
     */
    private Light light;

    /**
     * 空调系统。
     */
    private AirConditioner airConditioner;

    /**
     * 窗帘系统。
     */
    private Curtain curtain;

    /**
     * 安防系统。
     */
    private SecuritySystem securitySystem;

    /**
     * 注册所有同事对象。
     *
     * <p>中介者持有所有同事对象的引用，以便在需要时协调它们。
     * 注意：同事对象之间互不持有对方的引用。</p>
     *
     * @param light          灯光系统
     * @param airConditioner 空调系统
     * @param curtain        窗帘系统
     * @param securitySystem 安防系统
     */
    public void registerComponents(Light light, AirConditioner airConditioner,
                                   Curtain curtain, SecuritySystem securitySystem) {
        this.light = light;
        this.airConditioner = airConditioner;
        this.curtain = curtain;
        this.securitySystem = securitySystem;

        // 将中介者注入到各个同事对象中
        light.setMediator(this);
        airConditioner.setMediator(this);
        curtain.setMediator(this);
        securitySystem.setMediator(this);
    }

    /**
     * 接收同事对象的通知并进行协调。
     *
     * <p>这是中介者模式的核心方法。根据不同的事件来源和事件类型，
     * 执行相应的联动逻辑。所有复杂的协作规则都集中在这里管理。</p>
     *
     * @param sender 发送通知的同事对象
     * @param event  发生的事件类型
     */
    @Override
    public void notify(Component sender, String event) {
        System.out.println("\n  📡 [楼宇控制器] 收到通知：" + sender.getName() + " -> " + event);
        System.out.println("  📡 [楼宇控制器] 开始协调联动...");

        switch (event) {
            case "LIGHT_ON":
                // 灯光开启 → 自动关闭窗帘（避免眩光）
                System.out.println("  📡 [楼宇控制器] 联动策略：灯光开启，自动关闭窗帘避免眩光");
                curtain.close();
                break;

            case "LIGHT_OFF":
                System.out.println("  📡 [楼宇控制器] 灯光关闭，无需联动");
                break;

            case "AC_ON":
                // 空调开启 → 自动关闭窗帘（保温节能）
                System.out.println("  📡 [楼宇控制器] 联动策略：空调开启，自动关闭窗帘保温节能");
                curtain.close();
                break;

            case "AC_OFF":
                System.out.println("  📡 [楼宇控制器] 空调关闭，无需联动");
                break;

            case "CURTAIN_CLOSED":
                // 窗帘关闭 → 自动开启灯光（补充照明）
                System.out.println("  📡 [楼宇控制器] 联动策略：窗帘关闭，自动开启灯光补充照明");
                light.turnOn();
                break;

            case "CURTAIN_OPEN":
                System.out.println("  📡 [楼宇控制器] 窗帘打开，无需联动");
                break;

            case "SECURITY_ARMED":
                // 安防启动 → 关闭窗帘 + 开启外围灯光
                System.out.println("  📡 [楼宇控制器] 联动策略：安防启动，关闭窗帘并开启外围灯光");
                curtain.close();
                light.turnOn();
                break;

            case "SECURITY_DISARMED":
                System.out.println("  📡 [楼宇控制器] 安防解除，无需联动");
                break;

            case "ALARM_TRIGGERED":
                // 报警触发 → 开启所有灯光 + 关闭窗帘（威慑+保护隐私）
                System.out.println("  📡 [楼宇控制器] 联动策略：报警触发，开启所有灯光威慑入侵者，关闭窗帘保护隐私");
                light.turnOn();
                curtain.close();
                airConditioner.setState(true, 22.0);
                break;

            default:
                System.out.println("  📡 [楼宇控制器] 未知事件，不处理");
                break;
        }

        System.out.println("  📡 [楼宇控制器] 联动协调完成\n");
    }
}
