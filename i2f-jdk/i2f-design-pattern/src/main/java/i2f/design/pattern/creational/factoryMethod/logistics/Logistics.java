package i2f.design.pattern.creational.factoryMethod.logistics;

import i2f.design.pattern.creational.factoryMethod.transport.Transport;
import i2f.design.pattern.creational.factoryMethod.transport.impl.Ship;
import i2f.design.pattern.creational.factoryMethod.transport.impl.Truck;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.RoadLogistics;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.SeaLogistics;

/**
 * 工厂方法模式 —— 物流公司（Creator：Logistics）
 *
 * <p><b>角色：</b>抽象创建者（Abstract Creator）</p>
 *
 * <p><b>模式说明：</b>定义创建产品对象的工厂方法 {@link #createTransport()}，
 * 但不指定具体产品类——由子类决定实例化哪一种运输工具。
 * 这就是工厂方法模式的核心：<b>"将对象的实例化延迟到子类"</b>。</p>
 *
 * <p><b>命名立意：</b>"物流公司"天然地充当"工厂"角色——
 * 公路物流公司（{@link RoadLogistics}）创建卡车（{@link Truck}），
 * 海运物流公司（{@link SeaLogistics}）创建轮船（{@link Ship}）。
 * 客户端只需告诉物流公司"我要配送"，至于用什么交通工具，由具体物流公司自行决定。</p>
 *
 * <p><b>与简单工厂的区别：</b></p>
 * <pre>
 *  简单工厂（Static Factory）              工厂方法（Factory Method）
 *  ─────────────────────────────────────   ─────────────────────────────────────
 *  一个工厂类包含所有创建逻辑（if/switch）    每个具体工厂只负责创建一种产品
 *  新增产品需修改工厂类（违反开闭原则）        新增产品只需新增一个工厂子类（符合开闭原则）
 *  工厂类职责过重                            职责分散到各子类，符合单一职责
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Creator（抽象创建者）
 *    └─ createTransport(): Transport        ← 工厂方法（抽象）
 *    └─ planDelivery(destination): void     ← 业务方法（使用工厂方法的结果）
 *
 *  ConcreteCreator（具体创建者）
 *    ├─ RoadLogistics  → createTransport() 返回 Truck
 *    └─ SeaLogistics   → createTransport() 返回 Ship
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Transport
 * @see RoadLogistics
 * @see SeaLogistics
 */
public abstract class Logistics {

    /**
     * 工厂方法（Factory Method）—— 创建运输工具。
     *
     * <p>子类必须实现此方法，决定具体创建哪种运输工具。
     * 这是整个工厂方法模式的核心所在。</p>
     *
     * @return 具体的运输工具实例
     */
    public abstract Transport createTransport();

    /**
     * 规划并执行配送任务。
     *
     * <p>该方法体现了工厂方法模式的使用方式：
     * 调用工厂方法获取产品实例，然后基于该产品执行业务逻辑。
     * 此方法<b>不关心</b>具体是哪种运输工具在配送——多态的力量。</p>
     *
     * @param destination 配送目的地
     */
    public void planDelivery(String destination) {
        // 1. 调用工厂方法创建运输工具（具体类型由子类决定）
        Transport transport = createTransport();

        // 2. 使用产品执行业务（面向抽象编程，无需知道具体类型）
        System.out.println("  物流公司：" + getCompanyName());
        System.out.println("  运输工具：" + transport);
        System.out.println("  配送执行：" + transport.deliver(destination));
    }

    /**
     * 获取物流公司名称。
     *
     * @return 公司名称
     */
    public abstract String getCompanyName();
}
