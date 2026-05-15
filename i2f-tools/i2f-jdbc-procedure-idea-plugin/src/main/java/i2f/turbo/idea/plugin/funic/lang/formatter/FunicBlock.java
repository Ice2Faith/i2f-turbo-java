package i2f.turbo.idea.plugin.funic.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/5/12 22:08
 * @desc
 */
public class FunicBlock extends AbstractBlock {

    private final SpacingBuilder spacingBuilder;
    private final Indent indent;

    protected FunicBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                         SpacingBuilder spacingBuilder) {
        this(node, wrap, alignment, spacingBuilder, Indent.getNoneIndent());
    }

    protected FunicBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                         SpacingBuilder spacingBuilder, Indent indent) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.indent = indent;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        boolean isScriptBlock = myNode.getElementType() == FunicTypes.SCRIPT_BLOCK
                || myNode.getElementType() == FunicTypes.MAP_VALUE_EXPRESS
                || myNode.getElementType() == FunicTypes.LIST_VALUE_EXPRESS;
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE) {
                Indent childIndent;
                if (isScriptBlock) {
                    // { 和 } 不缩进，其余子元素缩进一层，和 Java 代码块逻辑一致
                    if (child.getElementType() == FunicTypes.TERM_CURLY_L
                            || child.getElementType() == FunicTypes.TERM_CURLY_R
                            || child.getElementType() == FunicTypes.TERM_BRACKET_SQUARE_L
                            || child.getElementType() == FunicTypes.TERM_BRACKET_SQUARE_R
                    ) {
                        childIndent = Indent.getNoneIndent();
                    } else {
                        childIndent = Indent.getNormalIndent();
                    }
                } else {
                    childIndent = Indent.getNoneIndent();
                }
                Block block = new FunicBlock(child, Wrap.createWrap(WrapType.NONE, false), null,
                        spacingBuilder, childIndent);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Override
    public Indent getIndent() {
        return indent;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

}
