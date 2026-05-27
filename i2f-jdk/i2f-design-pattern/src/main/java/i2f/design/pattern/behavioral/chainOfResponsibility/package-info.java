/**
 * 责任链模式（Chain of Responsibility）
 * <p>
 * 使多个对象都有机会处理请求，将这些对象连成一条链，并沿着链传递请求，直到有一个对象处理它为止。
 * 分类：行为型模式
 * </p>
 *
 * <h3>模式说明</h3>
 * <p>责任链模式通过将请求的发送者和接收者解耦，让多个处理者对象都有机会处理该请求。
 * 这些处理者对象形成一条链，请求沿着链传递，直到有一个处理者能够处理它为止。</p>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>多个对象可以处理同一请求，但具体由哪个对象处理在运行时决定</li>
 *   <li>希望在不明确指定接收者的情况下，向多个对象提交请求</li>
 *   <li>处理流程可动态组合和调整</li>
 * </ul>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li>JDK：{@code java.util.logging.Logger}（日志按层级向父 Handler 传递）</li>
 *   <li>Servlet：{@code Filter} 与 {@code FilterChain}</li>
 *   <li>Spring：{@code HandlerInterceptor} 拦截器链；{@code BeanPostProcessor} 链；Spring Security {@code SecurityFilterChain} 过滤器链</li>
 *   <li>Spring Cloud Gateway：{@code GlobalFilter} 与 {@code GatewayFilter} 链</li>
 *   <li>Netty：{@code ChannelPipeline} 与 {@code ChannelHandler}</li>
 *   <li>MyBatis：{@code Interceptor} 插件链</li>
 * </ul>
 *
 * <h3>本包演示结构</h3>
 * <pre>
 *  场景：员工请假审批流程
 *
 *  Handler（抽象处理者）
 *    └─ ApprovalHandler                     ← 定义处理接口和下一个处理者引用
 *       └─ handle(request): boolean         ← 处理请求（抽象方法）
 *       └─ setNext(handler): ApprovalHandler← 设置下一个处理者
 *       └─ handleNext(request): boolean     ← 传递给下一个处理者
 *
 *  ConcreteHandler（具体处理者）
 *    ├─ GroupLeader         → 处理 ≤ 3 天的请假
 *    ├─ DepartmentManager   → 处理 ≤ 7 天的请假
 *    └─ GeneralManager      → 处理 ≤ 30 天的请假
 *
 *  Request（请求对象）
 *    └─ ApprovalRequest                     ← 封装请假申请数据
 *
 *  责任链构建：
 *    groupLeader.setNext(deptManager).setNext(generalManager);
 *
 *  请求处理：
 *    groupLeader.handle(request);  // 请求从链头进入，自动传递到合适的处理者
 * </pre>
 *
 * <h3>模式优势</h3>
 * <ul>
 *   <li>降低耦合度：请求发送者无需知道哪个处理者会处理请求</li>
 *   <li>增强灵活性：可动态调整责任链的顺序和组成</li>
 *   <li>符合开闭原则：新增处理者只需新增类并插入链中</li>
 *   <li>符合单一职责：每个处理者只关注自己权限范围内的请求</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see i2f.design.pattern.behavioral.chainOfResponsibility.Test
 */
package i2f.design.pattern.behavioral.chainOfResponsibility;
