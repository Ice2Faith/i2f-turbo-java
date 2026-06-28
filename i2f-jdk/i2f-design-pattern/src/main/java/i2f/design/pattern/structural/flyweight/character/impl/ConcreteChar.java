package i2f.design.pattern.structural.flyweight.character.impl;

import i2f.design.pattern.structural.flyweight.character.Char;
import i2f.design.pattern.structural.flyweight.context.TextContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 享元模式 —— 具体字符（Concrete Flyweight：ConcreteCharacter）
 *
 * <p><b>角色：</b>具体享元（Concrete Flyweight）</p>
 *
 * <p><b>说明：</b>实现抽象享元接口，存储字符的内部状态（字符本身）。
 * 内部状态是共享的——同一个字符（如 'A'）在内存中只有一份实例，
 * 无论它在文档中出现多少次。</p>
 *
 * <p><b>共享机制：</b>通过 {@link i2f.design.pattern.structural.flyweight.factory.CharacterFactory}
 * 管理享元池，相同字符返回同一实例，不同字符才创建新实例。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Char
 * @see i2f.design.pattern.structural.flyweight.factory.CharacterFactory
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConcreteChar extends Char {

    /**
     * 字符符号（内部状态 - 可共享）。
     */
    private char symbol;

    /**
     * 构造函数：创建具体字符享元。
     *
     * @param symbol 字符符号（内部状态）
     */
    public ConcreteChar(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public void render(TextContext context) {
        System.out.printf("  [%s] 字符 '%c' 渲染于位置(x=%d, y=%d)，颜色=%s，字号=%dpt%n",
                getTypeDescription(),
                symbol,
                context.getX(),
                context.getY(),
                context.getColor(),
                context.getFontSize());
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    /**
     * 获取字符类型描述。
     *
     * @return 类型描述
     */
    private String getTypeDescription() {
        if (Character.isLetter(symbol)) {
            return "字母";
        } else if (Character.isDigit(symbol)) {
            return "数字";
        } else if (Character.isWhitespace(symbol)) {
            return "空白";
        } else {
            return "标点";
        }
    }

    @Override
    public String toString() {
        return String.format("Character{symbol='%c', type='%s'}",
                symbol, getTypeDescription());
    }
}
