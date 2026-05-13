package i2f.turbo.idea.plugin.tinyscript.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/5/12 22:08
 * @desc
 */
public class TinyScriptBlock extends AbstractBlock {

    private final SpacingBuilder spacingBuilder;
    private final Indent indent;

    protected TinyScriptBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                              SpacingBuilder spacingBuilder) {
        this(node, wrap, alignment, spacingBuilder, Indent.getNoneIndent());
    }

    protected TinyScriptBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                              SpacingBuilder spacingBuilder, Indent indent) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.indent = indent;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        boolean isScriptBlock = myNode.getElementType() == TinyScriptTypes.SCRIPT_BLOCK
                || myNode.getElementType() == TinyScriptTypes.JSON_MAP_VALUE
                || myNode.getElementType() == TinyScriptTypes.JSON_ARRAY_VALUE;
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE) {
                Indent childIndent;
                if (isScriptBlock) {
                    // { 和 } 不缩进，其余子元素缩进一层，和 Java 代码块逻辑一致
                    if (child.getElementType() == TinyScriptTypes.TERM_CURLY_L
                            || child.getElementType() == TinyScriptTypes.TERM_CURLY_R
                            || child.getElementType() == TinyScriptTypes.TERM_BRACKET_SQUARE_L
                            || child.getElementType() == TinyScriptTypes.TERM_BRACKET_SQUARE_R
                    ) {
                        childIndent = Indent.getNoneIndent();
                    } else {
                        childIndent = Indent.getNormalIndent();
                    }
                } else {
                    childIndent = Indent.getNoneIndent();
                }
                Block block = new TinyScriptBlock(child, Wrap.createWrap(WrapType.NONE, false), null,
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
