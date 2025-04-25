package i2f.jdbc.procedure.node.impl;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangFileWriteTextNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_FILE_WRITE_TEXT;

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
        File file=null;
        if(obj==null){

        }else if(obj instanceof File){
            file=(File) obj;
        }else{
            String str=String.valueOf(obj);
            file=new File(str);
        }
        String charset = executor.convertAs(executor.attrValue(AttrConsts.CHARSET, FeatureConsts.STRING, node, context), String.class);
        if(charset==null || charset.isEmpty()){
            charset="UTF-8";
        }
        String content=executor.convertAs(executor.attrValue(AttrConsts.CONTENT,FeatureConsts.VISIT,node,context),String.class);
        if(content==null){
            content=node.getTextBody();
        }
        try {
            StreamUtil.writeString(content,file);
        } catch (IOException e) {
            throw new ThrowSignalException(e.getMessage(),e);
        }
    }

}
