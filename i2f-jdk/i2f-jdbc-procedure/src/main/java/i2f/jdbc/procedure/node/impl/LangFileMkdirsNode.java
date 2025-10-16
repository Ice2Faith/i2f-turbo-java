package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.io.File;
import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFileMkdirsNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FILE_MKDIRS;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Object obj = executor.attrValue(AttrConsts.FILE, FeatureConsts.VISIT, node, context);
        File file = null;
        if (obj == null) {

        } else if (obj instanceof File) {
            file = (File) obj;
        } else {
            file = new File(String.valueOf(obj));
        }
        boolean val = file.mkdirs();
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }
    }

}
