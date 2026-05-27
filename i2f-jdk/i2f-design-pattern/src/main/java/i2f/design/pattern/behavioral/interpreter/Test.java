package i2f.design.pattern.behavioral.interpreter;

import i2f.design.pattern.behavioral.interpreter.expression.Expression;
import i2f.design.pattern.behavioral.interpreter.expression.nonterminal.AddExpression;
import i2f.design.pattern.behavioral.interpreter.expression.nonterminal.DivideExpression;
import i2f.design.pattern.behavioral.interpreter.expression.nonterminal.MultiplyExpression;
import i2f.design.pattern.behavioral.interpreter.expression.nonterminal.SubtractExpression;
import i2f.design.pattern.behavioral.interpreter.expression.terminal.NumberExpression;

/**
 * 解释器模式 —— 调用演示
 *
 * <p>演示解释器模式的核心机制：将语言中的每个文法规则映射为一个类，
 * 通过组合这些表达式对象构建抽象语法树（AST），
 * 然后递归调用 interpret() 方法解释执行。</p>
 *
 * <p><b>演示场景：</b>数学表达式计算器</p>
 * <ul>
 *   <li>终结符表达式：数字（字面量和变量）</li>
 *   <li>非终结符表达式：加法、减法、乘法、除法</li>
 *   <li>上下文：存储变量绑定</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 解释器模式核心演示 ====================
        System.out.println("====== 解释器模式（Interpreter）演示 ======");
        System.out.println("场景：数学表达式计算器（解释器递归解释表达式树）");
        System.out.println("      终结符：数字；非终结符：加减乘除\n");

        // ==================== 2. 简单表达式：3 + 5 ====================
        System.out.println("────── 示例 1：简单加法（3 + 5） ──────");
        Expression expr1 = new AddExpression(
                new NumberExpression(3),
                new NumberExpression(5)
        );
        Context context1 = new Context();
        System.out.println("  表达式: " + expr1);
        System.out.println("  结果: " + expr1.interpret(context1));

        System.out.println();

        // ==================== 3. 复合表达式：(10 - 3) * 2 ====================
        System.out.println("────── 示例 2：复合表达式（(10 - 3) * 2） ──────");
        Expression expr2 = new MultiplyExpression(
                new SubtractExpression(
                        new NumberExpression(10),
                        new NumberExpression(3)
                ),
                new NumberExpression(2)
        );
        Context context2 = new Context();
        System.out.println("  表达式: " + expr2);
        System.out.println("  结果: " + expr2.interpret(context2));

        System.out.println();

        // ==================== 4. 带变量的表达式：price * quantity ====================
        System.out.println("────── 示例 3：变量表达式（price * quantity） ──────");
        Expression expr3 = new MultiplyExpression(
                new NumberExpression("price"),
                new NumberExpression("quantity")
        );
        Context context3 = new Context();
        context3.setVariable("price", 99.5);
        context3.setVariable("quantity", 4);
        System.out.println("  表达式: " + expr3);
        System.out.println("  上下文: " + context3);
        System.out.println("  结果: " + expr3.interpret(context3));

        System.out.println();

        // ==================== 5. 复杂表达式：(a + b) * (c - d) / e ====================
        System.out.println("────── 示例 4：复杂表达式（(a + b) * (c - d) / e） ──────");
        Expression expr4 = new DivideExpression(
                new MultiplyExpression(
                        new AddExpression(
                                new NumberExpression("a"),
                                new NumberExpression("b")
                        ),
                        new SubtractExpression(
                                new NumberExpression("c"),
                                new NumberExpression("d")
                        )
                ),
                new NumberExpression("e")
        );
        Context context4 = new Context();
        context4.setVariable("a", 10);
        context4.setVariable("b", 5);
        context4.setVariable("c", 20);
        context4.setVariable("d", 8);
        context4.setVariable("e", 3);
        System.out.println("  表达式: " + expr4);
        System.out.println("  上下文: " + context4);
        System.out.println("  结果: " + expr4.interpret(context4));

        System.out.println();

        // ==================== 6. 实际应用：商品折扣计算 ====================
        System.out.println("────── 示例 5：实际场景 —— 商品折扣计算 ──────");
        System.out.println("  场景：原价 * 数量 * 折扣率 = 最终价格");
        Expression priceExpr = new MultiplyExpression(
                new MultiplyExpression(
                        new NumberExpression("originalPrice"),
                        new NumberExpression("quantity")
                ),
                new NumberExpression("discountRate")
        );
        Context shoppingContext = new Context();
        shoppingContext.setVariable("originalPrice", 299.0);
        shoppingContext.setVariable("quantity", 3);
        shoppingContext.setVariable("discountRate", 0.8); // 8折
        System.out.println("  表达式: " + priceExpr);
        System.out.println("  上下文: " + shoppingContext);
        System.out.println("  原价: ¥299.0 × 3件 = ¥897.0");
        System.out.println("  折扣: 8折（0.8）");
        System.out.println("  最终价格: ¥" + String.format("%.2f", priceExpr.interpret(shoppingContext)));

        System.out.println();

        // ==================== 7. 异常处理：除零错误 ====================
        System.out.println("────── 示例 6：异常处理 —— 除零检查 ──────");
        Expression divideByZero = new DivideExpression(
                new NumberExpression(100),
                new NumberExpression(0)
        );
        Context context6 = new Context();
        System.out.println("  表达式: " + divideByZero);
        try {
            divideByZero.interpret(context6);
        } catch (ArithmeticException e) {
            System.out.println("  捕获异常: " + e.getMessage());
        }

        System.out.println();

        // ==================== 8. 异常处理：未定义变量 ====================
        System.out.println("────── 示例 7：异常处理 —— 未定义变量 ──────");
        Expression undefinedVar = new AddExpression(
                new NumberExpression("x"),
                new NumberExpression(10)
        );
        Context context7 = new Context();
        System.out.println("  表达式: " + undefinedVar);
        System.out.println("  上下文: " + context7 + "（未定义变量 x）");
        try {
            undefinedVar.interpret(context7);
        } catch (IllegalArgumentException e) {
            System.out.println("  捕获异常: " + e.getMessage());
        }

        System.out.println();

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 解释器模式优势总结 ======");
        System.out.println("1. 易于扩展新规则：新增运算符只需新增一个表达式类（如幂运算、取模）");
        System.out.println("2. 实现简单：每个表达式类职责单一，代码清晰");
        System.out.println("3. 天然支持递归：非终结符表达式通过递归调用子表达式实现复杂逻辑");
        System.out.println("4. 易于构建抽象语法树（AST）：表达式对象组合形成树形结构");
        System.out.println("\n====== 解释器模式局限性 ======");
        System.out.println("1. 文法复杂时类爆炸：每个文法规则都需要一个类");
        System.out.println("2. 性能问题：递归解释效率较低，复杂表达式需优化");
        System.out.println("3. 适用场景有限：仅适合简单文法，复杂语言应使用专业解析器（如 ANTLR）");
    }
}
