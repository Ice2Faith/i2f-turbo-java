package i2f.design.pattern.structural.adapter.advanced;

/**
 * 适配器模式 —— 高级媒体播放器（Adaptee Interface：AdvancedMediaPlayer）
 *
 * <p><b>角色：</b>适配者接口（Adaptee Interface）</p>
 *
 * <p><b>模式说明：</b>定义已有的、但接口与目标不兼容的高级播放器。
 * 这些播放器功能强大，但调用方式与客户端期望的 {@code MediaPlayer} 接口不同。
 * 适配器模式的作用就是将这些高级播放器"包装"成标准接口。</p>
 *
 * <p><b>命名立意：</b>代表第三方或遗留系统中的高级播放组件，
 * 它们各自有独立的播放方法（如 {@code playVlc}、{@code playMp4}），
 * 不能直接被客户端统一调用，需要通过适配器进行接口转换。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see i2f.design.pattern.structural.adapter.advanced.impl.VlcPlayer
 * @see i2f.design.pattern.structural.adapter.advanced.impl.Mp4Player
 */
public interface AdvancedMediaPlayer {

    /**
     * 播放 VLC 格式媒体文件。
     *
     * @param fileName 文件名
     */
    void playVlc(String fileName);

    /**
     * 播放 MP4 格式媒体文件。
     *
     * @param fileName 文件名
     */
    void playMp4(String fileName);
}
