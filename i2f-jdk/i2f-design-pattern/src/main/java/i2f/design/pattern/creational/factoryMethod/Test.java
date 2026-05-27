package i2f.design.pattern.creational.factoryMethod;

import i2f.design.pattern.creational.factoryMethod.logistics.Logistics;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.RoadLogistics;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.SeaLogistics;
import i2f.design.pattern.creational.factoryMethod.transport.Transport;

/**
 * 工厂方法模式 —— 调用演示
 *
 * <p>演示工厂方法模式的核心机制：客户端面向抽象创建者（{@link Logistics}）编程，
 * 由具体子类决定创建哪种运输工具（{@link Transport}），实现对象创建与使用的解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 工厂方法核心演示 ====================
        System.out.println("====== 工厂方法模式（Factory Method）演示 ======");
        System.out.println("场景：物流公司（Creator）通过工厂方法创建运输工具（Product）");
        System.out.println("      不同的物流公司子类决定具体创建哪种运输工具\n");

        // ==================== 2. 公路物流 —— 创建卡车 ====================
        System.out.println("────── 公路物流配送 ──────");
        Logistics roadLogistics = new RoadLogistics();
        roadLogistics.planDelivery("北京市朝阳区");

        System.out.println();

        // ==================== 3. 海运物流 —— 创建轮船 ====================
        System.out.println("────── 海运物流配送 ──────");
        Logistics seaLogistics = new SeaLogistics();
        seaLogistics.planDelivery("美国洛杉矶港");

        System.out.println();

        // ==================== 4. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体工厂类型 ======");
        System.out.println("通过统一接口调度不同的物流公司：\n");

        Logistics[] companies = {new RoadLogistics(), new SeaLogistics()};
        String[] destinations = {"上海市浦东新区", "日本东京港"};

        for (int i = 0; i < companies.length; i++) {
            System.out.println("配送任务 " + (i + 1) + "：");
            companies[i].planDelivery(destinations[i]);
            System.out.println();
        }

        // ==================== 5. 验证工厂方法每次创建新实例 ====================
        System.out.println("====== 验证：工厂方法每次调用创建全新实例 ======");
        Transport t1 = roadLogistics.createTransport();
        Transport t2 = roadLogistics.createTransport();
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("t1 == t2 ? " + (t1 == t2));
        System.out.println("t1.equals(t2) ? " + t1.equals(t2));

        System.out.println();

        // ==================== 6. 模式优势总结 ====================
        System.out.println("====== 工厂方法模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增运输方式只需新增子类，无需修改已有代码");
        System.out.println("2. 遵循单一职责：每个具体工厂只负责创建一种产品");
        System.out.println("3. 客户端面向抽象编程：无需依赖具体产品类，降低耦合");
        System.out.println("4. 将对象的实例化延迟到子类，使框架可以灵活扩展");
    }
}
