package i2f.turbo.idea.plugin.funvi.lang.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviBlockBody;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviCommonBlock;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviIfBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/8/3 21:24
 * @desc
 */
public class FunviFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    public static final Logger log = Logger.getInstance(FunviFoldingBuilder.class);

    @Override
    public @NotNull FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
//        log.warn("funvi-01:"+root.getClass());
        List<FoldingDescriptor> list = new ArrayList<>();
        buildFoldRegionsNext(root, list);
        return list.toArray(new FoldingDescriptor[0]);
    }

    public void buildFoldRegionsNext(PsiElement root, @NotNull List<FoldingDescriptor> list) {
        if (root == null) {
            return;
        }
        for (PsiElement child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
            String placeholder = "";
            if(child instanceof FunviBlockBody){
                FunviBlockBody block = (FunviBlockBody) child;

                placeholder="...";
            }else if (child instanceof FunviIfBlock) {
                FunviIfBlock block = (FunviIfBlock) child;

                PsiElement curr = child.getFirstChild();
                for (int i = 0; i < 4 && curr != null; i++) {
                    placeholder += curr.getText();
                    curr = curr.getNextSibling();
                }
                placeholder += "...##";
            } else if (child instanceof FunviCommonBlock) {
                FunviCommonBlock block = (FunviCommonBlock) child;

                PsiElement curr = child.getFirstChild();
                for (int i = 0; i < 4 && curr != null; i++) {
                    placeholder += curr.getText();
                    curr = curr.getNextSibling();
                }
                placeholder += "...##";
            }

            buildFoldRegionsNext(child, list);

            if (!placeholder.isEmpty()) {
                ASTNode node = child.getNode();
                TextRange range = node.getTextRange();
                list.add(new FoldingDescriptor(
                        node,
                        range,
                        null,
                        placeholder,
                        false,
                        Collections.emptySet()
                ));
            }
        }

    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode astNode) {
        ASTNode child = astNode.getFirstChildNode();
        if (child == null) {
            return null;
        }
        IElementType tokenType = child.getElementType();
        if (tokenType.equals(FunviTypes.BLOCK_BODY)) {
            return "...";
        }else if (tokenType.equals(FunviTypes.IF_BLOCK)) {
            return "#if...##";
        } else if (tokenType.equals(FunviTypes.COMMON_BLOCK)) {
            String text = child.getFirstChildNode().getText();
            return text + "...##";
        }
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }
}
