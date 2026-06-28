package i2f.design.pattern.behavioral.interpreter.expression.nonterminal;

import i2f.design.pattern.behavioral.interpreter.Context;
import i2f.design.pattern.behavioral.interpreter.expression.Expression;
import lombok.Data;

/**
 * 解释器模式 —— 减法表达式（Non-terminal Expression：SubtractExpression）
 *
 * <p><b>角色：</b>非终结符表达式（Non-terminal Expression）</p>
 *
 * <p><b>说明：</b>减法表达式包含左右两个子表达式，
 * 解释时需要递归调用子表达式的 interpret() 方法，
 * 然后用左操作数减去右操作数。</p>
 *
 * <p><b>文法规则：</b> SubtractExpression ::= Expression '-' Expression</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 * @see Expression
 * @see Context
 */
@Data
public class SubtractExpression implements Expression {

    /**
     * 左操作数表达式。
     */
    private Expression left;

    /**
     * 右操作数表达式。
     */
    private Expression right;

    /**
     * 构造减法表达式。
     *
     * @param left  左操作数表达式
     * @param right 右操作数表达式
     */
    public SubtractExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double interpret(Context context) {
        // 递归解释左右子表达式，然后相减
        return left.interpret(context) - right.interpret(context);
    }

    @Override
    public String toString() {
        return "(" + left + " - " + right + ")";
    }
}
