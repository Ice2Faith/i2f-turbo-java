package i2f.design.pattern.behavioral.interpreter.expression;

import i2f.design.pattern.behavioral.interpreter.Context;

/**
 * 解释器模式 —— 抽象表达式（Abstract Expression）
 *
 * <p><b>角色：</b>抽象表达式接口（Abstract Expression）</p>
 *
 * <p><b>模式说明：</b>定义解释器的统一接口 {@link #interpret(Context)}，
 * 所有具体表达式（终结符和非终结符）都必须实现此接口。
 * 这是解释器模式的核心抽象层。</p>
 *
 * <p><b>命名立意：</b>以"数学表达式计算器"为场景——
 * 每个表达式对象都具备"解释"自己的能力，
 * 给定一个上下文（变量环境），就能计算出表达式的值。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象表达式                  具体表达式
 *  ─────────────────────────   ─────────────────────────────────────
 *  Expression                  NumberExpression（数字 —— 终结符表达式）
 *                              AddExpression（加法 —— 非终结符表达式）
 *                              SubtractExpression（减法 —— 非终结符表达式）
 *                              MultiplyExpression（乘法 —— 非终结符表达式）
 *                              DivideExpression（除法 —— 非终结符表达式）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 * @see Context
 */
public interface Expression {

    /**
     * 解释表达式并计算结果。
     *
     * <p>不同的表达式以不同的方式计算值：
     * <ul>
     *   <li>终结符表达式（如数字）：直接返回数值</li>
     *   <li>非终结符表达式（如加减乘除）：递归解释子表达式后运算</li>
     * </ul>
     * </p>
     *
     * @param context 上下文环境，包含变量绑定等信息
     * @return 表达式计算结果
     */
    double interpret(Context context);
}
