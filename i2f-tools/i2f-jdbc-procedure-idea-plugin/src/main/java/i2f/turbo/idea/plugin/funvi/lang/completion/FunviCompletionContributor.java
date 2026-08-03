package i2f.turbo.idea.plugin.funvi.lang.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funvi.FunviConsts;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviTokenType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/5/12 20:06
 * @desc
 */
public class FunviCompletionContributor extends CompletionContributor {
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        Project project = parameters.getOriginalFile().getProject();
        PsiElement position = parameters.getPosition();
        ASTNode node = position.getNode();
        if (node == null) {
            return;
        }

        IElementType type = node.getElementType();

        if (type instanceof FunviTokenType) {
            Set<String> completions = FunviConsts.KEYWORDS;
            if (completions != null && !completions.isEmpty()) {
                for (String attr : completions) {
                    LookupElement item = LookupElementBuilder.create(attr)
                            .withTypeText("Keywords")
                            .withIcon(FunviConsts.ICON)
                            .withItemTextItalic(true);
                    result.addElement(item);
                }
                return;
            }
        }
    }
}
