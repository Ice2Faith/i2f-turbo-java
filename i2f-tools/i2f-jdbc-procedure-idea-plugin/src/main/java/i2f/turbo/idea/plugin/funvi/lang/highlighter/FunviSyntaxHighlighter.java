package i2f.turbo.idea.plugin.funvi.lang.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes;
import i2f.turbo.idea.plugin.funvi.lang.FunviAdapter;
import org.jetbrains.annotations.NotNull;

public class FunviSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("FUNVI_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("FUNVI_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("FUNVI_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey PAREN =
            TextAttributesKey.createTextAttributesKey("FUNVI_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("FUNVI__BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PAREN};

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FunviAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        //
        if (tokenType.equals(FunviTypes.TERM_BLOCK_HEAD)
                || tokenType.equals(FunviTypes.TERM_BLOCK_IF)
                || tokenType.equals(FunviTypes.TERM_BLOCK_ELSE)
                || tokenType.equals(FunviTypes.TERM_BLOCK_END)
        ) {
            return KEYWORD_KEYS;
        }

        if (tokenType.equals(FunviTypes.TERM_REF_VALUE)) {
            return NUMBER_KEYS;
        }


        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
