package i2f.design.pattern.behavioral.interpreter;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 解释器模式 —— 上下文（Context）
 *
 * <p><b>角色：</b>上下文环境（Context）</p>
 *
 * <p><b>说明：</b>存储解释器执行过程中需要的全局信息，
 * 最典型的是变量绑定（变量名 → 变量值）。
 * 表达式在解释时可以访问上下文获取变量值。</p>
 *
 * <p><b>使用场景：</b></p>
 * <ul>
 *   <li>变量查找：当表达式引用变量时，从上下文中获取其值</li>
 *   <li>全局配置：可以存储解释器的全局配置信息</li>
 *   <li>中间结果：可以缓存子表达式的计算结果（可选）</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:30
 */
@Data
public class Context {

    /**
     * 变量存储表（变量名 → 变量值）。
     */
    private Map<String, Double> variables;

    /**
     * 构造空的上下文。
     */
    public Context() {
        this.variables = new HashMap<>();
    }

    /**
     * 设置变量值。
     *
     * @param name  变量名
     * @param value 变量值
     */
    public void setVariable(String name, double value) {
        variables.put(name, value);
    }

    /**
     * 获取变量值。
     *
     * @param name 变量名
     * @return 变量值，如果未定义则返回 null
     */
    public Double getVariable(String name) {
        return variables.get(name);
    }

    /**
     * 检查变量是否已定义。
     *
     * @param name 变量名
     * @return true 如果变量已定义
     */
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    /**
     * 清空所有变量。
     */
    public void clear() {
        variables.clear();
    }

    @Override
    public String toString() {
        return "Context{variables=" + variables + "}";
    }
}
