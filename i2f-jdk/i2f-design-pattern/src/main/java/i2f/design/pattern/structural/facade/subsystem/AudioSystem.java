package i2f.design.pattern.structural.facade.subsystem;

import lombok.Data;

/**
 * 外观模式 —— 音响子系统（Audio System）
 *
 * <p><b>角色：</b>子系统类（Subsystem Class）</p>
 *
 * <p><b>模式说明：</b>音响子系统包含播放、暂停、音量调节、音源切换等功能。
 * 通过外观类可以提供"音乐模式"、"影院模式"等场景化一键操作。</p>
 *
 * <p><b>命名立意：</b>"音响系统"是家庭娱乐的核心子系统，
 * 包含多种播放控制功能，但用户通常只需要简单的播放/暂停操作。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
public class AudioSystem {

    /**
     * 音响是否开启
     */
    private boolean isOn = false;

    /**
     * 音量级别（0-100）
     */
    private int volume = 30;

    /**
     * 播放状态
     */
    private String playState = "停止";

    /**
     * 当前音源
     */
    private String source = "蓝牙";

    /**
     * 开启音响
     */
    public void turnOn() {
        this.isOn = true;
        System.out.println("    [音响] 音响已开启 | 音量: " + volume + "% | 音源: " + source);
    }

    /**
     * 关闭音响
     */
    public void turnOff() {
        this.isOn = false;
        this.playState = "停止";
        System.out.println("    [音响] 音响已关闭");
    }

    /**
     * 播放音乐
     */
    public void play() {
        this.playState = "播放中";
        System.out.println("    [音响] 开始播放音乐");
    }

    /**
     * 暂停播放
     */
    public void pause() {
        this.playState = "已暂停";
        System.out.println("    [音响] 播放已暂停");
    }

    /**
     * 停止播放
     */
    public void stop() {
        this.playState = "停止";
        System.out.println("    [音响] 播放已停止");
    }

    /**
     * 调节音量
     *
     * @param level 音量级别（0-100）
     */
    public void setVolume(int level) {
        this.volume = Math.max(0, Math.min(100, level));
        System.out.println("    [音响] 音量已调节至: " + this.volume + "%");
    }

    /**
     * 切换音源
     *
     * @param source 音源名称（蓝牙/WiFi/aux/USB）
     */
    public void setSource(String source) {
        this.source = source;
        System.out.println("    [音响] 音源已切换至: " + this.source);
    }

    /**
     * 音乐模式：中等音量、蓝牙音源
     */
    public void setMusicMode() {
        this.volume = 40;
        this.source = "蓝牙";
        System.out.println("    [音响] 已切换至音乐模式（音量40%，蓝牙音源）");
    }

    /**
     * 影院模式：高音量、WiFi音源（环绕声）
     */
    public void setCinemaMode() {
        this.volume = 70;
        this.source = "WiFi";
        System.out.println("    [音响] 已切换至影院模式（音量70%，WiFi音源）");
    }
}
