package i2f.design.pattern.structural.proxy.player;

import i2f.design.pattern.structural.proxy.ProxyVideoPlayer;
import i2f.design.pattern.structural.proxy.player.impl.VideoPlayer;

/**
 * 代理模式 —— 视频播放器接口（Subject：IVideoPlayer）
 *
 * <p><b>角色：</b>抽象主题（Subject）</p>
 *
 * <p><b>模式说明：</b>定义真实主题和代理的共同接口，客户端通过此接口访问真实主题。
 * 代理模式的核心在于：客户端面对的是这个抽象接口，
 * 可以在不修改客户端代码的情况下，用代理替换真实主题。</p>
 *
 * <p><b>命名立意：</b>以"视频播放平台"为场景——用户通过统一的播放器接口观看视频，
 * 但普通用户和VIP用户的访问权限不同，代理控制器控制谁能观看哪些视频。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  Subject（抽象主题）        RealSubject（真实主题）        Proxy（代理）
 *  ──────────────────────   ───────────────────────────   ───────────────────────────
 *  IVideoPlayer              VideoPlayer                   ProxyVideoPlayer
 *  └─ play(videoId)          └─ play(videoId)              └─ play(videoId)
 *  └─ pause()                └─ pause()                    └─ pause()
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see VideoPlayer
 * @see ProxyVideoPlayer
 */
public interface IVideoPlayer {

    /**
     * 播放指定视频。
     *
     * <p>真实主题负责实际播放，代理负责控制访问权限。</p>
     *
     * @param videoId 视频ID
     * @return 播放结果描述
     */
    String play(String videoId);

    /**
     * 暂停播放。
     *
     * @return 暂停结果描述
     */
    String pause();
}
