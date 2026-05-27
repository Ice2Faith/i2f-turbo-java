/**
 * 命令模式（Command）
 *
 * <p><b>定义：</b>将一个请求封装为一个对象，从而使你可用不同的请求对客户进行参数化，
 * 对请求排队或记录请求日志，以及支持可撤销的操作。</p>
 * <p><b>分类：</b>行为型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>需要将请求发送者和接收者解耦。</li>
 *   <li>需要支持撤销/重做、事务、日志、队列。</li>
 *   <li>需要将操作组合成宏命令。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"智能家居遥控器"为场景——遥控器（Invoker）通过命令对象（Command）
 * 控制各种家电设备（Receiver），如开灯、关灯、调光、开风扇等。每个操作都被封装为独立的命令对象，
 * 支持执行和撤销，实现请求发送者与接收者的完全解耦。</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.behavioral.command.command.Command} —— 抽象命令（Command 接口）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.receiver.Light} —— 接收者（电灯设备）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.receiver.Fan} —— 接收者（电风扇设备）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.command.impl.LightOnCommand} —— 具体命令（开灯）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.command.impl.LightOffCommand} —— 具体命令（关灯）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.command.impl.DimmerCommand} —— 具体命令（调光）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.command.impl.FanOnCommand} —— 具体命令（开风扇）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.invoker.RemoteControl} —— 调用者（遥控器）</li>
 *   <li>{@link i2f.design.pattern.behavioral.command.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.behavioral.command.command.Command
 * @see i2f.design.pattern.behavioral.command.invoker.RemoteControl
 * @see i2f.design.pattern.behavioral.command.Test
 */
package i2f.design.pattern.behavioral.command;
