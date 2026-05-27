package i2f.design.pattern.structural.proxy;

import i2f.design.pattern.structural.proxy.player.IVideoPlayer;
import i2f.design.pattern.structural.proxy.player.impl.VideoPlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 代理模式 —— 代理视频播放器（Proxy：ProxyVideoPlayer）
 *
 * <p><b>角色：</b>代理（Proxy）</p>
 *
 * <p><b>模式说明：</b>持有真实主题的引用，在访问真实主题前后添加额外的控制逻辑。
 * 本例实现的是<b>保护代理（Protection Proxy）</b>——控制对真实对象的访问权限。
 * 代理与真实主题实现相同接口，客户端无需知道访问的是代理还是真实对象。</p>
 *
 * <p><b>代理类型说明：</b></p>
 * <ul>
 *   <li><b>保护代理：</b>控制访问权限（如本例，检查用户等级）</li>
 *   <li><b>虚代理：</b>延迟加载重量级对象（如图片懒加载）</li>
 *   <li><b>远程代理：</b>代表远程对象（如RPC调用）</li>
 *   <li><b>智能引用代理：</b>访问时执行额外操作（如计数、日志）</li>
 * </ul>
 *
 * <p><b>命名立意：</b>作为视频播放器的代理，它在实际播放前检查用户权限——
 * VIP用户可以观看所有视频，普通用户只能观看免费视频。
 * 这样既实现了权限控制，又不需要修改真实播放器的代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see IVideoPlayer
 * @see VideoPlayer
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class ProxyVideoPlayer implements IVideoPlayer {

    /**
     * 持有的真实视频播放器引用。
     */
    private VideoPlayer realPlayer;

    /**
     * 当前用户等级（VIP / NORMAL）。
     */
    private String userLevel;

    /**
     * 播放器名称（代理自身属性）。
     */
    private String proxyName;

    /**
     * 访问日志计数器。
     */
    private int accessCount = 0;

    public ProxyVideoPlayer(VideoPlayer realPlayer, String userLevel, String proxyName) {
        this.realPlayer = realPlayer;
        this.userLevel = userLevel;
        this.proxyName = proxyName;
    }

    @Override
    public String play(String videoId) {
        accessCount++;

        // 1. 前置处理：权限检查
        String permissionCheck = checkPermission(videoId);
        if (!"ALLOW".equals(permissionCheck)) {
            return String.format("[代理播放器·%s] 拒绝访问：用户等级【%s】无权播放视频【%s】",
                    proxyName, userLevel, videoId);
        }

        System.out.println(String.format("  [代理播放器·%s] 权限验证通过，允许播放", proxyName));

        // 2. 调用真实主题
        String result = realPlayer.play(videoId);

        // 3. 后置处理：记录日志
        System.out.println(String.format("  [代理播放器·%s] 访问日志：累计播放 %d 次", proxyName, accessCount));

        return result;
    }

    @Override
    public String pause() {
        accessCount++;
        System.out.println(String.format("  [代理播放器·%s] 记录暂停操作", proxyName));
        return realPlayer.pause();
    }

    /**
     * 权限检查方法。
     *
     * <p>模拟业务逻辑：VIP用户可以观看所有视频，
     * 普通用户只能观看免费视频（videoId以"free-"开头）。</p>
     *
     * @param videoId 视频ID
     * @return "ALLOW" 表示允许访问，其他值表示拒绝原因
     */
    private String checkPermission(String videoId) {
        // VIP用户拥有全部权限
        if ("VIP".equalsIgnoreCase(userLevel)) {
            return "ALLOW";
        }

        // 普通用户只能观看免费视频
        if (videoId.startsWith("free-")) {
            return "ALLOW";
        }

        return "DENIED: 需要VIP会员";
    }

    @Override
    public String toString() {
        return String.format("ProxyVideoPlayer{proxyName='%s', userLevel='%s', accessCount=%d, realPlayer=%s}",
                proxyName, userLevel, accessCount, realPlayer);
    }
}
