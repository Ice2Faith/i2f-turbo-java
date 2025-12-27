package i2f.jdbc.procedure.extension.flink.node;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.extension.flink.consts.FlinkAttrConsts;
import i2f.jdbc.procedure.extension.flink.consts.FlinkTagConsts;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/9/23 14:25
 */
public class FlinkExecuteNode extends AbstractExecutorNode {
    public static final String TAG_NAME = FlinkTagConsts.FLINK_EXECUTE;

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        StreamExecutionEnvironment execEnv = (StreamExecutionEnvironment) executor.attrValue(FlinkAttrConsts.EXEC_ENV, FeatureConsts.VISIT, node, context);
        String jobName = executor.convertAs(executor.attrValue(AttrConsts.NAME, FeatureConsts.STRING, node, context), String.class);
        try {
            JobExecutionResult res = null;
            if (jobName != null && !jobName.isEmpty()) {
                res = execEnv.execute(jobName);
            } else {
                res = execEnv.execute();
            }
            String result = node.getTagAttrMap().get(AttrConsts.RESULT);
            if (result != null) {
                Object val = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                executor.visitSet(context, result, val);
            }
        } catch (Exception e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }
}
