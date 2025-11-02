package i2f.jdbc.procedure.extension.flink.node;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.extension.flink.consts.FlinkAttrConsts;
import i2f.jdbc.procedure.extension.flink.consts.FlinkTagConsts;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/9/23 14:28
 */
public class FlinkExecSqlNode extends AbstractExecutorNode {
    public static final String TAG_NAME = FlinkTagConsts.FLINK_EXEC_SQL;

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        StreamTableEnvironment tabEnv = (StreamTableEnvironment)executor.attrValue(FlinkAttrConsts.TAB_ENV, FeatureConsts.VISIT, node, context);
        String script=executor.convertAs(executor.attrValue(AttrConsts.SCRIPT,FeatureConsts.VISIT,node,context),String.class);
        if(script!=null){
            script=script.trim();
        }
        if(script==null || script.isEmpty()){
            script=node.getTextBody();
        }
        TableResult res = tabEnv.executeSql(script);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object val = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }

    }
}
