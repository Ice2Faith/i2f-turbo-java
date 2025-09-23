package i2f.jdbc.procedure.extension.datax.node;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.extension.datax.consts.DataxAttrConsts;
import i2f.jdbc.procedure.extension.datax.consts.DataxTagConsts;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.os.OsUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2025/9/23 16:11
 */
public class DataxExecNode extends AbstractExecutorNode {
    public static final String TAG_NAME = DataxTagConsts.DATAX_EXEC;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String commandFilePath=executor.convertAs(executor.attrValue(DataxAttrConsts.COMMAND,FeatureConsts.STRING,node,context),String.class);
        String script=executor.convertAs(executor.attrValue(AttrConsts.SCRIPT,FeatureConsts.VISIT,node,context),String.class);
        if(script!=null){
            script=script.trim();
        }
        if(script==null || script.isEmpty()){
            script=node.getTextBody();
        }

        File jobFile=null;
        try {
            jobFile=File.createTempFile("datax-job-"+(UUID.randomUUID().toString().replace("-","")),".json");
            StreamUtil.writeString(script, StandardCharsets.UTF_8,jobFile);
            String jobFilePath=jobFile.getAbsolutePath();

            String res = OsUtil.runCmd(new String[]{commandFilePath,jobFilePath});

            String result = node.getTagAttrMap().get(AttrConsts.RESULT);
            if (result != null) {
                Object val = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                executor.visitSet(context, result, val);
            }
        } catch (IOException e) {
            throw new ThrowSignalException(e.getMessage(),e);
        }finally {
            if(jobFile!=null && jobFile.exists()){
                jobFile.delete();
            }
        }

    }
}
