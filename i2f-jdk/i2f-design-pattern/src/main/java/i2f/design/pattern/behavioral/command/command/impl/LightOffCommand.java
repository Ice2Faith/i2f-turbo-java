package i2f.design.pattern.behavioral.command.command.impl;

import i2f.design.pattern.behavioral.command.command.Command;
import i2f.design.pattern.behavioral.command.receiver.Light;

/**
 * 命令模式 —— 关灯命令（Concrete Command：LightOffCommand）
 *
 * <p><b>角色：</b>具体命令（Concrete Command）</p>
 *
 * <p><b>说明：</b>封装"关灯"操作为一个命令对象。与 {@link LightOnCommand} 相对，
 * 执行时关闭电灯，撤销时恢复到之前的亮度状态。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Command
 * @see Light
 * @see LightOnCommand
 */
public class LightOffCommand implements Command {

    /**
     * 接收者：电灯实例。
     */
    private final Light light;

    /**
     * 执行命令前的亮度状态（用于撤销）。
     */
    private int previousBrightness;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        // 保存执行前的状态
        this.previousBrightness = light.getBrightness();

        // 执行关灯操作
        light.turnOff();
    }

    @Override
    public void undo() {
        // 恢复到执行前的亮度
        if (previousBrightness > 0) {
            light.setBrightness(previousBrightness);
        }
        System.out.println("  ↩️  [撤销] 已恢复电灯状态");
    }

    @Override
    public String getName() {
        return String.format("关灯命令 [%s]", light.getRoom());
    }
}
