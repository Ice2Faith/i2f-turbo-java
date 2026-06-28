package i2f.design.pattern.behavioral.interpreter.expression.terminal;

import i2f.design.pattern.behavioral.interpreter.Context;
import i2f.design.pattern.behavioral.interpreter.expression.Expression;
import lombok.Data;

/**
 * 解释器模式 —— 数字表达式（Terminal Expression：NumberExpression）
 *
 * <p><b>角色：</b>终结符表达式（Terminal Expression）</p>
 *
 * <p><b>说明：</b>数字表达式是文法中的终结符，不需要进一步解释。
 * 它直接返回存储的数值，或者从上下文中查找变量的值。
 * 这是解释器递归解释的"终止条件"。</p>
 *
 * <p><b>两种模式：</b></p>
 * <ul>
 *   <li>字面量模式：直接存储数值，如 "3.14"、"100"</li>
 *   <li>变量模式：存储变量名，解释时从 Context 中查找，如 "x"、"price"</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 * @see Expression
 * @see Context
 */
@Data
public class NumberExpression implements Expression {

    /**
     * 数值（字面量模式时使用）。
     */
    private double value;

    /**
     * 变量名（变量模式时使用，为 null 表示字面量模式）。
     */
    private String variableName;

    /**
     * 构造字面量数字表达式。
     *
     * @param value 数值
     */
    public NumberExpression(double value) {
        this.value = value;
        this.variableName = null;
    }

    /**
     * 构造变量数字表达式。
     *
     * @param variableName 变量名
     */
    public NumberExpression(String variableName) {
        this.value = 0.0;
        this.variableName = variableName;
    }

    @Override
    public double interpret(Context context) {
        // 变量模式：从上下文中查找变量值
        if (variableName != null) {
            Double varValue = context.getVariable(variableName);
            if (varValue == null) {
                throw new IllegalArgumentException("未定义的变量: " + variableName);
            }
            return varValue;
        }
        // 字面量模式：直接返回数值
        return value;
    }

    @Override
    public String toString() {
        return variableName != null ? variableName : String.valueOf(value);
    }
}
