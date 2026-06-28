package i2f.design.pattern.behavioral.command.command;

import i2f.design.pattern.behavioral.command.command.impl.LightOffCommand;
import i2f.design.pattern.behavioral.command.command.impl.LightOnCommand;

/**
 * 命令模式 —— 命令接口（Command）
 *
 * <p><b>角色：</b>抽象命令（Abstract Command）</p>
 *
 * <p><b>模式说明：</b>定义命令的统一接口，声明执行方法 {@link #execute()} 和撤销方法 {@link #undo()}。
 * 所有具体命令都实现此接口，调用者（Invoker）通过该接口触发命令，无需关心命令的具体实现。</p>
 *
 * <p><b>命名立意：</b>以"智能家居遥控器"为场景——遥控器（调用者）通过统一的命令接口
 * 控制各种家电设备（接收者），如开灯、关灯、调光等。每个操作都被封装为一个命令对象，
 * 支持执行和撤销，实现请求发送者与接收者的完全解耦。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象命令              具体命令
 *  ─────────────────   ─────────────────────────────
 *  Command              LightOnCommand（开灯命令）
 *                       LightOffCommand（关灯命令）
 *                       DimmerCommand（调光命令）
 *                       FanOnCommand（开风扇命令）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see LightOnCommand
 * @see LightOffCommand
 * @see i2f.design.pattern.behavioral.command.invoker.RemoteControl
 */
public interface Command {

    /**
     * 执行命令。
     *
     * <p>调用此方法时，命令会将请求转发给接收者（Receiver）执行具体操作。
     * 命令对象封装了接收者实例和要调用的方法，调用者无需知道接收者的存在。</p>
     */
    void execute();

    /**
     * 撤销命令。
     *
     * <p>将接收者的状态恢复到执行命令之前。这是命令模式的核心特性之一，
     * 使得系统可以支持撤销/重做操作。</p>
     */
    void undo();

    /**
     * 获取命令名称（用于日志和调试）。
     *
     * @return 命令的描述性名称
     */
    String getName();
}
