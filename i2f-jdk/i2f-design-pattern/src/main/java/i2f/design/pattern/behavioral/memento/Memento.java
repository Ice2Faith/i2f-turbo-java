package i2f.design.pattern.behavioral.memento;

import lombok.Data;

/**
 * 备忘录模式 —— 备忘录（Memento）
 *
 * <p><b>角色：</b>备忘录（Memento）—— 存储发起人内部状态的容器</p>
 *
 * <p><b>模式说明：</b>Memento 负责保存 Originator 的内部状态快照。
 * 为了保证封装性，Memento 通常对外部（Caretaker）是不可见的，
 * 只有 Originator 可以创建和读取 Memento 的内容。</p>
 *
 * <p><b>封装性设计：</b>在实际应用中，Memento 可以通过以下方式实现封装：</p>
 * <ul>
 *   <li>将 Memento 设为 Originator 的私有内部类（最严格的封装）</li>
 *   <li>使用包级私有访问控制（本实现采用此方式）</li>
 *   <li>通过接口暴露只读方法，隐藏写方法</li>
 * </ul>
 *
 * <p><b>命名立意：</b>以"游戏存档文件"为类比——存档文件包含角色的所有状态数据，
 * 但玩家（Caretaker）只能保存和读取存档，不能直接修改存档内容，
 * 只有游戏引擎（Originator）才能解析和应用存档数据。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see GameRole
 * @see Caretaker
 */
@Data
public class Memento {

    /**
     * 角色等级（保存的状态）。
     */
    private final int level;

    /**
     * 生命值（保存的状态）。
     */
    private final int hp;

    /**
     * 魔法值（保存的状态）。
     */
    private final int mp;

    /**
     * 攻击力（保存的状态）。
     */
    private final int attack;

    /**
     * 防御力（保存的状态）。
     */
    private final int defense;

    /**
     * 创建备忘录，保存指定状态。
     *
     * <p>使用 final 字段确保备忘录一旦创建就不可修改，
     * 这是备忘录模式的重要特性——状态快照应该是不可变的。</p>
     *
     * @param level 角色等级
     * @param hp 生命值
     * @param mp 魔法值
     * @param attack 攻击力
     * @param defense 防御力
     */
    public Memento(int level, int hp, int mp, int attack, int defense) {
        this.level = level;
        this.hp = hp;
        this.mp = mp;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * 获取备忘录描述信息。
     *
     * @return 备忘录状态的字符串描述
     */
    @Override
    public String toString() {
        return String.format("存档 [等级: %d | HP: %d | MP: %d | 攻击: %d | 防御: %d]",
                level, hp, mp, attack, defense);
    }
}
