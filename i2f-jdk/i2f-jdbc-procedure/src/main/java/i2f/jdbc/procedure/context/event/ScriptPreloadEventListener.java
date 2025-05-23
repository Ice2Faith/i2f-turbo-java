package i2f.jdbc.procedure.context.event;

import i2f.compiler.MemoryCompiler;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.groovy.GroovyScript;
import i2f.extension.ognl.OgnlUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventListener;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.impl.LangEvalGroovyNode;
import i2f.jdbc.procedure.node.impl.LangEvalJavaNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/5/22 9:25
 */
@Data
@NoArgsConstructor
public class ScriptPreloadEventListener implements XProc4jEventListener {
    protected JdbcProcedureExecutor executor;
    protected final AtomicInteger maxCount=new AtomicInteger(1);
    protected final AtomicInteger count=new AtomicInteger(0);

    public ScriptPreloadEventListener(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean support(XProc4jEvent event) {
        return event instanceof JdbcProcedureMetaMapRefreshedEvent;
    }

    @Override
    public boolean handle(XProc4jEvent event) {
        if(maxCount.get()>=0 && count.get()>=maxCount.get()){
            return false;
        }
        count.incrementAndGet();
        JdbcProcedureMetaMapRefreshedEvent evt = (JdbcProcedureMetaMapRefreshedEvent) event;
        if(executor!=null){
            executor.logInfo(()-> XProc4jConsts.NAME+" script-preload begin...");
        }
        long beginTs=System.currentTimeMillis();
        Map<String, ProcedureMeta> metaMap = evt.getMetaMap();
        if(metaMap!=null){
            for (Map.Entry<String, ProcedureMeta> entry : metaMap.entrySet()) {
                ProcedureMeta value = entry.getValue();
                if (value.getType()== ProcedureMeta.Type.XML) {
                    XmlNode node = (XmlNode) value.getTarget();
                    preloadNodeScript(node,executor);
                }
            }
        }
        long endTs=System.currentTimeMillis();
        long useTs=endTs-beginTs;
        if(executor!=null){
            executor.logInfo(()-> XProc4jConsts.NAME+" script-preload finish, use "+useTs+"(ms).");
        }
        return false;
    }

    public static void preloadNodeScript(XmlNode node,JdbcProcedureExecutor executor){
        if(node==null){
            return;
        }
        String tagName = node.getTagName();
        if(tagName!=null){
            if(TagConsts.LANG_EVAL.equals(tagName)){
                try {
                    OgnlUtil.parseExpressionTreeNode(node.getTextBody());
                } catch (Throwable e) {
                    executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" body error :"+e.getMessage(),e);
                }
            }else if(TagConsts.LANG_EVAL_TS.equals(tagName)
                    ||TagConsts.LANG_EVAL_TINYSCRIPT.equals(tagName)){
                try {
                    TinyScript.parse(node.getTextBody());
                } catch (Throwable e) {
                    executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" body  error :"+e.getMessage(),e);
                }
            }else if(TagConsts.LANG_EVAL_JAVA.equals(tagName)){
                List<XmlNode> children = node.getChildren();
                String importSegment="";
                String memberSegment="";
                String bodySegment=node.getTagBody();
                for (XmlNode item : children) {
                    if(TagConsts.LANG_JAVA_IMPORT.equals(item.getTagName())){
                        importSegment=item.getTextBody();
                    }else if(TagConsts.LANG_JAVA_MEMBE.equals(item.getTagName())){
                        memberSegment=item.getTextBody();
                    }else if(TagConsts.LANG_JAVA_BODY.equals(item.getTagName())){
                        bodySegment=item.getTextBody();
                    }
                }
                Map.Entry<String, String> codeEntry = LangEvalJavaNode.getFullJavaSourceCode(importSegment, memberSegment, bodySegment);
                try {
                    MemoryCompiler.findCompileClass(codeEntry.getValue(), codeEntry.getKey() + ".java", codeEntry.getKey());
                } catch (Throwable e) {
                    executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" body error :"+e.getMessage()+"\n source code:\n"+codeEntry.getValue(),e);
                }
            }else if(TagConsts.LANG_EVAL_GROOVY.equals(tagName)){
                String fullSourceCode = LangEvalGroovyNode.getFullSourceCode(node.getTextBody());
                try {
                    GroovyScript.parseAsClass(fullSourceCode);
                } catch (Throwable e) {
                    executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" body error :"+e.getMessage()+"\n source code:\n"+fullSourceCode,e);
                }
            }
        }

        Map<String, List<String>> featureMap = node.getAttrFeatureMap();
        if(featureMap!=null && !featureMap.isEmpty()){
            for (Map.Entry<String, List<String>> entry : featureMap.entrySet()) {
                List<String> list = entry.getValue();
                if(!list.isEmpty()){
                    String feature = list.get(0);
                    if(FeatureConsts.EVAL.equals(feature)){
                        try {
                            OgnlUtil.parseExpressionTreeNode(node.getTagAttrMap().get(entry.getKey()));
                        } catch (Throwable e) {
                            executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" "+entry.getKey()+" feature error :"+e.getMessage(),e);
                        }
                    }else if(FeatureConsts.EVAL_TS.equals(feature)
                    ||FeatureConsts.EVAL_TINYSCRIPT.equals(feature)){
                        try {
                            TinyScript.parse(node.getTagAttrMap().get(entry.getKey()));
                        } catch (Throwable e) {
                            executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" "+entry.getKey()+" feature error :"+e.getMessage(),e);
                        }
                    } else if(FeatureConsts.EVAL_JAVA.equals(feature)){
                        Map.Entry<String, String> codeEntry = LangEvalJavaNode.getFullJavaSourceCode("", "", node.getTagAttrMap().get(entry.getKey()));
                        try {
                            MemoryCompiler.findCompileClass(codeEntry.getValue(), codeEntry.getKey() + ".java", codeEntry.getKey());
                        } catch (Throwable e) {
                            executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" "+entry.getKey()+" feature error :"+e.getMessage()+"\n source code:\n"+codeEntry.getValue(),e);
                        }
                    }else if(FeatureConsts.EVAL_GROOVY.equals(feature)){
                        String fullSourceCode = LangEvalGroovyNode.getFullSourceCode(node.getTagAttrMap().get(entry.getKey()));
                        try {
                            GroovyScript.parseAsClass(fullSourceCode);
                        } catch (Throwable e) {
                            executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" "+entry.getKey()+" feature error :"+e.getMessage()+"\n source code:\n"+fullSourceCode,e);
                        }
                    }
                }
            }
        }

        Map<String, String> attrMap = node.getTagAttrMap();
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            if(AttrConsts.TEST.equals(entry.getKey())){
                List<String> features = node.getAttrFeatureMap().get(entry.getKey());
                if(features==null || features.isEmpty()){
                    try {
                        OgnlUtil.parseExpressionTreeNode(entry.getValue());
                    } catch (Throwable e) {
                        executor.logInfo(()->"script-preload at "+XmlNode.getNodeLocation(node)+" "+entry.getKey()+" error :"+e.getMessage(),e);
                    }
                }
            }
        }

        List<XmlNode> children = node.getChildren();
        if(children!=null && !children.isEmpty()){
            for (XmlNode item : children) {
                preloadNodeScript(item,executor);
            }
        }
    }
}
