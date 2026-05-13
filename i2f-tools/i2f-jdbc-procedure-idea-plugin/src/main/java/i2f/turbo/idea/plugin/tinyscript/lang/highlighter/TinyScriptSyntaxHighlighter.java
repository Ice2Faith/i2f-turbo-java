package i2f.turbo.idea.plugin.tinyscript.lang.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import i2f.turbo.idea.plugin.tinyscript.lang.TinyScriptAdapter;
import org.jetbrains.annotations.NotNull;

public class TinyScriptSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey LINE_COMMENT =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey CLASS_REFERENCE =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_CLASS_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey PAREN =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey CURLY =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_CURLY", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKET_SQUARE =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_BRACKET_SQUARE", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey OPERATOR =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey COMMA =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey SEMICOLON =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey DOT =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT_DOT", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("TINYSCRIPT__BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


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
        return new TinyScriptAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        // ID NAMING
        if (tokenType.equals(TinyScriptTypes.KEY_BREAK)
                || tokenType.equals(TinyScriptTypes.KEY_CATCH)
                || tokenType.equals(TinyScriptTypes.KEY_CLASS)
                || tokenType.equals(TinyScriptTypes.KEY_CONTINUE)
                || tokenType.equals(TinyScriptTypes.KEY_DEBUGGER)
                || tokenType.equals(TinyScriptTypes.KEY_ELIF)
                || tokenType.equals(TinyScriptTypes.KEY_ELSE)
                || tokenType.equals(TinyScriptTypes.KEY_FINALLY)
                || tokenType.equals(TinyScriptTypes.KEY_FOR)
                || tokenType.equals(TinyScriptTypes.KEY_FOREACH)
                || tokenType.equals(TinyScriptTypes.KEY_IF)
                || tokenType.equals(TinyScriptTypes.KEY_NEW)
                || tokenType.equals(TinyScriptTypes.KEY_RETURN)
                || tokenType.equals(TinyScriptTypes.KEY_THROW)
                || tokenType.equals(TinyScriptTypes.KEY_TRY)
                || tokenType.equals(TinyScriptTypes.KEY_WHILE)
                || tokenType.equals(TinyScriptTypes.KEY_DO)
                || tokenType.equals(TinyScriptTypes.KEY_FUNC)

                || tokenType.equals(TinyScriptTypes.KEY_AND)
                || tokenType.equals(TinyScriptTypes.KEY_EQ)
                || tokenType.equals(TinyScriptTypes.KEY_GTE)
                || tokenType.equals(TinyScriptTypes.KEY_GT)
                || tokenType.equals(TinyScriptTypes.KEY_LTE)
                || tokenType.equals(TinyScriptTypes.KEY_LT)
                || tokenType.equals(TinyScriptTypes.KEY_NEQ)
                || tokenType.equals(TinyScriptTypes.KEY_NE)
                || tokenType.equals(TinyScriptTypes.KEY_OR)

                || tokenType.equals(TinyScriptTypes.KEY_AS)
                || tokenType.equals(TinyScriptTypes.KEY_IN)
                || tokenType.equals(TinyScriptTypes.KEY_CAST)
                || tokenType.equals(TinyScriptTypes.KEY_INSTANCE_OF)
                || tokenType.equals(TinyScriptTypes.KEY_IS)
                || tokenType.equals(TinyScriptTypes.KEY_NOT)
                || tokenType.equals(TinyScriptTypes.KEY_NOT_IN)
                || tokenType.equals(TinyScriptTypes.KEY_TYPE_OF)
        ) {
            return KEYWORD_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_COMMA)) {
            return COMMA_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_SEMICOLON)) {
            return SEMICOLON_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_DOT)) {
            return DOT_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_COLON)
                || tokenType.equals(TinyScriptTypes.TERM_DOLLAR)
                || tokenType.equals(TinyScriptTypes.TERM_QUESTION)
                || tokenType.equals(TinyScriptTypes.OP_ADD)
                || tokenType.equals(TinyScriptTypes.OP_AND)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_ADD)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_SUB)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_MUL)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_DIV)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_MOD)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_IFNULL)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN_NOTNULL)
                || tokenType.equals(TinyScriptTypes.OP_VERTICAL_BAR)
                || tokenType.equals(TinyScriptTypes.OP_DIV)
                || tokenType.equals(TinyScriptTypes.OP_EQ)
                || tokenType.equals(TinyScriptTypes.OP_EXCLAM)
                || tokenType.equals(TinyScriptTypes.OP_GT)
                || tokenType.equals(TinyScriptTypes.OP_GTE)
                || tokenType.equals(TinyScriptTypes.OP_LT)
                || tokenType.equals(TinyScriptTypes.OP_LTE)
                || tokenType.equals(TinyScriptTypes.OP_MOD)
                || tokenType.equals(TinyScriptTypes.OP_MUL)
                || tokenType.equals(TinyScriptTypes.OP_NE)
                || tokenType.equals(TinyScriptTypes.OP_NEQ)
                || tokenType.equals(TinyScriptTypes.OP_OR)
                || tokenType.equals(TinyScriptTypes.OP_SUB)
                || tokenType.equals(TinyScriptTypes.OP_PIPELINE)
                || tokenType.equals(TinyScriptTypes.OP_SELF_PIPE)
                || tokenType.equals(TinyScriptTypes.TERM_SHARP)
                || tokenType.equals(TinyScriptTypes.TERM_AT)) {
            return OPERATOR_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_BIN)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_FLOAT)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_HEX)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_OTC)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_SCIEN_1)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NUMBER_SCIEN_2)
                || tokenType.equals(TinyScriptTypes.TERM_INTEGER)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_BOOLEAN)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_NULL)
                || tokenType.equals(TinyScriptTypes.REF_EXPRESS)
                || tokenType.equals(TinyScriptTypes.TERM_DIGIT)
        ) {
            return NUMBER_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_CONST_STRING)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_STRING_MULTILINE)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_STRING_MULTILINE_QUOTE)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_STRING_RENDER)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_STRING_RENDER_SINGLE)
                || tokenType.equals(TinyScriptTypes.TERM_CONST_STRING_SINGLE)) {
            return STRING_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_CONST_TYPE_CLASS)) {
            return CLASS_REFERENCE_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_COMMENT_SINGLE_LINE)) {
            return LINE_COMMENT_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_COMMENT_MULTI_LINE)) {
            return BLOCK_COMMENT_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_BRACKET_SQUARE_L)
                || tokenType.equals(TinyScriptTypes.TERM_BRACKET_SQUARE_R)
        ) {
            return BRACKET_SQUARE_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_PAREN_L)
                || tokenType.equals(TinyScriptTypes.TERM_PAREN_R)
        ) {
            return PAREN_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.TERM_CURLY_L)
                || tokenType.equals(TinyScriptTypes.TERM_CURLY_R)
        ) {
            return CURLY_KEYS;
        }

        if (tokenType.equals(TinyScriptTypes.WORD)
                || tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
