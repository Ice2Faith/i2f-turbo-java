/**
 * 策略模式（Strategy）
 *
 * <p>定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换，算法可独立于使用它的客户而变化。</p>
 *
 * <p><b>模式分类：</b>行为型模式</p>
 *
 * <p><b>核心思想：</b>将可变的行为（算法）抽象为策略接口，每个具体策略实现一种算法，
 * 上下文（Context）持有策略引用，运行时可动态切换策略，避免大量的 if-else 或 switch 分支。</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>系统有许多类，它们之间的区别仅在于行为不同</li>
 *   <li>需要在运行时动态选择算法或行为</li>
 *   <li>避免代码中出现大量的条件分支判断</li>
 *   <li>算法需要独立于使用它的客户端演化</li>
 * </ul>
 *
 * <p><b>典型案例：</b></p>
 * <ul>
 *   <li>JDK：{@code java.util.Comparator}（可替换的排序策略）</li>
 *   <li>JDK：{@code ThreadPoolExecutor} 的拒绝策略（{@code RejectedExecutionHandler}）</li>
 *   <li>Spring Security：{@code PasswordEncoder}（BCrypt/Argon2 等可替换加密策略）</li>
 *   <li>Spring AOP：{@code AopProxy} 的 JDK 动态代理与 CGLIB 两种策略</li>
 * </ul>
 *
 * <p><b>本包示例：</b>以"电商支付方式"为场景——订单结算时可选择不同的支付策略（支付宝、微信、信用卡），
 * 各支付方式独立封装，客户端（订单上下文）运行时动态选择，符合开闭原则。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
package i2f.design.pattern.behavioral.strategy;
