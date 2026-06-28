package i2f.design.pattern.structural.flyweight;

import i2f.design.pattern.structural.flyweight.character.Char;
import i2f.design.pattern.structural.flyweight.context.TextContext;
import i2f.design.pattern.structural.flyweight.factory.CharacterFactory;

/**
 * 享元模式 —— 调用演示
 *
 * <p>演示享元模式的核心机制：通过共享技术有效支持大量细粒度对象，
 * 相同内部状态的对象在内存中只保留一份实例，大幅减少内存占用。</p>
 *
 * <p><b>场景说明：</b>文字排版系统中，文档包含成千上万个字符，
 * 但相同字符（如 'A'、'B'）共享同一享元实例，
 * 位置、颜色、字体等外部状态由上下文管理。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 享元模式核心演示 ====================
        System.out.println("====== 享元模式（Flyweight）演示 ======");
        System.out.println("场景：文字排版系统通过享元池共享字符实例，减少内存占用");
        System.out.println("      相同字符共享同一对象，不同位置/颜色由上下文管理\n");

        // 创建享元工厂
        CharacterFactory factory = new CharacterFactory();

        // ==================== 2. 创建文档中的字符 ====================
        System.out.println("────── 创建文档字符（演示享元共享机制） ──────");
        System.out.println("文档内容：\"Hello World\"\n");

        // 创建 "Hello" 的字符享元
        Char h1 = factory.getCharacter('H');
        Char e1 = factory.getCharacter('e');
        Char l1 = factory.getCharacter('l');
        Char l2 = factory.getCharacter('l');  // 第二个 'l'，应复用
        Char o1 = factory.getCharacter('o');

        System.out.println();

        // ==================== 3. 验证享元共享机制 ====================
        System.out.println("====== 验证：相同字符共享同一实例 ======");
        System.out.println("l1 == l2 ? " + (l1 == l2));
        System.out.println("l1.equals(l2) ? " + l1.equals(l2));
        System.out.println("享元池大小：" + factory.getPoolSize() + "（字符种类：H, e, l, o）\n");

        // ==================== 4. 渲染文档（结合外部状态） ====================
        System.out.println("────── 渲染文档（享元 + 上下文） ──────\n");

        // 渲染 "Hello" - 每个字符有不同的位置和样式
        System.out.println("【第一行：Hello】");
        h1.render(new TextContext(10, 20, "BLACK", 14, "Times New Roman"));
        e1.render(new TextContext(25, 20, "BLACK", 14, "Times New Roman"));
        l1.render(new TextContext(40, 20, "BLACK", 14, "Times New Roman"));
        l2.render(new TextContext(55, 20, "BLACK", 14, "Times New Roman"));
        o1.render(new TextContext(70, 20, "BLACK", 14, "Times New Roman"));

        System.out.println();

        // 创建 "World" 的字符享元（部分字符可复用）
        System.out.println("【第二行：World】");
        Char w1 = factory.getCharacter('W');
        Char o2 = factory.getCharacter('o');  // 复用已有的 'o'
        Char r1 = factory.getCharacter('r');
        Char l3 = factory.getCharacter('l');  // 复用已有的 'l'
        Char d1 = factory.getCharacter('d');

        w1.render(new TextContext(10, 40, "BLUE", 14, "Arial"));
        o2.render(new TextContext(30, 40, "BLUE", 14, "Arial"));
        r1.render(new TextContext(50, 40, "BLUE", 14, "Arial"));
        l3.render(new TextContext(65, 40, "BLUE", 14, "Arial"));
        d1.render(new TextContext(80, 40, "BLUE", 14, "Arial"));

        System.out.println();
        System.out.println("享元池大小：" + factory.getPoolSize() + "（字符种类：H, e, l, o, W, r, d）\n");

        // ==================== 5. 享元模式内存优化效果演示 ====================
        System.out.println("====== 享元模式内存优化效果演示 ======");
        System.out.println("场景：渲染一篇 1000 字的文档，统计享元池使用情况\n");

        String document = "The quick brown fox jumps over the lazy dog. " +
                "Pack my box with five dozen liquor jugs. " +
                "How vexingly quick daft zebras jump. " +
                "The five boxing wizards jump quickly at dawn.";

        System.out.println("文档内容（共 " + document.length() + " 个字符）：");
        System.out.println("\"" + document + "\"\n");

        // 模拟渲染文档
        CharacterFactory docFactory = new CharacterFactory();
        int position = 10;
        for (int i = 0; i < document.length(); i++) {
            char c = document.charAt(i);
            Char ch = docFactory.getCharacter(c);

            // 模拟渲染（仅打印部分字符避免输出过多）
            if (i < 10 || i % 50 == 0) {
                ch.render(new TextContext(position, 100, "GRAY", 12));
            }

            position += 15;
        }

        System.out.println("\n====== 内存优化效果统计 ======");
        System.out.println("文档总字符数：" + document.length());
        System.out.println("实际创建享元数：" + docFactory.getPoolSize());
        System.out.println("内存节省率：" + String.format("%.2f%%",
                (1 - (double) docFactory.getPoolSize() / document.length()) * 100));
        System.out.println("\n说明：1000 字文档仅需创建 " + docFactory.getPoolSize() + " 个字符对象，而非 " + document.length() + " 个！\n");

        // ==================== 6. 验证享元不可变性 ====================
        System.out.println("====== 验证：享元对象的内部状态不可变 ======");
        Char a1 = factory.getCharacter('A');
        Char a2 = factory.getCharacter('A');
        System.out.println("a1.getSymbol() = '" + a1.getSymbol() + "'");
        System.out.println("a2.getSymbol() = '" + a2.getSymbol() + "'");
        System.out.println("a1 == a2 ? " + (a1 == a2));
        System.out.println("享元对象创建后，内部状态（字符本身）不可改变\n");

        // ==================== 7. 模式优势总结 ====================
        System.out.println("====== 享元模式优势总结 ======");
        System.out.println("1. 大幅减少内存占用：相同对象共享，避免重复创建");
        System.out.println("2. 分离内部状态与外部状态：享元仅存储可共享的内部状态");
        System.out.println("3. 适用于大量细粒度对象场景：如文字处理器、图形编辑器");
        System.out.println("4. 外部状态由客户端管理：位置、颜色等随场景变化的状态不存储在享元中");
        System.out.println("5. 享元工厂管理对象池：自动处理共享与创建逻辑");
    }
}
