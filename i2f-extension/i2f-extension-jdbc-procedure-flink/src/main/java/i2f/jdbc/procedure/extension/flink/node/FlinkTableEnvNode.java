package i2f.jdbc.procedure.extension.flink.node;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.extension.flink.consts.FlinkAttrConsts;
import i2f.jdbc.procedure.extension.flink.consts.FlinkTagConsts;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/9/23 14:25
 */
public class FlinkTableEnvNode extends AbstractExecutorNode {
    public static final String TAG_NAME = FlinkTagConsts.FLINK_TABLE_ENV;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        StreamExecutionEnvironment execEnv = (StreamExecutionEnvironment)executor.attrValue(FlinkAttrConsts.EXEC_ENV, FeatureConsts.VISIT, node, context);
        StreamTableEnvironment tabEnv = StreamTableEnvironment.create(execEnv);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object val = executor.resultValue(tabEnv, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }
    }
}
