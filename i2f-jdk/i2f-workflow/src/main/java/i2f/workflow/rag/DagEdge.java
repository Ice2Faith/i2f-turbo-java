package i2f.workflow.rag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.function.Predicate;

/**
 * 边定义
 * 边上允许带有条件，决定后置节点是否需要执行
 *
 * @author Ice2Faith
 * @date 2026/4/4 21:03
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DagEdge {
    /**
     * 起始节点ID
     */
    protected String fromId;
    /**
     * 结束节点ID
     */
    protected String toId;
    /**
     * 边上的条件，条件用于对前一个节点的运行结果进行判断
     */
    protected Predicate<Object> condition = DagEdge::always;

    public static boolean always(Object val) {
        return true;
    }
}
