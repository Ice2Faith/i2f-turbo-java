package i2f.design.pattern.behavioral.command.command.impl;

import i2f.design.pattern.behavioral.command.command.Command;
import i2f.design.pattern.behavioral.command.receiver.Light;

/**
 * 命令模式 —— 调光命令（Concrete Command：DimmerCommand）
 *
 * <p><b>角色：</b>具体命令（Concrete Command）</p>
 *
 * <p><b>说明：</b>封装"调节亮度"操作为一个命令对象。支持将电灯亮度调整到指定值，
 * 撤销时恢复到调节前的亮度。这是命令模式灵活性的体现——即使是同一个接收者，
 * 不同的参数也可以封装为不同的命令对象。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Command
 * @see Light
 */
public class DimmerCommand implements Command {

    /**
     * 接收者：电灯实例。
     */
    private final Light light;

    /**
     * 目标亮度值。
     */
    private final int targetBrightness;

    /**
     * 执行命令前的亮度状态（用于撤销）。
     */
    private int previousBrightness;

    public DimmerCommand(Light light, int targetBrightness) {
        this.light = light;
        this.targetBrightness = targetBrightness;
    }

    @Override
    public void execute() {
        // 保存执行前的状态
        this.previousBrightness = light.getBrightness();

        // 执行调光操作
        light.setBrightness(targetBrightness);
    }

    @Override
    public void undo() {
        // 恢复到执行前的亮度
        light.setBrightness(previousBrightness);
        System.out.println("  ↩️  [撤销] 已恢复电灯亮度至 " + previousBrightness + "%");
    }

    @Override
    public String getName() {
        return String.format("调光命令 [%s] → %d%%", light.getRoom(), targetBrightness);
    }
}
