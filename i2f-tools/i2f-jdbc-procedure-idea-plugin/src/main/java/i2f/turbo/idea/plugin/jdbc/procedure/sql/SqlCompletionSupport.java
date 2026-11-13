package i2f.turbo.idea.plugin.jdbc.procedure.sql;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.sql.psi.SqlBinaryExpression;
import com.intellij.sql.psi.SqlIdentifier;
import com.intellij.sql.psi.SqlParameter;
import com.intellij.sql.psi.SqlTokenType;
import i2f.turbo.idea.plugin.jdbc.procedure.XProc4jConsts;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;

import java.util.Set;

/**
 * SQL 参数补全实现。
 * <p>
 * 本类直接链接 com.intellij.sql.* 类型，只能由 {@link SqlPsiSupport} 在 database 插件可用时反射访问，
 * 其它类禁止静态引用本类，否则无 database 插件时会导致 NoClassDefFoundError。
 *
 * @author Ice2Faith
 * @desc SQL 注入片段内 ${} / #{} 变量补全
 */
public final class SqlCompletionSupport {

    private SqlCompletionSupport() {
    }

    public static boolean tryComplete(CompletionParameters parameters, PsiElement position, CompletionResultSet result) {
        ASTNode node = position.getNode();
        if (node == null) {
            return false;
        }
        IElementType type = node.getElementType();
        if (!(type instanceof SqlTokenType)) {
            return false;
        }
        SqlTokenType tokenType = (SqlTokenType) type;
        String name = tokenType.getDebugName();
//        log.warn("xml-sql-state: completionType=" + parameters.getCompletionType()
//                + ", isStopped=" + result.isStopped());
//        log.warn("xml-sql-param:" + tokenType.getDebugName() + "," + type.getClass() + ", [" + position.getText() + "]");
        if (!"SQL_IDENT".equalsIgnoreCase(name)) {
            return false;
        }
        PsiElement parent = position.getParent();
//        log.warn("xml-sql-parent:" + parent.getClass() + ", " + parent.getText());
        if (!(parent instanceof SqlParameter)) {
            return false;
        }
        // 控制遍历频率，避免CPU过高
        Set<String> completions = CompletionHelper.getXmlFileVariablesFast(position);
//        log.warn("xml-sql-param-completions:" + completions);
        if (completions == null || completions.isEmpty()) {
            return false;
        }
        boolean withPrefix = false;
        PsiElement pparent = parent.getParent();
        if (pparent != null) {
            if (pparent instanceof SqlIdentifier) {
                withPrefix = true;
            }
            if (pparent instanceof SqlBinaryExpression) {
                withPrefix = false;
            }
        }
        for (String attr : completions) {
            result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
            if (withPrefix) {
                result.addElement(LookupElementBuilder.create("${" + attr).withIcon(XProc4jConsts.ICON));
                result.addElement(LookupElementBuilder.create("#{" + attr).withIcon(XProc4jConsts.ICON));
                result.addElement(LookupElementBuilder.create("$!{" + attr).withIcon(XProc4jConsts.ICON));
                result.addElement(LookupElementBuilder.create("#!{" + attr).withIcon(XProc4jConsts.ICON));
            }
        }
        result.stopHere();
        return true;
    }
}
