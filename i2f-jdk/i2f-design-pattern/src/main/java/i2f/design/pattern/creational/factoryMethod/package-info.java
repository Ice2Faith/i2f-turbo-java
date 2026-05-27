/**
 * 工厂方法模式（Factory Method）
 *
 * <p><b>定义：</b>定义一个用于创建对象的接口，让子类决定实例化哪一个类，使一个类的实例化延迟到其子类。</p>
 * <p><b>分类：</b>创建型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>创建对象时不希望暴露具体实现类。</li>
 *   <li>一个类希望由其子类来决定创建哪种对象。</li>
 *   <li>系统需要根据不同条件创建不同对象。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"物流运输"为场景——物流公司（Creator）通过工厂方法创建运输工具（Product），
 * 公路物流创建卡车、海运物流创建轮船，客户端面向抽象编程，无需关心具体产品类型。</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.transport.Transport} —— 抽象产品（运输工具）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.transport.impl.Truck} —— 具体产品（卡车）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.transport.impl.Ship} —— 具体产品（轮船）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.logistics.Logistics} —— 抽象创建者（物流公司）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.logistics.impl.RoadLogistics} —— 具体创建者（公路物流）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.logistics.impl.SeaLogistics} —— 具体创建者（海运物流）</li>
 *   <li>{@link i2f.design.pattern.creational.factoryMethod.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.creational.factoryMethod.transport.Transport
 * @see i2f.design.pattern.creational.factoryMethod.logistics.Logistics
 * @see i2f.design.pattern.creational.factoryMethod.Test
 */
package i2f.design.pattern.creational.factoryMethod;
