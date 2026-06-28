package i2f.design.pattern.structural.adapter.player;

/**
 * 适配器模式 —— 媒体播放器（Target：MediaPlayer）
 *
 * <p><b>角色：</b>目标接口（Target Interface）</p>
 *
 * <p><b>模式说明：</b>定义客户端期望使用的标准接口。
 * 适配器模式的核心目标就是让原本不兼容的接口（如高级媒体播放器）
 * 能够通过适配器转换，符合此接口的规范，从而被客户端直接调用。</p>
 *
 * <p><b>命名立意：</b>"MediaPlayer"是客户端直接使用的标准播放接口，
 * 支持播放常见音频格式。当需要播放视频或高级格式时，
 * 内部会通过适配器自动转换调用，对客户端透明。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see i2f.design.pattern.structural.adapter.adapter.MediaAdapter
 * @see i2f.design.pattern.structural.adapter.player.impl.AudioPlayer
 */
public interface MediaPlayer {

    /**
     * 播放指定类型的媒体文件。
     *
     * @param audioType 媒体类型（如 "mp3", "vlc", "mp4" 等）
     * @param fileName  文件名
     */
    void play(String audioType, String fileName);
}
