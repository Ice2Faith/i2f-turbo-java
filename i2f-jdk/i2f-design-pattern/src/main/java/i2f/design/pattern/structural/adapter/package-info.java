/**
 * 适配器模式（Adapter）
 * <p>
 * 将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而不能一起工作的类可以协同工作。
 * 分类：结构型模式
 * </p>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Target（目标接口）          Adapter（适配器）           Adaptee（适配者）
 *  ──────────────────────────   ──────────────────────────   ──────────────────────────
 *  MediaPlayer                  MediaAdapter                 AdvancedMediaPlayer
 *    └─ play()                    ├─ 实现 Target 接口          ├─ playVlc()
 *                                 └─ 持有 Adaptee 实例         └─ playMp4()
 *                                                              ├─ VlcPlayer（具体适配者）
 *                                                              └─ Mp4Player（具体适配者）
 *
 *  Client（客户端）
 *  ──────────────────────────
 *  AudioPlayer（已存在的目标实现）
 *    └─ 原生支持 mp3
 *    └─ 通过 MediaAdapter 转换调用 vlc/mp4
 * </pre>
 *
 * <h3>演示入口</h3>
 * <ul>
 *   <li>{@link i2f.design.pattern.structural.adapter.Test} - 运行此类查看完整演示</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see i2f.design.pattern.structural.adapter.player.MediaPlayer
 * @see i2f.design.pattern.structural.adapter.adapter.MediaAdapter
 * @see i2f.design.pattern.structural.adapter.advanced.AdvancedMediaPlayer
 */
package i2f.design.pattern.structural.adapter;
