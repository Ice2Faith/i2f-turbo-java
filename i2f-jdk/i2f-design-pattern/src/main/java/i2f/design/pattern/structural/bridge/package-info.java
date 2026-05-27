/**
 * 桥接模式（Bridge Pattern）
 * <p>
 * 将抽象部分与它的实现部分分离，使它们可以独立地变化。
 * 分类：结构型模式
 * </p>
 *
 * <h3>模式定义</h3>
 * <p>桥接模式通过<strong>组合</strong>而非继承的方式，将抽象层（Abstraction）与实现层（Implementor）解耦，
 * 使两个维度可以独立扩展，避免类爆炸问题。这是 GoF 23 种设计模式中
 * "<strong>优先使用对象组合而非类继承</strong>"原则的典型体现。</p>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>一个类存在两个或多个独立变化的维度，且这两个维度都需要扩展</li>
 *   <li>不希望使用继承导致类数量呈乘法增长（类爆炸）</li>
 *   <li>抽象与实现需要在运行时动态切换</li>
 *   <li>需要在抽象层和实现层之间建立灵活的映射关系</li>
 * </ul>
 *
 * <h3>本包类角色说明</h3>
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <tr>
 *     <th>角色</th>
 *     <th>类名</th>
 *     <th>说明</th>
 *   </tr>
 *   <tr>
 *     <td>Implementor（实现接口）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.device.Device}</td>
 *     <td>定义设备操作接口（电源、音量等）</td>
 *   </tr>
 *   <tr>
 *     <td>ConcreteImplementor（具体实现）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.device.impl.TV}</td>
 *     <td>电视设备实现，提供频道切换功能</td>
 *   </tr>
 *   <tr>
 *     <td>ConcreteImplementor（具体实现）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.device.impl.Radio}</td>
 *     <td>收音机设备实现，提供频率调谐功能</td>
 *   </tr>
 *   <tr>
 *     <td>Abstraction（抽象部分）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.remote.RemoteControl}</td>
 *     <td>遥控器抽象类，组合 Device 接口，定义控制逻辑</td>
 *   </tr>
 *   <tr>
 *     <td>RefinedAbstraction（扩展抽象）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.remote.impl.BasicRemote}</td>
 *     <td>基础遥控器，提供基本控制功能</td>
 *   </tr>
 *   <tr>
 *     <td>RefinedAbstraction（扩展抽象）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.remote.impl.AdvancedRemote}</td>
 *     <td>高级遥控器，扩展频道切换、频率调谐等功能</td>
 *   </tr>
 *   <tr>
 *     <td>Client（客户端）</td>
 *     <td>{@link i2f.design.pattern.structural.bridge.Test}</td>
 *     <td>演示入口，展示桥接模式的核心用法</td>
 *   </tr>
 * </table>
 *
 * <h3>模式结构图</h3>
 * <pre>
 *  Abstraction（抽象部分）                    Implementor（实现部分）
 *  ────────────────────────                 ────────────────────────
 *  RemoteControl                             Device
 *    └─ device: Device ◄──组合──►             ├─ powerOn()/powerOff()
 *    └─ togglePower()                         ├─ volumeUp()/volumeDown()
 *    └─ volumeUp()                            └─ getStatus()
 *    └─ volumeDown()
 *    └─ setDevice()         ◄── 桥接核心 ──►
 *
 *  RefinedAbstraction（扩展抽象）              ConcreteImplementor（具体实现）
 *  ────────────────────────────              ─────────────────────────────
 *  BasicRemote（基础遥控）                     ├─ TV（电视 —— 频道切换）
 *    └─ getRemoteType()                       └─ Radio（收音机 —— 频率调谐）
 *
 *  AdvancedRemote（高级遥控）
 *    └─ setChannel()        ──► TV.setChannel()
 *    └─ tuneFrequency()     ──► Radio.setFrequency()
 *    └─ mute()
 * </pre>
 *
 * <h3>核心思想</h3>
 * <p><strong>"抽象与实现分离"</strong> —— 遥控器（控制逻辑）不继承设备（执行逻辑），
 * 而是通过组合 {@link Device} 接口来调用设备功能。这样：
 * <ul>
 *   <li>新增遥控器类型（如语音遥控）→ 只需扩展 RemoteControl，无需修改设备</li>
 *   <li>新增设备类型（如空调）→ 只需实现 Device 接口，无需修改遥控器</li>
 *   <li>运行时动态切换 → 同一个遥控器可以控制不同的设备</li>
 * </ul>
 * </p>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li><strong>JDK/JDBC：</strong>DriverManager（抽象）与各数据库 Driver（实现）之间的解耦</li>
 *   <li><strong>Spring：</strong>PlatformTransactionManager（抽象）与 DataSourceTransactionManager、
 *       JpaTransactionManager、JtaTransactionManager（实现）相互独立扩展</li>
 *   <li><strong>Spring Data：</strong>Repository（抽象接口）与 JpaRepository、MongoRepository、
 *       RedisRepository（具体实现）独立演化</li>
 *   <li><strong>Slf4j：</strong>日志门面（抽象）与具体日志实现（log4j、logback）的分离</li>
 *   <li><strong>AWT：</strong>Component（抽象）与 Peer（平台实现）的分离</li>
 * </ul>
 *
 * <h3>演示入口</h3>
 * <p>运行 {@link i2f.design.pattern.structural.bridge.Test#main(String[])} 查看完整演示。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see i2f.design.pattern.structural.bridge.device.Device
 * @see i2f.design.pattern.structural.bridge.remote.RemoteControl
 * @see i2f.design.pattern.structural.bridge.Test
 */
package i2f.design.pattern.structural.bridge;
