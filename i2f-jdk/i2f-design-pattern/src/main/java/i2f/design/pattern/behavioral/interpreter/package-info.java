/**
 * 解释器模式（Interpreter）
 *
 * <p><b>定义：</b>给定一个语言，定义它的文法的一种表示，并定义一个解释器，该解释器使用该表示来解释语言中的句子。</p>
 * <p><b>分类：</b>行为型模式</p>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>有一个简单的语言需要解释执行。</li>
 *   <li>文法相对简单且文法变化不频繁。</li>
 *   <li>表达式求值、规则引擎、SQL 解析等。</li>
 * </ul>
 *
 * <p><b>本包演示：</b>以"数学表达式计算器"为场景——将数学表达式建模为抽象语法树（AST），
 * 通过递归解释表达式树计算结果。每个文法规则映射为一个表达式类。</p>
 * <ul>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.Expression} —— 抽象表达式接口</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.terminal.NumberExpression} —— 终结符表达式（数字/变量）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.nonterminal.AddExpression} —— 非终结符表达式（加法）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.nonterminal.SubtractExpression} —— 非终结符表达式（减法）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.nonterminal.MultiplyExpression} —— 非终结符表达式（乘法）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.expression.nonterminal.DivideExpression} —— 非终结符表达式（除法）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.Context} —— 上下文（变量存储）</li>
 *   <li>{@link i2f.design.pattern.behavioral.interpreter.Test} —— 调用演示</li>
 * </ul>
 *
 * @see i2f.design.pattern.behavioral.interpreter.expression.Expression
 * @see i2f.design.pattern.behavioral.interpreter.Context
 * @see i2f.design.pattern.behavioral.interpreter.Test
 */
package i2f.design.pattern.behavioral.interpreter;
