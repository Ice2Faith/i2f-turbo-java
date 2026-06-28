package i2f.design.pattern.behavioral.mediator.component.impl;

import i2f.design.pattern.behavioral.mediator.component.Component;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 安防系统（SecuritySystem）
 *
 * <p><b>角色：</b>具体同事（Concrete Colleague）</p>
 *
 * <p><b>业务场景：</b>智能楼宇的安全防护系统。当安防系统检测到异常（如入侵报警）时，
 * 需要联动多个设备协同响应（如：自动开启所有灯光威慑入侵者、关闭窗帘保护隐私、调节空调至舒适温度）。</p>
 *
 * <p><b>协作示例：</b>报警触发后，安防系统不需要直接调用灯光、窗帘、空调的方法，
 * 而是通知中介者"发生报警"，由中介者统一协调各设备的响应动作。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Component
 * @see Mediator
 */
public class SecuritySystem extends Component {

    /**
     * 安防状态：true=警戒中，false=已解除。
     */
    private boolean isArmed;

    /**
     * 报警状态：true=触发报警，false=正常。
     */
    private boolean isAlarming;

    public SecuritySystem(Mediator mediator) {
        super(mediator);
        this.isArmed = false;
        this.isAlarming = false;
    }

    /**
     * 启动警戒模式。
     *
     * <p>安防系统启动后，通知中介者进行联动处理（如：自动关闭所有窗户、开启外围灯光）。</p>
     */
    public void arm() {
        this.isArmed = true;
        System.out.println("  🚨 [" + getName() + "] 警戒模式已启动");
        // 通知中介者：安防已启动
        mediator.notify(this, "SECURITY_ARMED");
    }

    /**
     * 解除警戒模式。
     */
    public void disarm() {
        this.isArmed = false;
        System.out.println("  🚨 [" + getName() + "] 警戒模式已解除");
        // 通知中介者：安防已解除
        mediator.notify(this, "SECURITY_DISARMED");
    }

    /**
     * 触发报警。
     *
     * <p>检测到异常时触发报警，由中介者协调各设备响应（如：开灯、关窗帘、记录日志）。</p>
     */
    public void triggerAlarm() {
        this.isAlarming = true;
        System.out.println("  🚨 [" + getName() + "] ⚠️ 报警触发！检测到异常情况！");
        // 通知中介者：触发报警
        mediator.notify(this, "ALARM_TRIGGERED");
    }

    /**
     * 解除报警。
     */
    public void clearAlarm() {
        this.isAlarming = false;
        System.out.println("  🚨 [" + getName() + "] 报警已解除");
    }

    public boolean isArmed() {
        return isArmed;
    }

    public boolean isAlarming() {
        return isAlarming;
    }

    @Override
    public String getName() {
        return "安防系统";
    }

    @Override
    public String toString() {
        return "SecuritySystem{name='" + getName() + "', isArmed=" + isArmed + ", isAlarming=" + isAlarming + "}";
    }
}
