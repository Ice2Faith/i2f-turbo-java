package i2f.design.pattern.structural.adapter.advanced.impl;

import i2f.design.pattern.structural.adapter.advanced.AdvancedMediaPlayer;
import lombok.Data;

/**
 * 适配器模式 —— MP4 播放器（Concrete Adaptee：Mp4Player）
 *
 * <p><b>角色：</b>具体适配者（Concrete Adaptee）</p>
 *
 * <p><b>模式说明：</b>Mp4Player 是另一个已有的播放器实现，
 * 专注于 MP4 格式的播放。它与 {@link VlcPlayer} 接口相似但实现独立，
 * 同样需要通过适配器统一暴露给客户端。</p>
 *
 * <p><b>命名立意：</b>模拟专用的 MP4 硬解播放器，
 * 针对特定格式优化，但不兼容其他格式。
 * 适配器负责根据请求路由到正确的播放器。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see AdvancedMediaPlayer
 */
@Data
public class Mp4Player implements AdvancedMediaPlayer {

    /**
     * 解码器类型。
     */
    private String codec = "H.264/AVC";

    @Override
    public void playVlc(String fileName) {
        // MP4 播放器不支持 VLC（模拟接口不兼容的场景）
        System.out.println("  [Mp4Player codec=" + codec + "] 不支持 VLC 格式");
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("  [Mp4Player codec=" + codec + "] 正在播放 MP4 视频: " + fileName);
    }
}
