package i2f.workflow.rag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/4/4 22:28
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DagInboundNode {
    protected DagNode node;
    protected DagEdgeStatus edgeStatus;
}
