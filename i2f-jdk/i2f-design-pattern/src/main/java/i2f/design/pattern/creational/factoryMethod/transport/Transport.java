package i2f.design.pattern.creational.factoryMethod.transport;

import i2f.design.pattern.creational.factoryMethod.transport.impl.Ship;
import i2f.design.pattern.creational.factoryMethod.transport.impl.Truck;
import i2f.design.pattern.creational.factoryMethod.logistics.Logistics;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工厂方法模式 —— 运输工具（Product：Transport）
 *
 * <p><b>角色：</b>抽象产品（Abstract Product）</p>
 *
 * <p><b>模式说明：</b>定义产品的公共接口和通用属性，具体产品由子类实现。
 * 工厂方法模式中，Creator（工厂）返回的就是该抽象产品类型，
 * 客户端仅依赖此抽象类型，无需感知具体产品的实现细节。</p>
 *
 * <p><b>命名立意：</b>以"物流运输"为场景——物流公司（工厂）根据业务需要
 * 创建不同的运输工具（产品），公路物流创建卡车、海运物流创建轮船，
 * 客户端只需面对"运输工具"这一抽象，无需关心具体是哪种交通工具在配送。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象产品              具体产品
 *  ─────────────────   ─────────────────────────────
 *  Transport            Truck（卡车 —— 公路运输）
 *                       Ship（轮船 —— 海洋运输）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Truck
 * @see Ship
 * @see Logistics
 */
@Data
@NoArgsConstructor
public abstract class Transport {

    /**
     * 运输工具名称。
     */
    private String name;

    /**
     * 最大载重量（吨）。
     */
    private double capacity;

    /**
     * 最大速度（km/h）。
     */
    private double speed;

    public Transport(String name, double capacity, double speed) {
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    /**
     * 执行配送任务。
     *
     * <p>不同的运输工具以不同的方式完成配送，由子类提供具体实现。</p>
     *
     * @param destination 目的地
     * @return 配送结果描述
     */
    public abstract String deliver(String destination);

    /**
     * 获取运输工具类型描述。
     *
     * @return 类型名称
     */
    public abstract String getType();
}
