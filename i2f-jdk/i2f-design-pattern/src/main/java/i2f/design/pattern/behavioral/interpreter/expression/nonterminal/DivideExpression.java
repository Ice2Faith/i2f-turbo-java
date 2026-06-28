package i2f.design.pattern.behavioral.interpreter.expression.nonterminal;

import i2f.design.pattern.behavioral.interpreter.Context;
import i2f.design.pattern.behavioral.interpreter.expression.Expression;
import lombok.Data;

/**
 * 解释器模式 —— 除法表达式（Non-terminal Expression：DivideExpression）
 *
 * <p><b>角色：</b>非终结符表达式（Non-terminal Expression）</p>
 *
 * <p><b>说明：</b>除法表达式包含左右两个子表达式，
 * 解释时需要递归调用子表达式的 interpret() 方法，
 * 然后用左操作数除以右操作数。
 * 包含除零检查，避免 ArithmeticException。</p>
 *
 * <p><b>文法规则：</b> DivideExpression ::= Expression '/' Expression</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 * @see Expression
 * @see Context
 */
@Data
public class DivideExpression implements Expression {

    /**
     * 左操作数表达式。
     */
    private Expression left;

    /**
     * 右操作数表达式。
     */
    private Expression right;

    /**
     * 构造除法表达式。
     *
     * @param left  左操作数表达式
     * @param right 右操作数表达式
     */
    public DivideExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double interpret(Context context) {
        // 递归解释左右子表达式
        double divisor = right.interpret(context);
        
        // 卫语句：检查除数是否为零
        if (divisor == 0.0) {
            throw new ArithmeticException("除法运算错误：除数不能为零");
        }
        
        return left.interpret(context) / divisor;
    }

    @Override
    public String toString() {
        return "(" + left + " / " + right + ")";
    }
}
