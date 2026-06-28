/**
 * 代理模式（Proxy）
 *
 * <p><b>定义：</b>为其他对象提供一种代理以控制对这个对象的访问。</p>
 * <p><b>分类：</b>结构型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>需要对目标对象进行访问控制（远程代理、保护代理）。</li>
 *   <li>需要在访问前后增加额外操作（日志、缓存、事务）。</li>
 *   <li>需要延迟加载（虚代理）。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"视频播放平台"为场景——通过代理控制器控制用户访问权限，
 * VIP用户可以观看所有视频，普通用户只能观看免费视频，实现访问控制与业务逻辑的解耦。</p>
 *
 * <p><b>模式结构图：</b></p>
 * <pre>
 *  Client（客户端）
 *       │
 *       ▼ 使用
 *  ┌─────────────────┐
 *  │  IVideoPlayer   │ ◄── Subject（抽象主题）
 *  │  ─────────────  │
 *  │  + play()       │
 *  │  + pause()      │
 *  └────────┬────────┘
 *           │ 实现
 *    ┌──────┴──────┐
 *    ▼             ▼
 *  ┌──────────┐  ┌──────────────────┐
 *  │VideoPlayer│  │ProxyVideoPlayer │
 *  │──────────│  │──────────────────│
 *  │真实播放器 │  │代理播放器        │
 *  │(RealSubj)│  │(Proxy)           │
 *  │          │  │- realPlayer      │
 *  │          │  │- userLevel       │
 *  │          │  │- accessCount     │
 *  └──────────┘  │+ play() ← 权限检查│
 *                │+ pause()         │
 *                └──────────────────┘
 * </pre>
 *
 * <p><b>角色说明：</b></p>
 * <ul>
 *   <li>{@link IVideoPlayer} —— 抽象主题（Subject）：定义真实主题和代理的共同接口</li>
 *   <li>{@link VideoPlayer} —— 真实主题（RealSubject）：实际的视频播放器，负责播放业务</li>
 *   <li>{@link ProxyVideoPlayer} —— 代理（Proxy）：持有真实播放器引用，控制访问权限</li>
 *   <li>{@link Test} —— 调用演示</li>
 * </ul>
 *
 * <p><b>代理类型：</b></p>
 * <ul>
 *   <li>保护代理（Protection Proxy）：控制访问权限（如本例）</li>
 *   <li>虚代理（Virtual Proxy）：延迟加载重量级对象</li>
 *   <li>远程代理（Remote Proxy）：代表远程对象</li>
 *   <li>智能引用代理（Smart Reference Proxy）：访问时执行额外操作</li>
 * </ul>
 *
 * @see IVideoPlayer
 * @see VideoPlayer
 * @see ProxyVideoPlayer
 * @see Test
 */
package i2f.design.pattern.structural.proxy;
