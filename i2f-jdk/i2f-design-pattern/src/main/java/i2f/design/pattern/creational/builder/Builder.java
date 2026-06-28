package i2f.design.pattern.creational.builder;

/**
 * 建造者模式 —— 抽象建造者（Builder：ComputerBuilder）
 *
 * <p><b>角色：</b>抽象建造者（Builder）</p>
 *
 * <p><b>模式说明：</b>定义创建 {@link Computer} 各个部件的抽象接口。
 * 建造者模式将复杂对象的构建过程分解为多个步骤，每个步骤由具体建造者实现，
 * 从而使得同样的构建过程可以创建不同的产品表示。</p>
 *
 * <p><b>命名立意：</b>以"电脑组装"为场景——
 * 抽象建造者定义组装流程的标准步骤（装CPU → 装内存 → 装硬盘 → 装显卡等），
 * 但不指定具体用什么配件。具体建造者（如办公电脑建造者、游戏电脑建造者）
 * 实现这些步骤，决定每个部件的具体规格。</p>
 *
 * <p><b>与工厂方法模式的区别：</b></p>
 * <pre>
 *  工厂方法（Factory Method）              建造者模式（Builder）
 *  ───────────────────────────────────    ───────────────────────────────────
 *  关注创建单个对象                         关注分步骤构建复杂对象
 *  一次调用即返回完整实例                    多次调用逐步构建，最后返回产品
 *  适合简单对象的创建                        适合多部件、多配置复杂对象的创建
 *  通过子类决定创建哪种类型                  通过不同建造者决定产品的内部配置
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Builder（抽象建造者）
 *    ├─ buildCPU()
 *    ├─ buildMemory()
 *    ├─ buildStorage()
 *    ├─ buildGPU()
 *    ├─ buildMonitor()
 *    └─ buildSSD()
 *    └─ getComputer(): Computer
 *
 *  ConcreteBuilder（具体建造者）
 *    ├─ OfficeComputerBuilder  → 构建办公电脑（低配）
 *    └─ GamingComputerBuilder  → 构建游戏电脑（高配）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Computer
 * @see i2f.design.pattern.creational.builder.impl.OfficeComputerBuilder
 * @see i2f.design.pattern.creational.builder.impl.GamingComputerBuilder
 * @see i2f.design.pattern.creational.builder.Director
 */
public interface Builder {

    /**
     * 安装 CPU。
     *
     * @param cpu CPU 型号
     * @return 建造者自身（支持链式调用）
     */
    Builder buildCPU(String cpu);

    /**
     * 安装内存。
     *
     * @param memory 内存大小（GB）
     * @return 建造者自身（支持链式调用）
     */
    Builder buildMemory(int memory);

    /**
     * 安装硬盘。
     *
     * @param storage 硬盘类型与容量
     * @return 建造者自身（支持链式调用）
     */
    Builder buildStorage(String storage);

    /**
     * 安装显卡。
     *
     * @param gpu 显卡型号
     * @return 建造者自身（支持链式调用）
     */
    Builder buildGPU(String gpu);

    /**
     * 安装显示器。
     *
     * @param monitorSize 显示器尺寸（英寸）
     * @return 建造者自身（支持链式调用）
     */
    Builder buildMonitor(double monitorSize);

    /**
     * 安装固态硬盘（可选）。
     *
     * @param hasSSD 是否包含固态硬盘
     * @return 建造者自身（支持链式调用）
     */
    Builder buildSSD(boolean hasSSD);

    /**
     * 获取构建完成的电脑。
     *
     * @return 构建完成的 {@link Computer} 实例
     */
    Computer getComputer();
}
