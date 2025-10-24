package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionScope;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2025/10/23 16:01
 */
public class AnyHelpXmlCompletionContributor extends CompletionContributor {
    public static final Logger log = Logger.getInstance(AnyHelpXmlCompletionContributor.class);

    public static final String[] tinyScriptKeywords = {
            "null",
            "true",
            "false",
            "new",
            "class",
            "as", "cast",
            "is", "instanceof", "typeof",
            "in", "notin",
            "gte", "lte",
            "ne", "neq", "eq",
            "gt", "lt",
            "and", "or",
            "debugger",
            "try", "catch", "finally", "throw",
            "break", "continue", "return",
            "while", "for", "foreach",
            "if", "else", "elif"
    };

    public static final String[] sqlKeywords = {
            "select", "distinct", "top", "as",
            "case", "when", "then", "else", "end",
            "over", "partition", "partition by",
            "from", "join", "left", "left join",
            "right", "right join",
            "inner", "inner join",
            "outer", "outer join",
            "on", "and", "or",
            "where", "like", "exists",
            "in", "not", "not in",
            "null", "is null", "is not null", "not null",
            "group", "by", "group by",
            "having",
            "order", "order by", "asc", "desc",
            "limit", "offset",
            "with",
            "union", "all", "union all",
            "lead", "lag",
            "sum", "max", "min", "avg", "round", "instr", "substr", "trim",
            "count", "count(1)",
            "insert", "into", "insert into",
            "values",
            "update", "set",
            "delete", "delete from",
            "begin", "commit", "rollback",
            "create", "table", "view", "function", "procedure", "replace", "sequence", "index", "trigger",
            "create table",
            "primary", "key", "primary key",
            "auto_increment", "auto", "increment",
            "foreign", "foreign key", "references",
            "comment", "comment on",
            "column",
            "default", "unique",
            "drop", "drop table",
            "truncate", "truncate table",
            "alter", "alter table",
            "add", "remove",
            "grant", "revoke", "execute",
            "grant all",
            "int", "bigint", "varchar", "date", "datetime",
            "number",
            "nextval",
            "now()", "sysdate",
            "next", "rows", "fetch",
            "cursor",
            "exception",

    };

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        PsiElement position = parameters.getPosition();
//        log.warn("any-help completion-0:" + position.getClass());
        String text = position.getText();
//        log.warn("any-help completion-1:" + text);
        int offset = parameters.getOffset();
//        log.warn("any-help completion-2:" + offset);

        Set<CompletionScope> scopes = CompletionHelper.completionTypes(position);
        if (scopes.contains(CompletionScope.VARIABLES)) {
            Set<String> candidates = CompletionHelper.getXmlFileVariablesFast(position);
            if (candidates != null && !candidates.isEmpty()) {
                for (String candidate : candidates) {
                    LookupElement item = LookupElementBuilder.create(candidate)
                            .withTypeText("Variables")
                            .withIcon(XProc4jConsts.ICON)
                            .withItemTextItalic(true);
                    result.addElement(item);
                }
            }
        }
        if (scopes.contains(CompletionScope.FUNCTIONS)) {
            Map<String, LookupElement> candidates = CompletionHelper.getXmlFileFunctionsFast(position);
            if (candidates != null && !candidates.isEmpty()) {
                for (Map.Entry<String, LookupElement> candidate : candidates.entrySet()) {
                    LookupElement item = candidate.getValue();
                    if (item == null) {
                        continue;
                    }
                    result.addElement(item);
                }
            }
        }
        if (scopes.contains(CompletionScope.SQL_IDENTIFIER)) {
            Set<String> candidates = CompletionHelper.getXmlFileSqlIdentifiersFast(position);
            if (candidates != null && !candidates.isEmpty()) {
                for (String candidate : candidates) {
                    LookupElement item = LookupElementBuilder.create(candidate)
                            .withTypeText("Sql Identifiers")
                            .withIcon(XProc4jConsts.ICON)
                            .withItemTextItalic(true);
                    result.addElement(item);
                }
            }
        }
        if (scopes.contains(CompletionScope.SQL)) {
            String[] candidates = sqlKeywords;
            if (candidates != null) {
                for (String candidate : candidates) {
                    LookupElement item = LookupElementBuilder.create(candidate)
                            .withTypeText("Sql Keywords")
                            .withIcon(XProc4jConsts.ICON)
                            .withItemTextItalic(true);
                    result.addElement(item);
                }
            }
        }
        if (scopes.contains(CompletionScope.TINY_SCRIPT)) {
            String[] candidates = tinyScriptKeywords;
            if (candidates != null) {
                for (String candidate : candidates) {
                    LookupElement item = LookupElementBuilder.create(candidate)
                            .withTypeText("TinyScript Keywords")
                            .withIcon(XProc4jConsts.ICON)
                            .withItemTextItalic(true);
                    result.addElement(item);
                }
            }
        }

    }


}
