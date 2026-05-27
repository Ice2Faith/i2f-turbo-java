package i2f.design.pattern.behavioral.command.invoker;

import i2f.design.pattern.behavioral.command.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令模式 —— 遥控器（Invoker：RemoteControl）
 *
 * <p><b>角色：</b>调用者（Invoker）</p>
 *
 * <p><b>模式说明：</b>调用者持有命令对象的引用，通过调用命令的 {@code execute()} 方法触发请求，
 * 通过 {@code undo()} 方法撤销上一个命令。调用者不关心命令的具体实现，
 * 也不直接与接收者交互——所有请求都通过命令对象间接完成。</p>
 *
 * <p><b>命名立意：</b>智能家居遥控器是命令模式的典型应用场景——
 * 遥控器上有多个按钮（slot），每个按钮可以绑定不同的命令（开灯、关灯、调光等）。
 * 用户按下按钮时，遥控器执行对应的命令；按下"撤销"按钮时，遥控器撤销上一次执行的命令。
 * 遥控器完全不知道家电设备的存在，只与 Command 接口交互。</p>
 *
 * <p><b>核心特性：</b></p>
 * <ul>
 *   <li>支持为不同按钮绑定不同命令（多设备控制）</li>
 *   <li>维护命令历史记录，支持撤销操作</li>
 *   <li>可以扩展支持宏命令（一次执行多个命令）</li>
 *   <li>可以扩展支持命令队列、日志记录等功能</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Command
 */
public class RemoteControl {

    /**
     * 遥控器按钮数量。
     */
    private static final int SLOT_COUNT = 7;

    /**
     * 按钮槽位：存储绑定的命令（索引 0-6 对应 7 个按钮）。
     */
    private final Command[] slots;

    /**
     * 命令历史记录：用于撤销操作。
     */
    private final List<Command> history;

    public RemoteControl() {
        this.slots = new Command[SLOT_COUNT];
        this.history = new ArrayList<>();
    }

    /**
     * 为指定按钮绑定命令。
     *
     * <p>遥控器不关心命令控制的是什么设备，只负责在按钮被按下时执行命令。</p>
     *
     * @param slot    按钮索引（0-6）
     * @param command 要绑定的命令
     */
    public void setCommand(int slot, Command command) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            throw new IllegalArgumentException("按钮索引必须在 0-" + (SLOT_COUNT - 1) + " 之间");
        }
        this.slots[slot] = command;
        System.out.println(String.format("  [遥控器] 按钮 %d 已绑定：%s", slot, command.getName()));
    }

    /**
     * 按下指定按钮，执行对应的命令。
     *
     * <p>这是调用者的核心方法：找到按钮对应的命令对象，调用其 execute() 方法，
     * 并将命令加入历史记录以便后续撤销。</p>
     *
     * @param slot 按钮索引（0-6）
     */
    public void pressButton(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            throw new IllegalArgumentException("按钮索引必须在 0-" + (SLOT_COUNT - 1) + " 之间");
        }

        Command command = slots[slot];
        if (command == null) {
            System.out.println(String.format("  [遥控器] 按钮 %d 未绑定任何命令", slot));
            return;
        }

        System.out.println(String.format("\n  [遥控器] 按下按钮 %d → 执行：%s", slot, command.getName()));
        command.execute();
        
        // 记录到历史（用于撤销）
        history.add(command);
    }

    /**
     * 撤销上一次执行的命令。
     *
     * <p>从历史记录中取出最后一个命令，调用其 undo() 方法恢复到执行前的状态。
     * 这是命令模式的核心特性之一——支持撤销操作。</p>
     */
    public void pressUndo() {
        if (history.isEmpty()) {
            System.out.println("\n  [遥控器] 没有可撤销的命令");
            return;
        }

        Command lastCommand = history.remove(history.size() - 1);
        System.out.println(String.format("\n  [遥控器] 按下撤销按钮 → 撤销：%s", lastCommand.getName()));
        lastCommand.undo();
    }

    /**
     * 获取命令历史记录。
     *
     * @return 已执行的命令列表
     */
    public List<Command> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * 清空命令历史。
     */
    public void clearHistory() {
        history.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RemoteControl{");
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                sb.append(String.format("\n  按钮%d: %s", i, slots[i].getName()));
            }
        }
        sb.append("\n}");
        return sb.toString();
    }
}
