package i2f.turbo.idea.plugin.funic.lang.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes;
import i2f.turbo.idea.plugin.funic.lang.FunicAdapter;
import org.jetbrains.annotations.NotNull;

public class FunicSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("FUNIC_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("FUNIC_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("FUNIC_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey LINE_COMMENT =
            TextAttributesKey.createTextAttributesKey("FUNIC_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            TextAttributesKey.createTextAttributesKey("FUNIC_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey CLASS_REFERENCE =
            TextAttributesKey.createTextAttributesKey("FUNIC_CLASS_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey PAREN =
            TextAttributesKey.createTextAttributesKey("FUNIC_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey CURLY =
            TextAttributesKey.createTextAttributesKey("FUNIC_CURLY", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKET_SQUARE =
            TextAttributesKey.createTextAttributesKey("FUNIC_BRACKET_SQUARE", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey OPERATOR =
            TextAttributesKey.createTextAttributesKey("FUNIC_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey COMMA =
            TextAttributesKey.createTextAttributesKey("FUNIC_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey SEMICOLON =
            TextAttributesKey.createTextAttributesKey("FUNIC_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey DOT =
            TextAttributesKey.createTextAttributesKey("FUNIC_DOT", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("FUNIC__BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] LINE_COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
    private static final TextAttributesKey[] BLOCK_COMMENT_KEYS = new TextAttributesKey[]{BLOCK_COMMENT};
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
        return new FunicAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        // ID NAMING
        if (tokenType.equals(FunicTypes.KW_FUNC)
                || tokenType.equals(FunicTypes.KW_DEF)
                || tokenType.equals(FunicTypes.KW_TRY)
                || tokenType.equals(FunicTypes.KW_CATCH)
                || tokenType.equals(FunicTypes.KW_FINALLY)
                || tokenType.equals(FunicTypes.KW_THROW)
                || tokenType.equals(FunicTypes.KW_RETURN)
                || tokenType.equals(FunicTypes.KW_CONTINUE)
                || tokenType.equals(FunicTypes.KW_BREAK)
                || tokenType.equals(FunicTypes.KW_FOR)
                || tokenType.equals(FunicTypes.KW_DO)
                || tokenType.equals(FunicTypes.KW_WHILE)
                || tokenType.equals(FunicTypes.KW_IF)
                || tokenType.equals(FunicTypes.KW_ELIF)
                || tokenType.equals(FunicTypes.KW_ELSE)
                || tokenType.equals(FunicTypes.KW_AS)
                || tokenType.equals(FunicTypes.KW_NEW)
                || tokenType.equals(FunicTypes.KW_NOT)
                || tokenType.equals(FunicTypes.KW_TEQ)
                || tokenType.equals(FunicTypes.KW_TNEQ)
                || tokenType.equals(FunicTypes.KW_GTE)
                || tokenType.equals(FunicTypes.KW_LTE)
                || tokenType.equals(FunicTypes.KW_GT)
                || tokenType.equals(FunicTypes.KW_LT)
                || tokenType.equals(FunicTypes.KW_NEQ)
                || tokenType.equals(FunicTypes.KW_NE)
                || tokenType.equals(FunicTypes.KW_EQ)
                || tokenType.equals(FunicTypes.KW_IN)
                || tokenType.equals(FunicTypes.KW_INSTANCEOF)
                || tokenType.equals(FunicTypes.KW_IS)
                || tokenType.equals(FunicTypes.KW_AND)
                || tokenType.equals(FunicTypes.KW_OR)
                || tokenType.equals(FunicTypes.KW_GO)
                || tokenType.equals(FunicTypes.KW_SYNCHRONIZED)
                || tokenType.equals(FunicTypes.KW_IMPORT)
                || tokenType.equals(FunicTypes.KW_DEBUGGER)
        ) {
            return KEYWORD_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_COMMA)) {
            return COMMA_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_SEMICOLON)) {
            return SEMICOLON_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_DOT)) {
            return DOT_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_OPTION_DOT)
                || tokenType.equals(FunicTypes.TERM_COLON)
                || tokenType.equals(FunicTypes.TERM_QUESTION)
                || tokenType.equals(FunicTypes.TERM_SHARP)
                || tokenType.equals(FunicTypes.TERM_AT)
                || tokenType.equals(FunicTypes.OP_EXTRA)
                || tokenType.equals(FunicTypes.OP_EXTEND_TO)
                || tokenType.equals(FunicTypes.OP_RECV_FROM)
                || tokenType.equals(FunicTypes.OP_DOT_STAR)
                || tokenType.equals(FunicTypes.OP_DIAMOND_NAME_L)
                || tokenType.equals(FunicTypes.OP_DIAMOND_NAME_R)
                || tokenType.equals(FunicTypes.OP_MUL)
                || tokenType.equals(FunicTypes.OP_INT_DIV)
                || tokenType.equals(FunicTypes.OP_DIV)
                || tokenType.equals(FunicTypes.OP_MOD)
                || tokenType.equals(FunicTypes.OP_INCR)
                || tokenType.equals(FunicTypes.OP_DECR)
                || tokenType.equals(FunicTypes.OP_ADD)
                || tokenType.equals(FunicTypes.OP_SUB)
                || tokenType.equals(FunicTypes.OP_GTE)
                || tokenType.equals(FunicTypes.OP_LTE)
                || tokenType.equals(FunicTypes.OP_TNEQ)
                || tokenType.equals(FunicTypes.OP_NE)
                || tokenType.equals(FunicTypes.OP_NEQ)
                || tokenType.equals(FunicTypes.OP_TEQ)
                || tokenType.equals(FunicTypes.OP_EQ)
                || tokenType.equals(FunicTypes.OP_GT)
                || tokenType.equals(FunicTypes.OP_LT)
                || tokenType.equals(FunicTypes.OP_AND)
                || tokenType.equals(FunicTypes.OP_OR)
                || tokenType.equals(FunicTypes.OP_EXCLAM)
                || tokenType.equals(FunicTypes.OP_BIT_LMOV)
                || tokenType.equals(FunicTypes.OP_BIT_RSMOV)
                || tokenType.equals(FunicTypes.OP_BIT_RMOV)
                || tokenType.equals(FunicTypes.OP_BIT_XOR)
                || tokenType.equals(FunicTypes.OP_BIT_AND)
                || tokenType.equals(FunicTypes.OP_BIT_OR)
                || tokenType.equals(FunicTypes.OP_BIT_REVERSE)
                || tokenType.equals(FunicTypes.OP_ASSIGN)
                || tokenType.equals(FunicTypes.OP_ASSIGN_ADD)
                || tokenType.equals(FunicTypes.OP_ASSIGN_SUB)
                || tokenType.equals(FunicTypes.OP_ASSIGN_MUL)
                || tokenType.equals(FunicTypes.OP_ASSIGN_DIV)
                || tokenType.equals(FunicTypes.OP_ASSIGN_MOD)
                || tokenType.equals(FunicTypes.OP_ASSIGN_IFNULL)
                || tokenType.equals(FunicTypes.OP_ASSIGN_NOTNULL)
                || tokenType.equals(FunicTypes.OP_PIPELINE)
                || tokenType.equals(FunicTypes.OP_SELF_PIPE)) {
            return OPERATOR_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_CONST_NUMBER)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_BIN)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_FLOAT)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_HEX)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_OTC)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_SCIEN_1)
                || tokenType.equals(FunicTypes.TERM_CONST_NUMBER_SCIEN_2)
                || tokenType.equals(FunicTypes.KW_CONST_BOOLEAN)
                || tokenType.equals(FunicTypes.KW_CONST_NULL)
                || tokenType.equals(FunicTypes.TERM_CONST_VISITOR)
        ) {
            return NUMBER_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_CONST_STRING)
                || tokenType.equals(FunicTypes.TERM_CONST_STRING_MULTILINE)
                || tokenType.equals(FunicTypes.TERM_CONST_STRING_MULTILINE_QUOTE)
                || tokenType.equals(FunicTypes.TERM_CONST_STRING_RENDER)
                || tokenType.equals(FunicTypes.TERM_CONST_STRING_RENDER_SINGLE)
                || tokenType.equals(FunicTypes.TERM_CONST_STRING_SINGLE)) {
            return STRING_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_CONST_CLASS)) {
            return CLASS_REFERENCE_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_COMMENT_SINGLE_LINE)) {
            return LINE_COMMENT_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_COMMENT_MULTI_LINE)) {
            return BLOCK_COMMENT_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_BRACKET_SQUARE_L)
                || tokenType.equals(FunicTypes.TERM_BRACKET_SQUARE_R)
        ) {
            return BRACKET_SQUARE_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_PAREN_L)
                || tokenType.equals(FunicTypes.TERM_PAREN_R)
        ) {
            return PAREN_KEYS;
        }

        if (tokenType.equals(FunicTypes.TERM_CURLY_L)
                || tokenType.equals(FunicTypes.TERM_CURLY_R)
        ) {
            return CURLY_KEYS;
        }

        if (tokenType.equals(FunicTypes.WORD)
                || tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
