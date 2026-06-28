package i2f.design.pattern.behavioral.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录模式 —— 管理者（Caretaker：SaveManager）
 *
 * <p><b>角色：</b>管理者（Caretaker）—— 保存和管理备忘录，但不能修改备忘录内容</p>
 *
 * <p><b>模式说明：</b>Caretaker 负责保存 Memento 对象，但不能访问或修改 Memento 内部的状态。
 * 它只负责存储和提供备忘录，真正的状态读取和恢复由 Originator 完成。
 * 这确保了 Originator 的封装性不被破坏。</p>
 *
 * <p><b>命名立意：</b>以"游戏存档管理器"为场景——玩家可以保存多个存档（slot 1, slot 2...），
 * 也可以加载指定存档。存档管理器只负责存储存档文件，不关心存档里的具体内容，
 * 只有游戏引擎才能解析和应用存档数据。</p>
 *
 * <p><b>功能特性：</b></p>
 * <ul>
 *   <li>支持保存多个备忘录（多存档位）</li>
 *   <li>支持撤销操作（读取上一个存档）</li>
 *   <li>备忘录列表可索引访问</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Memento
 * @see GameRole
 */
public class SaveManager {

    /**
     * 存档名称列表。
     */
    private final List<String> saveNames;

    /**
     * 备忘录列表（实际存储的存档数据）。
     */
    private final List<Memento> mementos;

    /**
     * 当前存档索引。
     */
    private int currentIndex;

    public SaveManager() {
        this.saveNames = new ArrayList<>();
        this.mementos = new ArrayList<>();
        this.currentIndex = -1;
    }

    /**
     * 保存新的备忘录（创建新存档）。
     *
     * <p>将新的状态快照添加到存档列表中，
     * 并更新当前索引指向最新存档。</p>
     *
     * @param saveName 存档名称（如"存档1"、"Boss战前"）
     * @param memento 包含角色状态的备忘录对象
     */
    public void addMemento(String saveName, Memento memento) {
        saveNames.add(saveName);
        mementos.add(memento);
        currentIndex = mementos.size() - 1;
        System.out.println("  ✓ 已保存：" + saveName);
    }

    /**
     * 获取指定索引的备忘录（读取存档）。
     *
     * <p>注意：Caretaker 只能提供 Memento 对象，
     * 但不能访问或修改 Memento 内部的状态数据。</p>
     *
     * @param index 存档索引（从 0 开始）
     * @return 指定位置的备忘录对象
     */
    public Memento getMemento(int index) {
        if (index < 0 || index >= mementos.size()) {
            throw new IndexOutOfBoundsException("存档索引越界：共有 " + mementos.size() + " 个存档");
        }
        return mementos.get(index);
    }

    /**
     * 获取最新的备忘录（读取最新存档）。
     *
     * @return 最新的备忘录对象
     */
    public Memento getLatestMemento() {
        if (mementos.isEmpty()) {
            throw new IllegalStateException("没有可用的存档");
        }
        return mementos.get(currentIndex);
    }

    /**
     * 撤销到上一个存档。
     *
     * <p>将当前索引向前移动一位，返回上一个备忘录。
     * 这是备忘录模式支持"撤销"操作的关键方法。</p>
     *
     * @return 上一个备忘录对象，如果已在第一个存档则返回 null
     */
    public Memento undo() {
        if (currentIndex > 0) {
            currentIndex--;
            System.out.println("  ↩ 撤销到：" + saveNames.get(currentIndex));
            return mementos.get(currentIndex);
        }
        System.out.println("  ⚠ 已在第一个存档，无法继续撤销");
        return null;
    }

    /**
     * 显示所有存档信息。
     */
    public void showAllSaves() {
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.println("  │  存档列表                               │");
        System.out.println("  ├─────────────────────────────────────────┤");
        for (int i = 0; i < mementos.size(); i++) {
            String marker = (i == currentIndex) ? " ◄ 当前" : "";
            System.out.printf("  │  [%d] %s%s%n", i, saveNames.get(i), marker);
            System.out.println("  │      " + mementos.get(i));
            System.out.println("  ├─────────────────────────────────────────┤");
        }
        System.out.println("  └─────────────────────────────────────────┘");
    }

    /**
     * 获取存档数量。
     *
     * @return 存档总数
     */
    public int getSaveCount() {
        return mementos.size();
    }
}
