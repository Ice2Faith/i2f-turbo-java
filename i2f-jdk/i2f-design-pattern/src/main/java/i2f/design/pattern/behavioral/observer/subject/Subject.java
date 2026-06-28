package i2f.design.pattern.behavioral.observer.subject;

import i2f.design.pattern.behavioral.observer.observer.Observer;

/**
 * 观察者模式 —— 目标/被观察者（Subject）
 *
 * <p><b>角色：</b>抽象目标（Abstract Subject）</p>
 *
 * <p><b>模式说明：</b>定义目标对象的接口，提供管理观察者的方法（注册、移除、通知）。
 * 目标对象维护一个观察者列表，当自身状态发生变化时，负责通知所有已注册的观察者。</p>
 *
 * <p><b>命名立意：</b>在气象站场景中，"气象站"就是被观察的目标，
 * 多个显示设备（观察者）订阅它的数据变化。气象站不关心具体有哪些显示设备，
 * 只负责在数据更新时通知所有订阅者。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Subject（抽象目标）
 *    ├─ attach(Observer)     ← 注册观察者
 *    ├─ detach(Observer)     ← 移除观察者
 *    └─ notify()             ← 通知所有观察者
 *
 *  ConcreteSubject（具体目标）
 *    └─ WeatherStation       ← 气象站，维护温度/湿度/气压等状态
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Observer
 * @see WeatherStation
 */
public interface Subject {

    /**
     * 注册观察者。
     *
     * <p>将观察者添加到订阅列表中，当目标状态变化时，该观察者将收到通知。</p>
     *
     * @param observer 要注册的观察者
     */
    void attach(Observer observer);

    /**
     * 移除观察者。
     *
     * <p>将观察者从订阅列表中移除，此后目标状态变化时，该观察者不再收到通知。</p>
     *
     * @param observer 要移除的观察者
     */
    void detach(Observer observer);

    /**
     * 通知所有观察者。
     *
     * <p>当目标状态发生变化时，遍历观察者列表，调用每个观察者的 update() 方法，
     * 将最新状态推送给所有订阅者。</p>
     */
    void notifyObservers();
}
