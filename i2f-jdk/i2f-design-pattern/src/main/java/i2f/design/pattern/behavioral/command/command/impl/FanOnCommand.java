package i2f.design.pattern.behavioral.command.command.impl;

import i2f.design.pattern.behavioral.command.command.Command;
import i2f.design.pattern.behavioral.command.receiver.Fan;

/**
 * 命令模式 —— 开风扇命令（Concrete Command：FanOnCommand）
 *
 * <p><b>角色：</b>具体命令（Concrete Command）</p>
 *
 * <p><b>说明：</b>封装"开启风扇"操作为一个命令对象。演示命令模式可以控制不同类型的接收者，
 * 遥控器通过统一的 Command 接口调度各种家电，体现多态的力量。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Command
 * @see Fan
 */
public class FanOnCommand implements Command {

    /**
     * 接收者：电风扇实例。
     */
    private final Fan fan;

    /**
     * 执行命令前的档位状态（用于撤销）。
     */
    private int previousSpeed;

    /**
     * 执行命令前的开关状态（用于撤销）。
     */
    private boolean previousIsOn;

    public FanOnCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        // 保存执行前的状态
        this.previousSpeed = fan.getSpeed();
        this.previousIsOn = fan.isOn();

        // 执行开启风扇操作
        fan.turnOn();
    }

    @Override
    public void undo() {
        // 恢复到执行前的状态
        if (previousIsOn) {
            fan.setSpeed(previousSpeed);
        } else {
            fan.turnOff();
        }
        System.out.println("  ↩️  [撤销] 已恢复电风扇状态");
    }

    @Override
    public String getName() {
        return String.format("开风扇命令 [%s]", fan.getLocation());
    }
}
