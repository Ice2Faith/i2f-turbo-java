# Future/Promise 模式

> Future/Promise 模式是并发型设计模式，用于将异步任务的"发起"与"结果获取"解耦。通过一个占位对象（Future）代表异步计算的结果，调用方可以在未来的某个时间点获取该结果，而无需阻塞等待任务完成。

---

## 一、核心逻辑

### 1.1 异步结果解耦

Future/Promise 模式的核心思想是将**异步任务的提交**与**结果的获取**分离：

- **Promise（写入端）**：由任务执行者（生产者）持有，负责在任务完成后"承诺"写入结果
- **Future（读取端）**：由任务调用者（消费者）持有，用于在未来某个时刻读取结果
- **共享状态**：Promise 和 Future 指向同一个异步计算结果，Promise 负责写入，Future 负责读取

### 1.2 状态机流转

异步任务的生命周期通过状态机管理：

```
PENDING（进行中）
    ↓
├─→ DONE（完成）    → get() 返回结果
├─→ FAILED（失败）   → get() 抛出 ExecutionException
└─→ CANCELLED（已取消） → get() 抛出 CancellationException
```

### 1.3 线程同步机制

- 使用 `synchronized` + `wait/notifyAll` 实现线程间通信
- `get()` 方法在 `PENDING` 状态下阻塞等待（`wait()`）
- `set()` / `setException()` / `cancel()` 完成后唤醒所有等待线程（`notifyAll()`）
- 结果只能写入一次，重复写入抛出 `IllegalStateException`

### 1.4 模式优势

1. **非阻塞编程**：主线程无需阻塞等待，可继续处理其他任务
2. **灵活的结果获取**：支持阻塞等待、超时等待、轮询检查、取消任务
3. **职责分离**：写入端（Promise）与读取端（Future）职责清晰
4. **提升系统吞吐量**：适用于高并发场景，避免线程资源浪费

---

## 二、核心组成

### 2.1 五大角色映射

| 角色 | 接口/类 | 职责 | 持有者 |
|------|---------|------|--------|
| **Promise（写入端）** | `Promise<T>` | 定义写入结果的接口 | 任务执行者（厨房） |
| **Future（读取端）** | `Future<T>` | 定义读取结果的接口 | 任务调用者（顾客） |
| **统一实现** | `FutureTask<T>` | 同时实现 Promise 和 Future | 订单对象 |
| **任务执行者** | `Kitchen` | 异步执行任务并写入结果 | 厨房（生产者） |
| **任务调用者** | `Customer` | 提交任务并在未来获取结果 | 顾客（消费者） |
| **结果对象** | `Food` | 异步任务的实际产出 | 菜品 |
| **异常体系** | `ExecutionException`<br>`CancellationException`<br>`TimeoutException` | 封装不同失败场景 | - |

### 2.2 结构图

```
Customer（顾客）                        Kitchen（厨房）
    │                                      │
    │  1. order(food, kitchen)             │
    ├─────────────────────────────────────>│
    │                                      │
    │  2. 创建 FutureTask                  │
    │     (同时具备 Promise + Future 能力)  │
    │                                      │
    │  3. 保留 Future 端                   │ 4. 传递 Promise 端
    │     ←───────────────────────────────>│
    │                                      │
    │  5. future.get() (阻塞等待)           │ 6. cookAsync() (新线程)
    │     [阻塞中...]                       │    制作菜品
    │                                      │    ↓
    │                                      │ 7. promise.set(food)
    │                                      │    [唤醒等待线程]
    │                                      │
    │  8. 返回 Food 结果                   │
    │     ←───────────────────────────────>│
```

### 2.3 接口定义

#### Promise 接口（写入端）

```java
public interface Promise<T> {
    void set(T result);              // 设置成功结果
    void setException(Throwable e);  // 设置异常结果
    boolean cancel();                // 取消任务
}
```

#### Future 接口（读取端）

```java
public interface Future<T> {
    T get() throws ...;              // 阻塞获取结果
    T get(long timeoutMillis) throws ...;  // 超时获取结果
    boolean isDone();                // 检查是否完成
    boolean isCancelled();           // 检查是否取消
}
```

---

## 三、案例设计解析

### 3.1 业务场景：餐厅点餐系统

本包以**餐厅点餐**为场景，直观展示 Future/Promise 模式的应用：

- **顾客（Customer）**：任务调用者，点单后获得取餐凭据（Future）
- **厨房（Kitchen）**：任务执行者，异步制作菜品，完成后更新订单状态（Promise）
- **菜品（Food）**：异步任务的结果对象
- **订单（FutureTask）**：取餐凭据的具体实现，同时具备 Promise 和 Future 能力

### 3.2 核心流程演示

#### 场景 1：阻塞等待取餐

```java
// 顾客点单，立即获得 Future（取餐凭据）
Food food = new Food("宫保鸡丁", 38.0, 2);
Future<Food> future = customer.order(food, kitchen);

// 顾客阻塞等待取餐（等待 2 秒）
Food result = customer.waitForFood(future);
```

**执行流程：**
1. 顾客调用 `order()` → 创建 `FutureTask`（订单）
2. 将 `Promise` 端交给厨房 → 厨房在新线程中异步制作
3. 顾客保留 `Future` 端 → 调用 `get()` 阻塞等待
4. 厨房制作完成 → 调用 `promise.set(food)` 写入结果
5. 唤醒等待线程 → `future.get()` 返回菜品

#### 场景 2：超时等待

```java
// 最多等待 1.5 秒，但菜品需要 3 秒制作 → 触发超时
Food result = customer.waitForFoodWithTimeout(future, 1500);
```

**设计要点：**
- 使用 `wait(remainingTime)` 实现带超时的阻塞
- 超时后抛出 `TimeoutException`，顾客可选择离开

#### 场景 3：轮询检查

```java
// 不阻塞等待，每 500ms 检查一次订单状态
customer.pollOrderStatus(future, 500);
```

**设计要点：**
- 使用 `isDone()` 非阻塞检查任务状态
- 适用于顾客想做其他事情（如刷手机）的场景

#### 场景 4：取消订单

```java
// 等待 1 秒后取消订单
boolean cancelled = customer.cancelOrder(future);
```

**设计要点：**
- 只有 `PENDING` 状态才能取消
- 取消后调用 `notifyAll()` 唤醒等待线程
- 后续 `get()` 抛出 `CancellationException`

#### 场景 5：多订单并发

```java
// 多个顾客同时点单，厨房并发制作
for (int i = 0; i < customers.length; i++) {
    futures[i] = customers[i].order(foods[i], kitchen);
}

// 各自独立等待取餐
for (int i = 0; i < customers.length; i++) {
    Food result = customers[i].waitForFood(futures[i]);
}
```

**设计要点：**
- 每个订单独立的状态机（独立的 `FutureTask` 实例）
- 厨房使用新线程处理每个订单，互不阻塞

#### 场景 6：重复获取结果

```java
// 同一个 Future 可多次调用 get()，结果保持不变
Food result1 = customer.waitForFood(future);
Food result2 = customer.waitForFood(future);
// result1 == result2 → true
```

**设计要点：**
- 任务完成后状态不再改变（`DONE` / `FAILED` / `CANCELLED`）
- 后续 `get()` 立即返回，无需再次等待

### 3.3 线程安全保证

`FutureTask` 通过以下机制确保线程安全：

1. **状态保护**：所有状态转换通过 `synchronized` 块保护
2. **单次写入**：结果只能设置一次，重复设置抛出异常
3. **唤醒机制**：`set()` / `cancel()` 后调用 `notifyAll()` 唤醒等待线程
4. **volatile 修饰**：`state` 字段使用 `volatile` 保证可见性

### 3.4 异常处理设计

| 异常类型 | 触发场景 | 处理方式 |
|---------|---------|---------|
| `ExecutionException` | 任务执行过程中发生异常 | `promise.setException(e)` → `get()` 抛出 |
| `CancellationException` | 任务被取消 | `promise.cancel()` → `get()` 抛出 |
| `TimeoutException` | 等待超时 | `get(timeout)` 超时后抛出 |
| `InterruptedException` | 线程被中断 | 恢复中断标志 `Thread.currentThread().interrupt()` |

---

## 四、典型适用场景

### 4.1 异步任务执行

**场景描述：** 需要异步执行耗时任务，不阻塞主线程。

**典型案例：**
- Spring `@Async` 注解方法返回 `CompletableFuture`
- 异步发送邮件、短信、推送通知
- 异步生成报表、导出数据

**示例：**
```java
@Async
public CompletableFuture<UserReport> generateReport(Long userId) {
    // 耗时操作：查询数据、生成报表
    UserReport report = buildReport(userId);
    return CompletableFuture.completedFuture(report);
}
```

### 4.2 非阻塞网络请求

**场景描述：** 发起 HTTP/RPC 请求后无需阻塞等待，可在未来获取响应。

**典型案例：**
- JDK `HttpClient.sendAsync()` 返回 `CompletableFuture<HttpResponse>`
- Netty `ChannelFuture` 异步 IO 操作
- gRPC 异步调用返回 `ListenableFuture`

**示例：**
```java
CompletableFuture<String> response = httpClient.sendAsync(request);
// 继续处理其他任务...
String body = response.join();  // 在需要时获取结果
```

### 4.3 并发数据聚合

**场景描述：** 多个独立任务并行执行，最后汇总结果。

**典型案例：**
- 并行查询多个数据源（数据库、缓存、外部 API）后合并
- 批量处理任务并行执行，等待全部完成后汇总
- 分页数据并发加载

**示例：**
```java
CompletableFuture<User> userFuture = userService.getUserAsync(userId);
CompletableFuture<List<Order>> ordersFuture = orderService.getOrdersAsync(userId);
CompletableFuture<VipStatus> vipFuture = vipService.checkVipAsync(userId);

// 等待所有任务完成后合并结果
CompletableFuture.allOf(userFuture, ordersFuture, vipFuture).join();

UserProfile profile = buildProfile(
    userFuture.get(), 
    ordersFuture.get(), 
    vipFuture.get()
);
```

### 4.4 响应式编程

**场景描述：** 基于事件驱动的异步数据流处理。

**典型案例：**
- Spring WebFlux `Mono<T>`（单值异步）、`Flux<T>`（多值异步流）
- Reactor 响应式流
- RxJava 异步事件流

**示例：**
```java
Mono<User> userMono = userRepository.findById(userId);
Mono<List<Order>> ordersMono = orderRepository.findByUserId(userId);

// 响应式组合
Mono<UserProfile> profileMono = Mono.zip(userMono, ordersMono)
    .map(tuple -> buildProfile(tuple.getT1(), tuple.getT2()));
```

### 4.5 超时控制与降级

**场景描述：** 为异步任务设置超时，超时后执行降级逻辑。

**典型案例：**
- 微服务调用超时降级（返回缓存数据或默认值）
- 第三方 API 调用超时重试
- 慢查询自动取消

**示例：**
```java
try {
    Result result = future.get(3000, TimeUnit.MILLISECONDS);
    return result;
} catch (TimeoutException e) {
    // 超时降级：返回缓存数据
    return cacheService.getCachedResult();
}
```

### 4.6 任务编排与链式调用

**场景描述：** 多个异步任务按依赖关系顺序执行。

**典型案例：**
- JDK `CompletableFuture` 链式 API（`thenApply` / `thenCompose` / `thenCombine`）
- 异步任务依赖编排（任务 B 依赖任务 A 的结果）
- 流水线处理（数据过滤 → 转换 → 聚合）

**示例：**
```java
CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> transform(data))
    .thenApply(transformed -> validate(transformed))
    .thenAccept(valid -> save(valid));
```

### 4.7 回调与事件通知

**场景描述：** 任务完成后自动触发回调函数。

**典型案例：**
- Guava `ListenableFuture` 添加回调监听器
- 文件上传完成后通知用户
- 异步计算完成后更新 UI

**示例：**
```java
future.whenComplete((result, exception) -> {
    if (exception != null) {
        log.error("任务失败", exception);
    } else {
        notifyUser("任务完成: " + result);
    }
});
```

---

## 五、模式总结

### 5.1 核心优势

✅ **解耦任务提交与结果获取**：调用者无需等待任务完成即可继续其他操作  
✅ **提升系统吞吐量**：主线程无需阻塞，可处理更多请求  
✅ **灵活的结果获取方式**：支持阻塞等待、超时等待、轮询检查、取消任务  
✅ **职责分离**：Promise（写入端）与 Future（读取端）职责清晰  
✅ **适用于高并发场景**：非阻塞式并发编程，避免线程资源浪费  

### 5.2 相关模式

- **生产者-消费者模式**：Future/Promise 可视为特殊的生产者-消费者模式，但更强调结果的"未来性"
- **观察者模式**：任务完成后可通过回调通知订阅者（如 `CompletableFuture.whenComplete()`）
- **命令模式**：异步任务可封装为命令对象，提交到线程池执行

### 5.3 JDK 原生支持

| 类 | 说明 |
|---|------|
| `java.util.concurrent.Future` | JDK 基础 Future 接口 |
| `java.util.concurrent.FutureTask` | JDK 默认实现 |
| `java.util.concurrent.CompletableFuture` | 支持链式编排、组合、回调 |
| `java.util.concurrent.ScheduledFuture` | 定时任务的 Future |

### 5.4 框架生态

| 框架 | 实现 |
|------|------|
| **Spring** | `@Async` 返回 `CompletableFuture`、`AsyncResult` |
| **Spring WebFlux** | `Mono<T>`、`Flux<T>` 响应式 Future |
| **Netty** | `ChannelFuture`、`Promise` |
| **Guava** | `ListenableFuture` |
| **Reactor** | `Mono`、`Flux` |
| **RxJava** | `Observable`、`Single`、`Maybe` |

---

> **设计哲学**：Future/Promise 模式通过"占位对象"将异步任务的发起与结果获取解耦，让调用方能够在未来某个时刻获取结果，而非立即阻塞等待。这种设计显著提升了系统的并发能力和响应性，是现代异步编程的基石。
