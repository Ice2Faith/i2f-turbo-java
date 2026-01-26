package i2f.turbo.idea.plugin.tinyscript.lang.bracket;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/1/25 23:04
 * @desc
 */
public class TinyScriptPairedBraceMatcher  implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = new BracePair[]{
            new BracePair(TinyScriptTypes.TERM_BRACKET_SQUARE_L, TinyScriptTypes.TERM_BRACKET_SQUARE_R, false),
            new BracePair(TinyScriptTypes.TERM_PAREN_L, TinyScriptTypes.TERM_PAREN_R, false),
            new BracePair(TinyScriptTypes.TERM_CURLY_L, TinyScriptTypes.TERM_CURLY_L, true)  // true 表示是结构化块（如函数体、代码块）
    };

    @Override
    public @NotNull
    BracePair [] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType tokenType, @Nullable IElementType contextType) {
        // 允许在标识符、关键字、数字等前面有右括号（这是标准行为）
        return tokenType == TokenType.WHITE_SPACE
                || tokenType == TokenType.NEW_LINE_INDENT
                || tokenType == null;
    }

    @Override
    public int getCodeConstructStart(final com.intellij.psi.PsiFile file, int openingBraceOffset) {
        // 对于结构化括号（如 {}），IDE 可能用此方法定位代码块起始（如函数定义）
        // 默认返回原位置即可，除非你有特殊需求
        return openingBraceOffset;
    }
}