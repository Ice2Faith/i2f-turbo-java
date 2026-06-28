package i2f.design.pattern.structural.adapter.advanced.impl;

import i2f.design.pattern.structural.adapter.advanced.AdvancedMediaPlayer;
import lombok.Data;

/**
 * 适配器模式 —— VLC 播放器（Concrete Adaptee：VlcPlayer）
 *
 * <p><b>角色：</b>具体适配者（Concrete Adaptee）</p>
 *
 * <p><b>模式说明：</b>VlcPlayer 是已有的高级播放器实现，
 * 它拥有自己的接口规范（{@code playVlc} 方法），
 * 与客户端期望的 {@code MediaPlayer} 接口不兼容。
 * 需要通过适配器将其转换为标准接口。</p>
 *
 * <p><b>命名立意：</b>模拟第三方 VLC 媒体库的播放器组件，
 * 功能专一且强大，但调用方式独立，
 * 适配器将负责"翻译"客户端的统一调用请求。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see AdvancedMediaPlayer
 */
@Data
public class VlcPlayer implements AdvancedMediaPlayer {

    /**
     * 播放器版本标识。
     */
    private String version = "VLC-3.0.20";

    @Override
    public void playVlc(String fileName) {
        System.out.println("  [VlcPlayer v" + version + "] 正在播放 VLC 视频: " + fileName);
    }

    @Override
    public void playMp4(String fileName) {
        // VLC 播放器不支持 MP4（模拟接口不兼容的场景）
        System.out.println("  [VlcPlayer v" + version + "] 不支持 MP4 格式");
    }
}
