package i2f.design.pattern.structural.bridge;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.device.impl.TV;
import i2f.design.pattern.structural.bridge.device.impl.Radio;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;
import i2f.design.pattern.structural.bridge.remote.impl.BasicRemote;
import i2f.design.pattern.structural.bridge.remote.impl.AdvancedRemote;

/**
 * 桥接模式 —— 调用演示
 *
 * <p>演示桥接模式的核心机制：客户端通过组合而非继承的方式，
 * 将抽象部分（遥控器 {@link RemoteControl}）与实现部分（设备 {@link Device}）
 * 解耦，使两个维度可以独立变化和扩展。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 桥接模式核心演示 ====================
        System.out.println("====== 桥接模式（Bridge Pattern）演示 ======");
        System.out.println("场景：遥控器（Abstraction）通过组合设备接口（Implementor）控制不同设备");
        System.out.println("      遥控器类型和设备类型可以独立变化，避免类爆炸问题\n");

        // ==================== 2. 基础遥控器控制电视 ====================
        System.out.println("────── 基础遥控器 + 电视 ──────");
        Device tv = new TV("小米电视 65寸");
        RemoteControl basicRemote = new BasicRemote(tv);
        System.out.println("遥控器类型：" + basicRemote.getRemoteType());
        System.out.println("控制设备：" + tv + "\n");

        basicRemote.togglePower();
        basicRemote.volumeUp();
        basicRemote.volumeUp();
        basicRemote.volumeDown();
        basicRemote.togglePower();

        // ==================== 3. 基础遥控器控制收音机 ====================
        System.out.println("────── 基础遥控器 + 收音机 ──────");
        Device radio = new Radio("索尼便携收音机");
        basicRemote.setDevice(radio);
        System.out.println("遥控器类型：" + basicRemote.getRemoteType());
        System.out.println("控制设备：" + radio + "\n");

        basicRemote.togglePower();
        basicRemote.volumeUp();
        basicRemote.volumeDown();
        basicRemote.togglePower();

        System.out.println();

        // ==================== 4. 高级遥控器控制电视（扩展功能演示） ====================
        System.out.println("────── 高级遥控器 + 电视（扩展功能） ──────");
        Device tv2 = new TV("三星 QLED 75寸");
        AdvancedRemote advancedRemote = new AdvancedRemote(tv2);
        System.out.println("遥控器类型：" + advancedRemote.getRemoteType());
        System.out.println("控制设备：" + tv2 + "\n");

        advancedRemote.togglePower();
        advancedRemote.volumeUp();
        advancedRemote.setChannel(5);  // 高级功能：频道切换
        advancedRemote.mute();         // 高级功能：静音
        advancedRemote.togglePower();

        System.out.println();

        // ==================== 5. 高级遥控器控制收音机（扩展功能演示） ====================
        System.out.println("────── 高级遥控器 + 收音机（扩展功能） ──────");
        Device radio2 = new Radio("德生专业收音机");
        advancedRemote.setDevice(radio2);
        System.out.println("遥控器类型：" + advancedRemote.getRemoteType());
        System.out.println("控制设备：" + radio2 + "\n");

        advancedRemote.togglePower();
        advancedRemote.tuneFrequency(101.7);  // 高级功能：频率调谐
        advancedRemote.volumeUp();
        advancedRemote.tuneFrequency(88.5);   // 切换频率
        advancedRemote.togglePower();

        System.out.println();

        // ==================== 6. 运行时动态切换设备（桥接模式核心优势） ====================
        System.out.println("====== 桥接模式核心优势：运行时动态切换实现 ======");
        System.out.println("同一个遥控器在运行时切换控制不同的设备：\n");

        RemoteControl universalRemote = new BasicRemote(new TV("客厅电视"));
        System.out.println("初始配置：" + universalRemote);
        universalRemote.togglePower();

        System.out.println("─── 动态切换到收音机 ───");
        universalRemote.setDevice(new Radio("卧室收音机"));
        System.out.println("切换后配置：" + universalRemote);
        universalRemote.togglePower();

        System.out.println("─── 再次切换到电视 ───");
        universalRemote.setDevice(new TV("书房电视"));
        System.out.println("切换后配置：" + universalRemote);
        universalRemote.togglePower();

        System.out.println();

        // ==================== 7. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体类型 ======");
        System.out.println("通过统一接口调度不同的遥控器与设备组合：\n");

        RemoteControl[] remotes = {
                new BasicRemote(new TV("基础遥控-电视")),
                new BasicRemote(new Radio("基础遥控-收音机")),
                new AdvancedRemote(new TV("高级遥控-电视")),
                new AdvancedRemote(new Radio("高级遥控-收音机"))
        };

        for (int i = 0; i < remotes.length; i++) {
            System.out.println("测试场景 " + (i + 1) + "：");
            System.out.println("  遥控器：" + remotes[i].getRemoteType());
            System.out.println("  设备：" + remotes[i].getDevice());
            remotes[i].togglePower();
            remotes[i].volumeUp();
            remotes[i].volumeDown();
            remotes[i].togglePower();
            System.out.println();
        }

        // ==================== 8. 验证桥接模式避免类爆炸 ====================
        System.out.println("====== 桥接模式 vs 继承方案对比 ======");
        System.out.println("【继承方案】需要创建的类数量 = 遥控器类型数 × 设备类型数");
        System.out.println("  2种遥控器 × 2种设备 = 4个类（TVBasicRemote、TVAdvancedRemote、RadioBasicRemote、RadioAdvancedRemote）");
        System.out.println("  新增1种设备 → 2×3=6个类，新增1种遥控器 → 3×2=6个类");
        System.out.println("  类数量呈乘法增长，难以维护\n");

        System.out.println("【桥接方案】需要创建的类数量 = 遥控器类型数 + 设备类型数");
        System.out.println("  2种遥控器 + 2种设备 = 4个类（但结构清晰、职责单一）");
        System.out.println("  新增1种设备 → 只需+1个设备类，遥控器无需修改");
        System.out.println("  新增1种遥控器 → 只需+1个遥控器类，设备无需修改");
        System.out.println("  类数量呈加法增长，易于扩展\n");

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 桥接模式优势总结 ======");
        System.out.println("1. 分离抽象与实现：遥控器（控制逻辑）与设备（执行逻辑）独立演化");
        System.out.println("2. 避免类爆炸：使用组合替代继承，类数量从乘法增长变为加法增长");
        System.out.println("3. 运行时切换：同一个遥控器可以动态切换控制不同的设备");
        System.out.println("4. 遵循开闭原则：新增遥控器或设备类型无需修改现有代码");
        System.out.println("5. 遵循单一职责：遥控器专注控制流程，设备专注具体实现");
        System.out.println("6. 客户端面向抽象编程：依赖 Device 和 RemoteControl 接口，降低耦合");
    }
}
