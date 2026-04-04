package i2f.workflow.rag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:03
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class DagNode {
    protected String id;
    protected DagTask<?> task;
    protected Set<String> dependencies = new LinkedHashSet<>();
    protected DagStatus status = DagStatus.PENDING;
    protected Object result;
    protected Throwable error;
    protected DagInboundGateway inboundGateway = DagInboundGateways::allSuccess;

}
