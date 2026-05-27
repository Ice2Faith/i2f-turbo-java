package i2f.design.pattern.structural.proxy.player.impl;

import i2f.design.pattern.structural.proxy.ProxyVideoPlayer;
import i2f.design.pattern.structural.proxy.player.IVideoPlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 代理模式 —— 真实视频播放器（RealSubject：VideoPlayer）
 *
 * <p><b>角色：</b>真实主题（RealSubject）</p>
 *
 * <p><b>模式说明：</b>定义了代理所代表的真实对象，包含实际的业务逻辑。
 * 在代理模式中，客户端并不直接访问此对象，而是通过代理间接访问。
 * 代理可以在访问前后添加额外的控制逻辑（如权限检查、日志记录、缓存等）。</p>
 *
 * <p><b>命名立意：</b>这是真正的视频播放器，负责实际的播放操作。
 * 但在实际应用中，我们不希望用户直接访问它，而是通过代理来控制访问权限——
 * 比如检查用户是否是VIP、视频是否需要付费等。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see IVideoPlayer
 * @see ProxyVideoPlayer
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class VideoPlayer implements IVideoPlayer {

    /**
     * 播放器名称。
     */
    private String playerName;

    /**
     * 播放器版本。
     */
    private String version;

    public VideoPlayer(String playerName, String version) {
        this.playerName = playerName;
        this.version = version;
    }

    @Override
    public String play(String videoId) {
        // 模拟视频播放的实际业务逻辑
        return String.format("[真实播放器·%s v%s] 正在播放视频【%s】，画质：1080P，音效：环绕立体声",
                playerName, version, videoId);
    }

    @Override
    public String pause() {
        return String.format("[真实播放器·%s v%s] 已暂停播放", playerName, version);
    }

    @Override
    public String toString() {
        return String.format("VideoPlayer{playerName='%s', version='%s'}", playerName, version);
    }
}
