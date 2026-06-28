/**
 * Future/Promise 模式
 * <p>
 * 用一个占位对象代表异步计算的结果，调用方可以在未来的某个时间点获取该结果。
 * 分类：并发型模式
 * </p>
 *
 * <h3>模式说明</h3>
 * <p>Future/Promise 模式将异步任务的"发起"与"结果获取"解耦：</p>
 * <ul>
 *   <li><b>Promise</b>：由任务执行者（生产者）持有，用于在任务完成后"承诺"写入结果</li>
 *   <li><b>Future</b>：由任务调用者（消费者）持有，用于在未来某个时刻读取结果</li>
 *   <li>两者指向同一个异步计算结果，Promise 负责写入，Future 负责读取</li>
 * </ul>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Promise（写入端）                    Future（读取端）
 *  ──────────────────────────────────   ──────────────────────────────────
 *  + set(T result)                      + get(): T
 *  + setException(Throwable e)          + isDone(): boolean
 *  + cancel()                           + isCancelled(): boolean
 *       ↓                                    ↑
 *       └───── 共享异步结果状态 ─────────────┘
 *              (Pending → Done / Cancelled / Failed)
 * </pre>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>需要异步执行任务并在未来获取结果</li>
 *   <li>需要将多个异步操作组合编排（链式调用）</li>
 *   <li>避免阻塞主线程，提升系统吞吐量</li>
 *   <li>非阻塞式并发编程</li>
 * </ul>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li>JDK：{@link java.util.concurrent.Future}、{@link java.util.concurrent.CompletableFuture}</li>
 *   <li>Spring：{@code @Async} 方法返回 {@code CompletableFuture}</li>
 *   <li>Spring WebFlux：{@code Mono<T>}、{@code Flux<T>} —— 响应式 Future</li>
 *   <li>Netty：{@code ChannelFuture}、{@code Promise}</li>
 *   <li>Guava：{@code ListenableFuture}</li>
 * </ul>
 *
 * <h3>本包示例：餐厅点餐系统</h3>
 * <ul>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.task.Promise} —— 取餐凭证（写入端，厨房完成后更新状态）</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.task.Future} —— 取餐凭据（读取端，顾客等待取餐）</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.task.FutureTask} —— 凭证的具体实现</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.Food} —— 菜品（异步任务的结果）</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.Kitchen} —— 厨房（任务执行者，异步制作食物）</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.Customer} —— 顾客（任务调用者，提交订单后等待取餐）</li>
 *   <li>{@link i2f.design.pattern.concurrency.futurePromise.Test} —— 综合测试演示</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see i2f.design.pattern.concurrency.futurePromise.task.Promise
 * @see i2f.design.pattern.concurrency.futurePromise.task.Future
 * @see i2f.design.pattern.concurrency.futurePromise.task.FutureTask
 */
package i2f.design.pattern.concurrency.futurePromise;
