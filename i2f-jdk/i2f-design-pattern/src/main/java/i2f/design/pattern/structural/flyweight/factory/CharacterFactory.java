package i2f.design.pattern.structural.flyweight.factory;

import i2f.design.pattern.structural.flyweight.character.Char;
import i2f.design.pattern.structural.flyweight.character.impl.ConcreteChar;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元模式 —— 字符工厂（Flyweight Factory：CharacterFactory）
 *
 * <p><b>角色：</b>享元工厂（Flyweight Factory）</p>
 *
 * <p><b>模式说明：</b>创建并管理享元对象池，确保相同内部状态的对象只创建一次。
 * 当客户端请求一个字符时，工厂首先检查池中是否已存在该字符：
 * 如果存在则返回共享实例，如果不存在则创建新实例并放入池中。</p>
 *
 * <p><b>命名立意：</b>"字符工厂"充当享元池的管理者——
 * 文档中可能包含成千上万个字符，但 ASCII 字符集仅有 128 个字符，
 * 通过共享技术，内存中最多只需要 128 个字符实例，而非数百万个。</p>
 *
 * <p><b>核心方法：</b>{@link #getCharacter(char)} —— 获取享元对象，
 * 如果池中不存在则创建，如果已存在则返回共享实例。</p>
 *
 * <p><b>内存优化效果：</b></p>
 * <pre>
 *  未使用享元模式                    使用享元模式
 *  ────────────────────────────   ────────────────────────────
 *  100万字文档 = 100万个对象         100万字文档 = 最多128个对象（ASCII）
 *  每个对象占用 ~64 bytes           每个对象占用 ~64 bytes
 *  总内存：~64 MB                   总内存：~8 KB（节省 99.99%）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Char
 * @see ConcreteChar
 */
public class CharacterFactory {

    /**
     * 享元池：缓存已创建的字符享元对象。
     * Key: 字符本身（内部状态），Value: 享元实例。
     */
    private final Map<Character, Char> flyweightPool = new HashMap<>();

    /**
     * 获取字符享元对象。
     *
     * <p>这是享元模式的核心方法：
     * 1. 检查享元池中是否已存在该字符
     * 2. 如果存在，返回共享实例（节省内存）
     * 3. 如果不存在，创建新实例并放入池中</p>
     *
     * <p>与工厂方法模式的区别：
     * - 工厂方法每次调用都创建新实例
     * - 享元工厂相同键值返回同一实例</p>
     *
     * @param symbol 字符符号（内部状态）
     * @return 字符享元对象（可能是新建的，也可能是共享的）
     */
    public Char getCharacter(char symbol) {
        // 使用 Character.valueOf(symbol) 确保包装类型的一致性
        Character key = Character.valueOf(symbol);
        
        // 检查享元池中是否已存在该字符
        if (!flyweightPool.containsKey(key)) {
            // 不存在：创建新享元并放入池中
            System.out.println("  [工厂] 创建新享元: '" + symbol + "'");
            ConcreteChar newChar = new ConcreteChar(symbol);
            flyweightPool.put(key, newChar);
            return newChar;
        }
        
        // 已存在：返回共享实例
        System.out.println("  [工厂] 复用已有享元: '" + symbol + "'");
        return flyweightPool.get(key);
    }

    /**
     * 获取享元池大小。
     *
     * @return 已创建的享元对象数量
     */
    public int getPoolSize() {
        return flyweightPool.size();
    }

    /**
     * 清空享元池（一般不需要，此处仅用于演示）。
     */
    public void clearPool() {
        flyweightPool.clear();
    }
}
