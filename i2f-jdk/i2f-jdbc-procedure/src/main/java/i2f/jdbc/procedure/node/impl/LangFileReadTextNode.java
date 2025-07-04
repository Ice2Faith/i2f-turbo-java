package i2f.jdbc.procedure.node.impl;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.resources.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFileReadTextNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FILE_READ_TEXT;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Object obj = executor.attrValue(AttrConsts.FILE, FeatureConsts.VISIT, node, context);
        URL url = null;
        File file = null;
        if (obj == null) {

        } else if (obj instanceof File) {
            file = (File) obj;
        } else if (obj instanceof URL) {
            url = (URL) obj;
        } else {
            String str = String.valueOf(obj);
            if (str.startsWith(ResourceUtil.CLASSPATH_PREFIX)
                    || str.startsWith(ResourceUtil.CLASSPATH_MUL_PREFIX)) {
                try {
                    url = ResourceUtil.getResource(str);
                } catch (IOException e) {

                }
            }
            if (url == null) {
                try {
                    url = new URL(str);
                } catch (Exception e) {

                }
            }
            file = new File(str);
        }
        String charset = executor.convertAs(executor.attrValue(AttrConsts.CHARSET, FeatureConsts.STRING, node, context), String.class);
        if (charset == null || charset.isEmpty()) {
            charset = "UTF-8";
        }
        try {
            String val = null;
            if (url != null) {
                val = StreamUtil.readString(url, charset);
            } else {
                val = StreamUtil.readString(file, charset);
            }
            String result = node.getTagAttrMap().get(AttrConsts.RESULT);
            if (result != null) {
                Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                executor.visitSet(context, result, res);
            }
        } catch (IOException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

}
