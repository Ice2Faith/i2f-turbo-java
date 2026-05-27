package i2f.design.pattern.behavioral.command;

import i2f.design.pattern.behavioral.command.command.*;
import i2f.design.pattern.behavioral.command.command.impl.DimmerCommand;
import i2f.design.pattern.behavioral.command.command.impl.FanOnCommand;
import i2f.design.pattern.behavioral.command.command.impl.LightOffCommand;
import i2f.design.pattern.behavioral.command.command.impl.LightOnCommand;
import i2f.design.pattern.behavioral.command.invoker.RemoteControl;
import i2f.design.pattern.behavioral.command.receiver.Fan;
import i2f.design.pattern.behavioral.command.receiver.Light;

/**
 * 命令模式 —— 调用演示
 *
 * <p>演示命令模式的核心机制：将请求封装为命令对象，通过统一的 Command 接口调度，
 * 支持撤销操作，实现调用者（遥控器）与接收者（家电设备）的完全解耦。</p>
 *
 * <p><b>场景说明：</b>智能家居遥控器控制多种家电设备（电灯、电风扇等），
 * 每个操作（开灯、关灯、调光、开风扇）都被封装为独立的命令对象，
 * 遥控器通过 Command 接口执行和撤销命令，无需知道具体设备的存在。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 命令模式核心演示 ====================
        System.out.println("====== 命令模式（Command）演示 ======");
        System.out.println("场景：智能家居遥控器（Invoker）通过命令对象控制家电设备（Receiver）");
        System.out.println("      每个操作被封装为命令，支持执行和撤销\n");

        // ==================== 2. 创建接收者（家电设备） ====================
        System.out.println("────── 步骤 1：创建家电设备（Receiver） ──────");
        Light livingRoomLight = new Light("客厅");
        Light bedroomLight = new Light("卧室");
        Fan studyFan = new Fan("书房");
        System.out.println("  已创建：客厅电灯、卧室电灯、书房电风扇\n");

        // ==================== 3. 创建命令对象 ====================
        System.out.println("────── 步骤 2：创建命令对象（Command） ──────");
        Command livingRoomLightOn = new LightOnCommand(livingRoomLight);
        Command livingRoomLightOff = new LightOffCommand(livingRoomLight);
        Command bedroomDimmer = new DimmerCommand(bedroomLight, 80);
        Command studyFanOn = new FanOnCommand(studyFan);
        System.out.println("  已创建：客厅开灯命令、客厅关灯命令、卧室调光命令（80%）、书房开风扇命令\n");

        // ==================== 4. 创建调用者（遥控器）并绑定命令 ====================
        System.out.println("────── 步骤 3：配置遥控器（Invoker） ──────");
        RemoteControl remote = new RemoteControl();
        remote.setCommand(0, livingRoomLightOn);   // 按钮 0：客厅开灯
        remote.setCommand(1, livingRoomLightOff);  // 按钮 1：客厅关灯
        remote.setCommand(2, bedroomDimmer);       // 按钮 2：卧室调光
        remote.setCommand(3, studyFanOn);          // 按钮 3：书房开风扇
        System.out.println();

        // ==================== 5. 执行命令演示 ====================
        System.out.println("====== 演示 1：执行命令 ======");
        System.out.println("操作：依次按下遥控器按钮 0、2、3\n");

        remote.pressButton(0);  // 客厅开灯
        remote.pressButton(2);  // 卧室调光至 80%
        remote.pressButton(3);  // 书房开风扇

        System.out.println("\n  当前设备状态：");
        System.out.println("  " + livingRoomLight);
        System.out.println("  " + bedroomLight);
        System.out.println("  " + studyFan);

        // ==================== 6. 撤销命令演示 ====================
        System.out.println("\n====== 演示 2：撤销命令 ======");
        System.out.println("操作：连续按 3 次撤销按钮，逆序撤销刚才的操作\n");

        remote.pressUndo();  // 撤销：书房开风扇
        remote.pressUndo();  // 撤销：卧室调光
        remote.pressUndo();  // 撤销：客厅开灯

        System.out.println("\n  撤销后设备状态：");
        System.out.println("  " + livingRoomLight);
        System.out.println("  " + bedroomLight);
        System.out.println("  " + studyFan);

        // ==================== 7. 多设备控制演示 ====================
        System.out.println("\n====== 演示 3：多设备组合控制 ======");
        System.out.println("操作：配置新的命令组合，演示遥控器可同时控制多种设备\n");

        remote.clearHistory();  // 清空历史
        Command bedroomLightOn = new LightOnCommand(bedroomLight);
        Command bedroomLightOff = new LightOffCommand(bedroomLight);

        remote.setCommand(0, bedroomLightOn);    // 按钮 0：卧室开灯
        remote.setCommand(1, bedroomLightOff);   // 按钮 1：卧室关灯
        remote.setCommand(2, livingRoomLightOn); // 按钮 2：客厅开灯
        remote.setCommand(3, livingRoomLightOff); // 按钮 3：客厅关灯

        System.out.println("\n  执行：打开卧室和客厅的灯");
        remote.pressButton(0);
        remote.pressButton(2);

        System.out.println("\n  执行：关闭卧室和客厅的灯");
        remote.pressButton(1);
        remote.pressButton(3);

        // ==================== 8. 验证解耦特性 ====================
        System.out.println("\n====== 演示 4：验证调用者与接收者解耦 ======");
        System.out.println("说明：遥控器（Invoker）只与 Command 接口交互，");
        System.out.println("      不知道 Light、Fan 等接收者的存在，也不知道具体执行了什么操作\n");

        System.out.println("  遥控器视角：");
        System.out.println("  - 我只知道 Command 接口有 execute() 和 undo() 方法");
        System.out.println("  - 我不需要知道命令控制的是电灯还是电风扇");
        System.out.println("  - 我不需要知道命令的具体实现逻辑");
        System.out.println("  - 我只负责在按钮被按下时调用 command.execute()");

        // ==================== 9. 模式优势总结 ====================
        System.out.println("\n====== 命令模式优势总结 ======");
        System.out.println("1. 解耦调用者与接收者：遥控器不需要知道家电设备的存在");
        System.out.println("2. 支持撤销/重做：命令对象保存状态，可恢复到执行前");
        System.out.println("3. 易于扩展：新增命令只需实现 Command 接口，无需修改现有代码");
        System.out.println("4. 支持命令队列：可将命令对象放入队列延迟执行");
        System.out.println("5. 支持宏命令：可组合多个命令为一个大命令");
        System.out.println("6. 支持日志记录：可将命令对象序列化，系统崩溃后恢复");
        System.out.println("7. 符合开闭原则：新增命令类型对扩展开放，对修改关闭");

        System.out.println("\n====== JDK/Spring 中的命令模式案例 ======");
        System.out.println("- JDK：java.lang.Runnable、java.util.concurrent.Callable");
        System.out.println("- Spring：JdbcTemplate 的 StatementCallback、TaskExecutor#execute(Runnable)");
        System.out.println("- Spring Batch：Tasklet 将批处理步骤封装为可执行命令");
        System.out.println("- JUnit：Test 接口将测试方法封装为可执行命令");
    }
}
