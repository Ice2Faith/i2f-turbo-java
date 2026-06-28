package i2f.design.pattern.structural.adapter.adapter;

import i2f.design.pattern.structural.adapter.advanced.AdvancedMediaPlayer;
import i2f.design.pattern.structural.adapter.advanced.impl.Mp4Player;
import i2f.design.pattern.structural.adapter.advanced.impl.VlcPlayer;
import i2f.design.pattern.structural.adapter.player.MediaPlayer;
import lombok.Data;

/**
 * 适配器模式 —— 媒体适配器（Adapter：MediaAdapter）
 *
 * <p><b>角色：</b>适配器（Adapter）</p>
 *
 * <p><b>模式说明：</b>适配器是连接"目标接口"与"适配者"的桥梁。
 * 它实现 {@link MediaPlayer} 接口，同时内部持有 {@link AdvancedMediaPlayer} 实例，
 * 将客户端的统一播放请求转换为对应高级播放器的特定方法调用。</p>
 *
 * <p><b>命名立意：</b>MediaAdapter 如同一个"翻译官"——
 * 客户端说"播放 MP4"，适配器听懂后去找 Mp4Player 执行；
 * 客户端说"播放 VLC"，适配器则委托给 VlcPlayer。
 * 客户端无需知道背后有多个不同的播放器实现。</p>
 *
 * <p><b>适配方式：</b>对象适配器（通过组合持有 Adaptee 实例）</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see MediaPlayer
 * @see AdvancedMediaPlayer
 */
@Data
public class MediaAdapter implements MediaPlayer {

    /**
     * 持有高级播放器实例（适配者）。
     */
    private AdvancedMediaPlayer advancedMusicPlayer;

    /**
     * 根据媒体类型创建对应的适配器。
     *
     * @param audioType 媒体类型（"vlc" 或 "mp4"）
     */
    public MediaAdapter(String audioType) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            this.advancedMusicPlayer = new VlcPlayer();
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            this.advancedMusicPlayer = new Mp4Player();
        }
    }

    /**
     * 将标准播放请求转换为高级播放器的特定方法调用。
     *
     * <p>这是适配器的核心逻辑：
     * 根据类型判断应该调用 {@code playVlc} 还是 {@code playMp4}，
     * 实现接口的"翻译"功能。</p>
     *
     * @param audioType 媒体类型
     * @param fileName  文件名
     */
    @Override
    public void play(String audioType, String fileName) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            advancedMusicPlayer.playVlc(fileName);
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            advancedMusicPlayer.playMp4(fileName);
        } else {
            System.out.println("  [MediaAdapter] 错误：未知的媒体格式 - " + audioType);
        }
    }
}
