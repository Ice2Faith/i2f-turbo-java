package i2f.turbo.idea.plugin.funic.lang.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicListValueExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicMapValueExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
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
public class FunicFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    public static final Logger log = Logger.getInstance(FunicFoldingBuilder.class);

    @Override
    public @NotNull FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
//        log.warn("funic-01:"+root.getClass());
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
            if (child instanceof FunicScriptBlock) {
                FunicScriptBlock block = (FunicScriptBlock) child;

                placeholder = "{...}";

            }else if (child instanceof FunicMapValueExpress) {
                FunicMapValueExpress block = (FunicMapValueExpress) child;

                placeholder = "{...}";

            }else if (child instanceof FunicListValueExpress) {
                FunicListValueExpress block = (FunicListValueExpress) child;

                placeholder = "[...]";

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
        if (tokenType.equals(FunicTypes.SCRIPT_BLOCK)) {
            return "{...}";
        }else if (tokenType.equals(FunicTypes.MAP_VALUE_EXPRESS)) {
            return "{...}";
        }else if (tokenType.equals(FunicTypes.LIST_VALUE_EXPRESS)) {
            return "[...]";
        }
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }
}
