package i2f.design.pattern.behavioral.command.command.impl;

import i2f.design.pattern.behavioral.command.command.Command;
import i2f.design.pattern.behavioral.command.receiver.Light;

/**
 * 命令模式 —— 开灯命令（Concrete Command：LightOnCommand）
 *
 * <p><b>角色：</b>具体命令（Concrete Command）</p>
 *
 * <p><b>说明：</b>封装"开灯"操作为一个命令对象。命令持有接收者（Light）的引用，
 * 在 {@code execute()} 中调用接收者的 {@code turnOn()} 方法，
 * 在 {@code undo()} 中调用 {@code turnOff()} 恢复到执行前的状态。</p>
 *
 * <p><b>解耦体现：</b>遥控器（调用者）只需调用 {@code command.execute()}，
 * 完全不知道 Light 的存在，也不知道具体执行了什么操作。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Command
 * @see Light
 */
public class LightOnCommand implements Command {

    /**
     * 接收者：电灯实例。
     */
    private final Light light;

    /**
     * 执行命令前的亮度状态（用于撤销）。
     */
    private int previousBrightness;

    /**
     * 执行命令前的开关状态（用于撤销）。
     */
    private boolean previousIsOn;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        // 保存执行前的状态（用于撤销）
        this.previousBrightness = light.getBrightness();
        this.previousIsOn = light.isOn();

        // 执行开灯操作
        light.turnOn();
    }

    @Override
    public void undo() {
        // 恢复到执行前的状态
        if (previousIsOn) {
            light.setBrightness(previousBrightness);
        } else {
            light.turnOff();
        }
        System.out.println("  ↩️  [撤销] 已恢复电灯状态");
    }

    @Override
    public String getName() {
        return String.format("开灯命令 [%s]", light.getRoom());
    }
}
