package i2f.jdbc.procedure.node.impl;

import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.NodeTime;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.os.OsUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangShellNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_SHELL;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {


    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        boolean await = true;
        String awaitExpr = node.getTagAttrMap().get(AttrConsts.AWAIT);
        if (awaitExpr != null && !awaitExpr.isEmpty()) {
            await = executor.toBoolean(executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context));
        }
        long timeout = -1;
        String ttl = node.getTagAttrMap().get(AttrConsts.TIMEOUT);
        if (ttl != null && !ttl.isEmpty()) {
            timeout = executor.convertAs(executor.attrValue(AttrConsts.TIMEOUT, FeatureConsts.LONG, node, context), Long.class);
        }
        String timeUnitExpr = node.getTagAttrMap().get(AttrConsts.TIME_UNIT);
        TimeUnit timeUnit = NodeTime.getTimeUnit(timeUnitExpr, TimeUnit.MILLISECONDS);
        boolean runAsFile = executor.toBoolean(executor.attrValue(AttrConsts.RUN_AS_FILE, FeatureConsts.BOOLEAN, node, context));
        String workdir = executor.convertAs(executor.attrValue(AttrConsts.WORKDIR, FeatureConsts.STRING, node, context), String.class);
        if (workdir == null || workdir.isEmpty()) {
            workdir = ".";
        }
        String envpExpr = executor.convertAs(executor.attrValue(AttrConsts.ENVP, FeatureConsts.STRING, node, context), String.class);
        String[] envp = null;
        if (envpExpr != null && !envpExpr.isEmpty()) {
            envp = envpExpr.split("[;,|]");
        }
        String script = executor.convertAs(executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context), String.class);
        if (script == null || script.isEmpty()) {
            script = node.getTextBody();
        }
        script = script.trim();

        File dir = new File(workdir);
        File scriptFile = null;
        if (runAsFile) {
            String fileName = "tmp_script_" + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
            if (OsUtil.isWindows()) {
                fileName = fileName + ".bat";
            } else if (OsUtil.isLinux()) {
                fileName = fileName + ".sh";
            }

            scriptFile = new File(dir, fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                StreamUtil.writeString(script, OsUtil.getCmdCharset(), scriptFile);
            } catch (IOException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }

            if (OsUtil.isWindows()) {
                script = "cmd /c " + fileName;
            } else if (OsUtil.isLinux()) {
                OsUtil.runCmd("chmod +x " + fileName);
                script = "sh " + fileName;
            }
        }

        String val = null;
        try {
            val = OsUtil.execCmd(await, timeUnit.toMillis(timeout), script, envp, dir, OsUtil.getCmdCharset());
        } finally {
            if (scriptFile != null) {
                scriptFile.delete();
            }
        }

        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, res);
        }
    }
}
