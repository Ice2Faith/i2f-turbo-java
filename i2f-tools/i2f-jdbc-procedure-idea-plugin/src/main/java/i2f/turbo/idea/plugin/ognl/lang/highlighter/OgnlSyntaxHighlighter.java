package i2f.turbo.idea.plugin.ognl.lang.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes;
import i2f.turbo.idea.plugin.ognl.lang.OgnlAdapter;
import org.jetbrains.annotations.NotNull;

public class OgnlSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("OGNL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("OGNL_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("OGNL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey CLASS_REFERENCE =
            TextAttributesKey.createTextAttributesKey("OGNL_CLASS_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey PAREN =
            TextAttributesKey.createTextAttributesKey("OGNL_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey CURLY =
            TextAttributesKey.createTextAttributesKey("OGNL_CURLY", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKET_SQUARE =
            TextAttributesKey.createTextAttributesKey("OGNL_BRACKET_SQUARE", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey OPERATOR =
            TextAttributesKey.createTextAttributesKey("OGNL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey COMMA =
            TextAttributesKey.createTextAttributesKey("OGNL_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey SEMICOLON =
            TextAttributesKey.createTextAttributesKey("OGNL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey DOT =
            TextAttributesKey.createTextAttributesKey("OGNL_DOT", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("OGNL__BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] CLASS_REFERENCE_KEYS = new TextAttributesKey[]{CLASS_REFERENCE};
    private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PAREN};
    private static final TextAttributesKey[] CURLY_KEYS = new TextAttributesKey[]{CURLY};
    private static final TextAttributesKey[] BRACKET_SQUARE_KEYS = new TextAttributesKey[]{BRACKET_SQUARE};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{SEMICOLON};
    private static final TextAttributesKey[] DOT_KEYS = new TextAttributesKey[]{DOT};

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new OgnlAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        // ID NAMING
        if (tokenType.equals(OgnlTypes.KW_NEW)
                || tokenType.equals(OgnlTypes.KW_NOT)
                || tokenType.equals(OgnlTypes.KW_GTE)
                || tokenType.equals(OgnlTypes.KW_LTE)
                || tokenType.equals(OgnlTypes.KW_GT)
                || tokenType.equals(OgnlTypes.KW_LT)
                || tokenType.equals(OgnlTypes.KW_NEQ)
                || tokenType.equals(OgnlTypes.KW_NE)
                || tokenType.equals(OgnlTypes.KW_EQ)
                || tokenType.equals(OgnlTypes.KW_IN)
                || tokenType.equals(OgnlTypes.KW_INSTANCEOF)
                || tokenType.equals(OgnlTypes.KW_AND)
                || tokenType.equals(OgnlTypes.KW_OR)
        ) {
            return KEYWORD_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_COMMA)) {
            return COMMA_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_DOT)) {
            return DOT_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_COLON)
                || tokenType.equals(OgnlTypes.TERM_QUESTION)
                || tokenType.equals(OgnlTypes.TERM_SHARP)
                || tokenType.equals(OgnlTypes.TERM_AT)
                || tokenType.equals(OgnlTypes.OP_MUL)
                || tokenType.equals(OgnlTypes.OP_DIV)
                || tokenType.equals(OgnlTypes.OP_MOD)
                || tokenType.equals(OgnlTypes.OP_ADD)
                || tokenType.equals(OgnlTypes.OP_SUB)
                || tokenType.equals(OgnlTypes.OP_GTE)
                || tokenType.equals(OgnlTypes.OP_LTE)
                || tokenType.equals(OgnlTypes.OP_NE)
                || tokenType.equals(OgnlTypes.OP_EQ)
                || tokenType.equals(OgnlTypes.OP_GT)
                || tokenType.equals(OgnlTypes.OP_LT)
                || tokenType.equals(OgnlTypes.OP_AND)
                || tokenType.equals(OgnlTypes.OP_OR)
                || tokenType.equals(OgnlTypes.OP_EXCLAM)
                || tokenType.equals(OgnlTypes.OP_BIT_LMOV)
                || tokenType.equals(OgnlTypes.OP_BIT_RSMOV)
                || tokenType.equals(OgnlTypes.OP_BIT_RMOV)
                || tokenType.equals(OgnlTypes.OP_BIT_XOR)
                || tokenType.equals(OgnlTypes.OP_BIT_AND)
                || tokenType.equals(OgnlTypes.OP_BIT_OR)
                || tokenType.equals(OgnlTypes.OP_BIT_REVERSE)
                || tokenType.equals(OgnlTypes.OP_ASSIGN)) {
            return OPERATOR_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_CONST_NUMBER)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_BIN)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_FLOAT)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_HEX)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_OTC)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_SCIEN_1)
                || tokenType.equals(OgnlTypes.TERM_CONST_NUMBER_SCIEN_2)
                || tokenType.equals(OgnlTypes.KW_CONST_BOOLEAN)
                || tokenType.equals(OgnlTypes.KW_CONST_NULL)
        ) {
            return NUMBER_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_CONST_STRING)
                || tokenType.equals(OgnlTypes.TERM_CONST_STRING_SINGLE)) {
            return STRING_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_BRACKET_SQUARE_L)
                || tokenType.equals(OgnlTypes.TERM_BRACKET_SQUARE_R)
        ) {
            return BRACKET_SQUARE_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_PAREN_L)
                || tokenType.equals(OgnlTypes.TERM_PAREN_R)
        ) {
            return PAREN_KEYS;
        }

        if (tokenType.equals(OgnlTypes.TERM_CURLY_L)
                || tokenType.equals(OgnlTypes.TERM_CURLY_R)
        ) {
            return CURLY_KEYS;
        }

        if (tokenType.equals(OgnlTypes.WORD)
                || tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
