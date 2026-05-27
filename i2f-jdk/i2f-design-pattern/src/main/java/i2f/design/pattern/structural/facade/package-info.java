/**
 * 外观模式（Facade）
 * <p>
 * 为子系统中的一组接口提供一个一致的高层接口，使得子系统更加容易使用。
 * 分类：结构型模式
 * </p>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Client（客户端）
 *    └─ 调用 Facade（外观类）
 *         └─ SmartHomeFacade
 *              ├─ LightSystem        （灯光子系统）
 *              ├─ AirConditioningSystem  （空调子系统）
 *              ├─ AudioSystem        （音响子系统）
 *              └─ CurtainSystem      （窗帘子系统）
 *
 *  Facade 提供的高层方法：
 *    ├─ goHomeMode()     → 回家模式
 *    ├─ leaveHomeMode()  → 离家模式
 *    ├─ cinemaMode()     → 影院模式
 *    ├─ sleepMode()      → 睡眠模式
 *    └─ wakeUpMode()     → 起床模式
 * </pre>
 *
 * <h3>演示入口</h3>
 * <p>运行 {@link i2f.design.pattern.structural.facade.Test#main(String[])} 查看完整演示</p>
 *
 * <h3>角色说明</h3>
 * <ul>
 *   <li><b>Facade（外观类）：</b>{@link SmartHomeFacade} - 为子系统提供统一接口</li>
 *   <li><b>Subsystem（子系统）：</b>{@link i2f.design.pattern.structural.facade.subsystem.LightSystem} 等 - 实现具体功能</li>
 *   <li><b>Client（客户端）：</b>{@link Test} - 通过外观类访问子系统</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
package i2f.design.pattern.structural.facade;
