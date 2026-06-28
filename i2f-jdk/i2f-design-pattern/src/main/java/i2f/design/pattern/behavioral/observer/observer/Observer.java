package i2f.design.pattern.behavioral.observer.observer;

import i2f.design.pattern.behavioral.observer.subject.Subject;
import i2f.design.pattern.behavioral.observer.subject.WeatherStation;

/**
 * 观察者模式 —— 观察者（Observer）
 *
 * <p><b>角色：</b>抽象观察者（Abstract Observer）</p>
 *
 * <p><b>模式说明：</b>定义观察者的更新接口，当目标对象状态发生变化时，
 * 调用此方法通知观察者进行相应的更新操作。</p>
 *
 * <p><b>命名立意：</b>在气象站场景中，"观察者"就是各种显示设备（手机、电脑、统计面板等），
 * 它们订阅气象站的数据变化。当气象站数据更新时，所有显示设备通过 update() 方法
 * 获取最新数据并刷新显示。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Observer（抽象观察者）
 *    └─ update(Subject)         ← 接收通知并更新
 *
 *  ConcreteObserver（具体观察者）
 *    ├─ PhoneDisplay            ← 手机显示屏
 *    ├─ ComputerDisplay         ← 电脑显示屏
 *    └─ StatisticsDisplay       ← 统计显示屏
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Subject
 * @see WeatherStation
 */
public interface Observer {

    /**
     * 更新方法。
     *
     * <p>当目标对象状态发生变化时，目标会调用此方法通知观察者。
     * 观察者可以通过 Subject 参数获取目标的最新状态，并进行相应的更新操作。</p>
     *
     * @param subject 目标对象（被观察者），观察者可通过它获取最新状态
     */
    void update(Subject subject);
}
