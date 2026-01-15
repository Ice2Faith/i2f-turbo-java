package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.stringify.BindSqlStringifier;
import i2f.bindsql.stringify.BindSqlStringifiers;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.jdbc.script.JdbcScriptRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlRunnerNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_RUNNER;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        Object scriptObj = executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context);
        String separator = (String) executor.attrValue(AttrConsts.SEPARATOR, FeatureConsts.STRING, node, context);
        Boolean jumpError = (Boolean) executor.attrValue(AttrConsts.JUMP_ERROR, FeatureConsts.BOOLEAN, node, context);
        Boolean fullSend = (Boolean) executor.attrValue(AttrConsts.FULL_SEND, FeatureConsts.BOOLEAN, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);

        Connection conn = executor.getConnection(datasource, context);

        String script=null;
        if(scriptObj!=null) {
            if (scriptObj instanceof BindSql) {
                try {
                    BindSqlStringifier stringifier = BindSqlStringifiers.of(conn);
                    script = stringifier.stringify((BindSql) scriptObj);
                } catch (SQLException e) {
                    executor.logger().logWarn("stringify BindSql error! " + e.getMessage(), e);
                }
            }
            if (script == null) {
                script = String.valueOf(scriptObj);
            }
        }
        if(script!=null){
            script=script.trim();
        }
        if(script==null || script.isEmpty()){
            script=node.getTextBody();
        }

        if (executor.isDebug()) {
            script = script.concat(getTrackingComment(node));
        }

        if (executor.isDebug()) {
            executor.logger().logDebug("sql-runner:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tscript:\n" + script);
        }

        StringBuffer resultBuffer=new StringBuffer();

        JdbcScriptRunner runner=new JdbcScriptRunner(conn);
        runner.setStopOnError(true);
        if(jumpError!=null && jumpError){
            runner.setStopOnError(false);
        }
        try {
            runner.setAutoCommit(conn.getAutoCommit());
        } catch (SQLException e) {
            throw new ThrowSignalException(e.getMessage(),e);
        }
        runner.setSendFullScript(false);
        if(fullSend!=null){
            runner.setSendFullScript(fullSend);
        }
        if(separator!=null && !separator.isEmpty()) {
            runner.setDelimiter(separator);
        }
        runner.setLogPrinter((o)->{
            String s=String.valueOf(o);
            resultBuffer.append(o);
            if(executor.isDebug()){
                executor.logger().logDebug(s);
            }
        });
        runner.setLogErrorPrinter((o,e)->{
            if(e!=null) {
                executor.logger().logError(o, e);
            }else{
                executor.logger().logError(o);
            }
        });
        runner.runScript(script);

        String row=resultBuffer.toString();
        if (result != null) {
            Object val = executor.resultValue(row, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }
    }

}
