/**
 * 观察者模式（Observer）
 * <p>
 * 定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
 * 分类：行为型模式
 * </p>
 *
 * <h3>模式说明</h3>
 * <p>观察者模式又称发布-订阅模式（Publish-Subscribe）。核心思想是：一个目标对象（Subject）维护一个观察者列表，
 * 当目标状态发生变化时，自动通知所有观察者（Observer）进行相应更新。这种模式实现了目标与观察者之间的松耦合。</p>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>一个对象的改变需要同时改变其它对象，但不知道具体有多少对象有待改变</li>
 *   <li>事件驱动的系统、消息发布订阅</li>
 *   <li>解耦发布者和订阅者，使它们可以独立变化</li>
 * </ul>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li>JDK：java.util.Observer/Observable（已废弃）、java.beans.PropertyChangeListener</li>
 *   <li>Spring：ApplicationListener 与 ApplicationEvent、@EventListener 注解</li>
 *   <li>Spring Boot：生命周期事件（ApplicationStartingEvent、ApplicationReadyEvent 等）</li>
 *   <li>Guava：EventBus</li>
 *   <li>Reactor/RxJava：响应式编程</li>
 * </ul>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Subject（目标/被观察者）
 *    ├─ attach(Observer)              ← 注册观察者
 *    ├─ detach(Observer)              ← 移除观察者
 *    └─ notify()                      ← 通知所有观察者
 *
 *  Observer（观察者）
 *    └─ update(Subject)               ← 接收通知并更新
 *
 *  ConcreteSubject（具体目标）
 *    └─ WeatherStation                ← 气象站（维护温度、湿度等状态）
 *
 *  ConcreteObserver（具体观察者）
 *    ├─ PhoneDisplay                  ← 手机显示屏
 *    ├─ ComputerDisplay               ← 电脑显示屏
 *    └─ StatisticsDisplay             ← 统计显示屏
 * </pre>
 *
 * <h3>本示例场景</h3>
 * <p>气象站系统：气象站（WeatherStation）作为被观察者，实时监测温度、湿度、气压等数据。
 * 多个显示设备（手机、电脑、统计面板）作为观察者订阅气象站数据。
 * 当气象站数据更新时，所有显示设备自动接收到最新数据并刷新显示。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21
 */
package i2f.design.pattern.behavioral.observer;
