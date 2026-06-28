package i2f.design.pattern.behavioral.memento;

import lombok.Data;

/**
 * 备忘录模式 —— 游戏角色（Originator：GameRole）
 *
 * <p><b>角色：</b>发起人（Originator）—— 需要保存状态的对象</p>
 *
 * <p><b>模式说明：</b>游戏角色可以创建备忘录来保存当前状态（等级、生命值、魔法值等），
 * 也可以从备忘录中恢复状态。这模拟了游戏中的"存档/读档"功能。</p>
 *
 * <p><b>命名立意：</b>以"游戏角色存档"为场景——玩家可以在关键时刻保存角色状态，
 * 如果后续操作不满意（如打 Boss 失败），可以读取存档恢复到之前的状态。
 * 角色的内部状态（等级、生命值等）通过备忘录封装，外部无法直接修改。</p>
 *
 * <p><b>封装性体现：</b>GameRole 的状态通过 Memento 对象保存，但 Memento 的具体内容
 * 对外部是不可见的（通过私有构造和包级访问控制），只有 Originator 本身可以访问。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Memento
 * @see Caretaker
 */
@Data
public class GameRole {

    /**
     * 角色名称。
     */
    private String name;

    /**
     * 角色等级。
     */
    private int level;

    /**
     * 生命值（HP）。
     */
    private int hp;

    /**
     * 魔法值（MP）。
     */
    private int mp;

    /**
     * 攻击力。
     */
    private int attack;

    /**
     * 防御力。
     */
    private int defense;

    public GameRole(String name, int level, int hp, int mp, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.mp = mp;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * 创建备忘录 —— 保存当前角色状态。
     *
     * <p>此方法将角色的所有关键状态属性封装到 Memento 对象中，
     * 以便后续恢复。这是备忘录模式的核心操作之一。</p>
     *
     * @return 包含当前状态的备忘录对象
     */
    public Memento createMemento() {
        return new Memento(this.level, this.hp, this.mp, this.attack, this.defense);
    }

    /**
     * 从备忘录恢复状态 —— 读取存档。
     *
     * <p>从传入的 Memento 对象中恢复角色的所有状态属性。
     * 这是备忘录模式的另一个核心操作。</p>
     *
     * @param memento 包含历史状态的备忘录对象
     */
    public void restoreMemento(Memento memento) {
        this.level = memento.getLevel();
        this.hp = memento.getHp();
        this.mp = memento.getMp();
        this.attack = memento.getAttack();
        this.defense = memento.getDefense();
    }

    /**
     * 显示角色当前状态。
     *
     * @return 角色状态的字符串描述
     */
    public String displayStatus() {
        return String.format("[%s] 等级: %d | HP: %d | MP: %d | 攻击: %d | 防御: %d",
                name, level, hp, mp, attack, defense);
    }

    /**
     * 模拟战斗 —— 消耗生命值。
     *
     * @param damage 受到的伤害值
     */
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
        System.out.println("  → " + name + " 受到 " + damage + " 点伤害，剩余 HP: " + this.hp);
    }

    /**
     * 模拟升级 —— 提升角色属性。
     */
    public void levelUp() {
        this.level++;
        this.hp += 50;
        this.mp += 30;
        this.attack += 10;
        this.defense += 5;
        System.out.println("  → " + name + " 升级了！当前等级: " + this.level);
    }
}
