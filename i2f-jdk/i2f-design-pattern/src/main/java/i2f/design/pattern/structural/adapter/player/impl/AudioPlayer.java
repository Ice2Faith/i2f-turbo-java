package i2f.design.pattern.structural.adapter.player.impl;

import i2f.design.pattern.structural.adapter.adapter.MediaAdapter;
import i2f.design.pattern.structural.adapter.player.MediaPlayer;
import lombok.Data;

/**
 * 适配器模式 —— 音频播放器（Concrete Target：AudioPlayer）
 *
 * <p><b>角色：</b>具体目标实现（已存在的、符合目标接口的类）</p>
 *
 * <p><b>模式说明：</b>AudioPlayer 本身只能播放 MP3 格式的音频文件。
 * 当客户端要求播放其他格式（如 VLC、MP4）时，
 * 它会将请求委托给 {@link MediaAdapter} 进行接口转换，
 * 这正是适配器模式中"组合适配器"的典型用法。</p>
 *
 * <p><b>命名立意：</b>模拟一个已有的基础音频播放器，
 * 它自身功能有限，但通过引入适配器，
 * 能够无缝扩展支持更多媒体格式，而无需修改自身核心逻辑。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see MediaPlayer
 * @see MediaAdapter
 */
@Data
public class AudioPlayer implements MediaPlayer {

    /**
     * 支持的默认音频格式。
     */
    private static final String SUPPORTED_FORMAT = "mp3";

    @Override
    public void play(String audioType, String fileName) {
        // 1. 支持原生格式，直接播放
        if (SUPPORTED_FORMAT.equalsIgnoreCase(audioType)) {
            System.out.println("  [AudioPlayer] 正在播放音频: " + fileName);
            return;
        }

        // 2. 不支持的格式，尝试通过适配器转换调用
        MediaAdapter mediaAdapter = new MediaAdapter(audioType);
        if (mediaAdapter != null) {
            System.out.println("  [AudioPlayer] 检测到不支持的格式，使用适配器转换...");
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("  [AudioPlayer] 错误：不支持的媒体格式 - " + audioType);
        }
    }
}
