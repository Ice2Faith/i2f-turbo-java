package i2f.design.pattern.structural.proxy;

import i2f.design.pattern.structural.proxy.player.IVideoPlayer;
import i2f.design.pattern.structural.proxy.player.impl.VideoPlayer;

/**
 * 代理模式 —— 调用演示
 *
 * <p>演示代理模式的核心机制：客户端通过代理（{@link ProxyVideoPlayer}）间接访问真实主题（{@link VideoPlayer}），
 * 代理在访问前后添加权限控制、日志记录等额外逻辑，实现访问控制与业务逻辑的解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 代理模式核心演示 ====================
        System.out.println("====== 代理模式（Proxy）演示 ======");
        System.out.println("场景：视频播放平台通过代理控制器控制用户访问权限");
        System.out.println("      VIP用户可观看所有视频，普通用户只能观看免费视频\n");

        // ==================== 2. 创建真实播放器 ====================
        System.out.println("────── 步骤1：创建真实视频播放器 ──────");
        VideoPlayer realPlayer = new VideoPlayer("IcePlayer", "2.0");
        System.out.println("真实播放器：" + realPlayer);
        System.out.println();

        // ==================== 3. VIP用户通过代理播放 ====================
        System.out.println("────── 步骤2：VIP用户通过代理播放付费视频 ──────");
        IVideoPlayer vipProxy = new ProxyVideoPlayer(realPlayer, "VIP", "VIP代理");
        System.out.println("VIP代理：" + vipProxy);
        System.out.println("播放结果：" + vipProxy.play("vip-001-复仇者联盟"));
        System.out.println();

        // ==================== 4. 普通用户播放免费视频 ====================
        System.out.println("────── 步骤3：普通用户通过代理播放免费视频 ──────");
        IVideoPlayer normalProxy = new ProxyVideoPlayer(realPlayer, "NORMAL", "普通代理");
        System.out.println("普通代理：" + normalProxy);
        System.out.println("播放结果：" + normalProxy.play("free-001-猫咪日常"));
        System.out.println();

        // ==================== 5. 普通用户尝试播放付费视频 ====================
        System.out.println("────── 步骤4：普通用户尝试播放付费视频（应被拒绝） ──────");
        System.out.println("播放结果：" + normalProxy.play("vip-002-阿凡达2"));
        System.out.println();

        // ==================== 6. 多次播放验证访问计数 ====================
        System.out.println("────── 步骤5：多次播放验证代理的访问日志功能 ──────");
        System.out.println("第1次播放：");
        System.out.println(vipProxy.play("free-002-旅行vlog"));
        System.out.println();
        System.out.println("第2次播放：");
        System.out.println(vipProxy.play("vip-003-流浪地球"));
        System.out.println();
        System.out.println("第3次播放：");
        System.out.println(vipProxy.play("free-003-美食制作"));
        System.out.println();

        // ==================== 7. 暂停操作验证 ====================
        System.out.println("────── 步骤6：测试暂停操作 ──────");
        System.out.println(vipProxy.pause());
        System.out.println();

        // ==================== 8. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道是代理还是真实对象 ======");
        System.out.println("通过统一接口（IVideoPlayer）调度不同类型的播放器：\n");

        IVideoPlayer[] players = {
                realPlayer,                          // 真实播放器
                new ProxyVideoPlayer(realPlayer, "VIP", "VIP代理"),
                new ProxyVideoPlayer(realPlayer, "NORMAL", "普通代理")
        };
        String[] videoIds = {"free-004-纪录片", "vip-004-黑客帝国", "free-005-动画片"};

        for (int i = 0; i < players.length; i++) {
            System.out.println("播放任务 " + (i + 1) + "：");
            System.out.println("播放器类型：" + players[i].getClass().getSimpleName());
            System.out.println("播放结果：" + players[i].play(videoIds[i]));
            System.out.println();
        }

        // ==================== 9. 代理类型说明 ====================
        System.out.println("====== 代理模式的常见类型 ======");
        System.out.println("1. 保护代理（Protection Proxy）：控制访问权限（如本例）");
        System.out.println("2. 虚代理（Virtual Proxy）：延迟加载重量级对象（如图片懒加载）");
        System.out.println("3. 远程代理（Remote Proxy）：代表远程对象（如RPC调用）");
        System.out.println("4. 智能引用代理（Smart Reference Proxy）：访问时执行额外操作（如计数、日志）");
        System.out.println();

        // ==================== 10. 模式优势总结 ====================
        System.out.println("====== 代理模式优势总结 ======");
        System.out.println("1. 开闭原则：可以在不修改真实主题的情况下增加新的控制逻辑");
        System.out.println("2. 单一职责：真实主题专注业务逻辑，代理专注访问控制");
        System.out.println("3. 客户端透明：客户端通过统一接口访问，无需知道是代理还是真实对象");
        System.out.println("4. 灵活扩展：可随时替换代理实现不同的控制策略（权限、缓存、日志等）");
        System.out.println("5. 保护真实对象：避免客户端直接访问真实对象，增强安全性");
    }
}
