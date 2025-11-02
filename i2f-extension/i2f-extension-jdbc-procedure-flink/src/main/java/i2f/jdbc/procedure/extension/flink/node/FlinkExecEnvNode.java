package i2f.jdbc.procedure.extension.flink.node;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.extension.flink.consts.FlinkTagConsts;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/9/23 14:20
 */
public class FlinkExecEnvNode extends AbstractExecutorNode {
    public static final String TAG_NAME = FlinkTagConsts.FLINK_EXEC_ENV;

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Configuration conf = new Configuration();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object val = executor.resultValue(env, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }
    }
}
