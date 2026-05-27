package i2f.design.pattern.structural.adapter;

import i2f.design.pattern.structural.adapter.player.MediaPlayer;
import i2f.design.pattern.structural.adapter.player.impl.AudioPlayer;

/**
 * 适配器模式 —— 调用演示
 *
 * <p>演示适配器模式的核心机制：客户端面向目标接口（{@link MediaPlayer}）编程，
 * 通过适配器（{@link i2f.design.pattern.structural.adapter.adapter.MediaAdapter}）
 * 将不兼容的高级播放器接口转换为标准接口，实现无缝调用。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 适配器模式核心演示 ====================
        System.out.println("====== 适配器模式（Adapter）演示 ======");
        System.out.println("场景：音频播放器（Target）通过适配器调用高级播放器（Adaptee）");
        System.out.println("      客户端统一使用 MediaPlayer 接口，无需关心底层播放器差异\n");

        // ==================== 2. 原生格式播放 —— 直接支持 ====================
        System.out.println("────── 播放原生支持的 MP3 格式 ──────");
        MediaPlayer audioPlayer = new AudioPlayer();
        audioPlayer.play("mp3", "平凡之路.mp3");

        System.out.println();

        // ==================== 3. VLC 格式播放 —— 适配器转换 ====================
        System.out.println("────── 播放 VLC 格式（通过适配器转换） ──────");
        audioPlayer.play("vlc", "复仇者联盟4.vlc");

        System.out.println();

        // ==================== 4. MP4 格式播放 —— 适配器转换 ====================
        System.out.println("────── 播放 MP4 格式（通过适配器转换） ──────");
        audioPlayer.play("mp4", "星际穿越.mp4");

        System.out.println();

        // ==================== 5. 不支持的格式 —— 优雅降级 ====================
        System.out.println("────── 播放不支持的格式（优雅降级） ──────");
        audioPlayer.play("avi", "测试视频.avi");

        System.out.println();

        // ==================== 6. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道适配器存在 ======");
        System.out.println("通过统一接口调度不同格式的播放任务：\n");

        String[][] playTasks = {
                {"mp3", "夜曲.mp3"},
                {"vlc", "阿凡达2.vlc"},
                {"mp4", "盗梦空间.mp4"},
                {"mp3", "七里香.mp3"},
                {"avi", "不支持的格式.avi"}
        };

        for (int i = 0; i < playTasks.length; i++) {
            System.out.println("播放任务 " + (i + 1) + "：");
            audioPlayer.play(playTasks[i][0], playTasks[i][1]);
            System.out.println();
        }

        // ==================== 7. 验证适配器每次创建新实例 ====================
        System.out.println("====== 验证：适配器内部每次创建新的播放器实例 ======");
        MediaPlayer player1 = new AudioPlayer();
        MediaPlayer player2 = new AudioPlayer();
        System.out.println("player1: " + player1);
        System.out.println("player2: " + player2);
        System.out.println("player1 == player2 ? " + (player1 == player2));

        System.out.println();

        // ==================== 8. 模式优势总结 ====================
        System.out.println("====== 适配器模式优势总结 ======");
        System.out.println("1. 让原本不兼容的接口可以协同工作，无需修改已有代码");
        System.out.println("2. 遵循开闭原则：新增播放器只需新增适配者和适配器映射");
        System.out.println("3. 客户端面向抽象编程：无需依赖具体播放器类，降低耦合");
        System.out.println("4. 复用现有组件：将第三方库或遗留系统集成到统一接口下");
        System.out.println("5. 适配逻辑集中管理：接口转换逻辑封装在适配器中，便于维护");
    }
}
