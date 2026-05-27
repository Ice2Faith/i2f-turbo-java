package i2f.design.pattern.behavioral.memento;

/**
 * 备忘录模式 —— 调用演示
 *
 * <p>演示备忘录模式的核心机制：通过 Memento 对象保存和恢复 Originator 的内部状态，
 * 实现"存档/读档"功能，同时保持 Originator 的封装性不被破坏。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 备忘录模式核心演示 ====================
        System.out.println("====== 备忘录模式（Memento）演示 ======");
        System.out.println("场景：游戏角色（Originator）通过备忘录（Memento）保存状态");
        System.out.println("      存档管理器（Caretaker）负责管理多个存档\n");

        // 创建游戏角色
        GameRole hero = new GameRole("勇者·亚瑟", 1, 100, 50, 20, 10);
        System.out.println("初始状态：");
        System.out.println("  " + hero.displayStatus());
        System.out.println();

        // ==================== 2. 第一次存档 ====================
        System.out.println("────── 步骤 1：创建存档管理器并保存第一个存档 ──────");
        SaveManager saveManager = new SaveManager();
        
        // 保存初始状态（新手村存档）
        Memento save1 = hero.createMemento();
        saveManager.addMemento("新手村", save1);
        System.out.println("  存档内容：" + save1);
        System.out.println();

        // ==================== 3. 角色成长并第二次存档 ====================
        System.out.println("────── 步骤 2：角色战斗升级后保存第二个存档 ──────");
        hero.levelUp();
        hero.levelUp();
        System.out.println("  升级后状态：" + hero.displayStatus());
        
        // 保存升级后的状态（Boss 战前存档）
        Memento save2 = hero.createMemento();
        saveManager.addMemento("Boss战前", save2);
        System.out.println("  存档内容：" + save2);
        System.out.println();

        // ==================== 4. 模拟战斗失败并恢复存档 ====================
        System.out.println("────── 步骤 3：挑战 Boss 失败，读取存档恢复 ──────");
        hero.takeDamage(150);
        System.out.println("  战斗后状态：" + hero.displayStatus());
        System.out.println("  ⚠ HP 归零，挑战失败！准备读取存档...\n");
        
        // 读取 Boss 战前存档
        System.out.println("  读取存档：[Boss战前]");
        Memento restoreSave = saveManager.getMemento(1);
        hero.restoreMemento(restoreSave);
        System.out.println("  恢复后状态：" + hero.displayStatus());
        System.out.println("  ✓ 成功恢复到 Boss 战前的状态！\n");

        // ==================== 5. 第三次存档 ====================
        System.out.println("────── 步骤 4：准备充分后保存第三个存档 ──────");
        hero.levelUp();
        System.out.println("  再次升级后：" + hero.displayStatus());
        
        Memento save3 = hero.createMemento();
        saveManager.addMemento("最终决战前", save3);
        System.out.println("  存档内容：" + save3);
        System.out.println();

        // ==================== 6. 查看所有存档 ====================
        System.out.println("────── 步骤 5：查看所有存档 ──────");
        saveManager.showAllSaves();
        System.out.println();

        // ==================== 7. 演示撤销操作 ====================
        System.out.println("────── 步骤 6：演示撤销（Undo）功能 ──────");
        System.out.println("  当前状态：" + hero.displayStatus());
        
        // 模拟一次战斗
        hero.takeDamage(30);
        System.out.println("  战斗后：" + hero.displayStatus());
        
        // 使用撤销功能
        System.out.println("\n  执行撤销操作：");
        Memento undoSave = saveManager.undo();
        if (undoSave != null) {
            hero.restoreMemento(undoSave);
            System.out.println("  撤销后状态：" + hero.displayStatus());
        }
        System.out.println();

        // ==================== 8. 验证封装性 ====================
        System.out.println("====== 验证：备忘录的封装性保护 ======");
        System.out.println("Caretaker（SaveManager）只能保存和提供 Memento，");
        System.out.println("但无法直接访问或修改 Memento 内部的状态数据。");
        System.out.println("只有 Originator（GameRole）才能读取和应用备忘录。\n");
        
        Memento testMemento = saveManager.getLatestMemento();
        System.out.println("从 SaveManager 获取的备忘录：");
        System.out.println("  " + testMemento);
        System.out.println("  （外部只能看到 toString() 的描述，无法修改内部状态）\n");

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 备忘录模式优势总结 ======");
        System.out.println("1. 封装性保护：Originator 的内部状态不会暴露给外部");
        System.out.println("2. 简化 Originator：不需要自己维护状态历史，交由 Caretaker 管理");
        System.out.println("3. 支持撤销/重做：通过保存多个备忘录实现历史回溯");
        System.out.println("4. 状态快照不可变：Memento 使用 final 字段，确保存档不被篡改");
        System.out.println("5. 职责分离：Originator 专注业务逻辑，Caretaker 专注状态管理");
        
        System.out.println("\n====== 典型应用场景 ======");
        System.out.println("• 文本编辑器的 Ctrl+Z 撤销功能");
        System.out.println("• 游戏的存档/读档系统");
        System.out.println("• 数据库事务的回滚机制（undo log）");
        System.out.println("• 浏览器的前进/后退功能");
        System.out.println("• 图形编辑软件的历史记录面板");
    }
}
