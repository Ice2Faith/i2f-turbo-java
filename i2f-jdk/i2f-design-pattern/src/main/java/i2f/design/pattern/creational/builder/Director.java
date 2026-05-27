package i2f.design.pattern.creational.builder;

/**
 * 建造者模式 —— 指挥者（Director：ComputerDirector）
 *
 * <p><b>角色：</b>指挥者（Director）</p>
 *
 * <p><b>模式说明：</b>指挥者负责定义复杂对象的构建流程，它不关心具体建造者的实现细节，
 * 只按照固定的步骤调用建造者的方法来构建产品。客户端通过指挥者来使用建造者模式，
 * 从而实现构建过程与表示的分离。</p>
 *
 * <p><b>命名立意：</b>以"电脑组装"为场景——
 * 指挥者就像流水线上的装配主管，它知道组装一台电脑需要哪些步骤
 * （装CPU → 装内存 → 装硬盘 → 装显卡 → 装显示器 → 装SSD），
 * 但不关心具体用什么配件。配件的选择由具体建造者决定。</p>
 *
 * <p><b>核心价值：</b></p>
 * <ul>
 *   <li><b>构建流程复用：</b>同一套组装流程可用于不同类型的电脑</li>
 *   <li><b>解耦构建过程与表示：</b>指挥者只定义流程，建造者决定具体配置</li>
 *   <li><b>客户端简化：</b>客户端只需选择建造者，调用指挥者的构建方法即可</li>
 * </ul>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Director（指挥者）
 *    └─ construct(Builder builder)
 *         ├─ builder.buildCPU()
 *         ├─ builder.buildMemory()
 *         ├─ builder.buildStorage()
 *         ├─ builder.buildGPU()
 *         ├─ builder.buildMonitor()
 *         └─ builder.buildSSD()
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Builder
 * @see Computer
 * @see i2f.design.pattern.creational.builder.impl.OfficeComputerBuilder
 * @see i2f.design.pattern.creational.builder.impl.GamingComputerBuilder
 */
public class Director {

    /**
     * 按照标准流程构建电脑。
     *
     * <p>该方法定义了电脑组装的固定步骤顺序，但具体配置由传入的建造者决定。
     * 这正是建造者模式的核心：<b>同样的构建过程可以创建不同的表示</b>。</p>
     *
     * @param builder 具体建造者（办公电脑建造者或游戏电脑建造者）
     * @return 构建完成的 {@link Computer} 实例
     */
    public Computer construct(Builder builder) {
        return builder
                .buildCPU("Intel Core i5-12400")
                .buildMemory(16)
                .buildStorage("1TB HDD")
                .buildGPU("Intel UHD Graphics 630")
                .buildMonitor(24.0)
                .buildSSD(true)
                .getComputer();
    }

    /**
     * 构建办公电脑——使用办公电脑建造者。
     *
     * @param builder 办公电脑建造者
     * @return 构建完成的办公电脑
     */
    public Computer constructOfficeComputer(Builder builder) {
        return builder
                .buildCPU("Intel Core i3-12100")
                .buildMemory(8)
                .buildStorage("500GB HDD + 128GB SSD")
                .buildGPU("Intel UHD Graphics 630")
                .buildMonitor(21.5)
                .buildSSD(true)
                .getComputer();
    }

    /**
     * 构建游戏电脑——使用游戏电脑建造者。
     *
     * @param builder 游戏电脑建造者
     * @return 构建完成的游戏电脑
     */
    public Computer constructGamingComputer(Builder builder) {
        return builder
                .buildCPU("AMD Ryzen 9 7950X")
                .buildMemory(64)
                .buildStorage("2TB NVMe SSD")
                .buildGPU("NVIDIA RTX 4090")
                .buildMonitor(27.0)
                .buildSSD(true)
                .getComputer();
    }
}
