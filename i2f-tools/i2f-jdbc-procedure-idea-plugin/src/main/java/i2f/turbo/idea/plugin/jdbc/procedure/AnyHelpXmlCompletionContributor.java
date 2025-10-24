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

    }


}
