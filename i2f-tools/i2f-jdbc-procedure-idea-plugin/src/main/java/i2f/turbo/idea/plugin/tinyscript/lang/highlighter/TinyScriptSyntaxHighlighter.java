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
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey COMMENT =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey CLASS_REFERENCE =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_CLASS_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey PAREN =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT_PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("TINY_SCRIPT__BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] CLASS_REFERENCE_KEYS = new TextAttributesKey[]{CLASS_REFERENCE};
    private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PAREN};

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
        // ID NAMING REF_EXPRESS
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
                || tokenType.equals(TinyScriptTypes.OP_ADD)
                || tokenType.equals(TinyScriptTypes.OP_AND)
                || tokenType.equals(TinyScriptTypes.OP_AND_STR)
                || tokenType.equals(TinyScriptTypes.OP_AS)
                || tokenType.equals(TinyScriptTypes.OP_ASSIGN)
                || tokenType.equals(TinyScriptTypes.OP_VERTICAL_BAR)
                || tokenType.equals(TinyScriptTypes.OP_CAST)
                || tokenType.equals(TinyScriptTypes.OP_DIV)
                || tokenType.equals(TinyScriptTypes.OP_EQ)
                || tokenType.equals(TinyScriptTypes.OP_EQ_STR)
                || tokenType.equals(TinyScriptTypes.OP_EXCLAM)
                || tokenType.equals(TinyScriptTypes.OP_GT)
                || tokenType.equals(TinyScriptTypes.OP_GTE)
                || tokenType.equals(TinyScriptTypes.OP_GTE_STR)
                || tokenType.equals(TinyScriptTypes.OP_GT_STR)
                || tokenType.equals(TinyScriptTypes.OP_IN)
                || tokenType.equals(TinyScriptTypes.OP_INSTANCE_OF)
                || tokenType.equals(TinyScriptTypes.OP_IS)
                || tokenType.equals(TinyScriptTypes.OP_LT)
                || tokenType.equals(TinyScriptTypes.OP_LTE)
                || tokenType.equals(TinyScriptTypes.OP_LTE_STR)
                || tokenType.equals(TinyScriptTypes.OP_LT_STR)
                || tokenType.equals(TinyScriptTypes.OP_MOD)
                || tokenType.equals(TinyScriptTypes.OP_MUL)
                || tokenType.equals(TinyScriptTypes.OP_NE)
                || tokenType.equals(TinyScriptTypes.OP_NEQ)
                || tokenType.equals(TinyScriptTypes.OP_NEQ_STR)
                || tokenType.equals(TinyScriptTypes.OP_NE_STR)
                || tokenType.equals(TinyScriptTypes.OP_NOT)
                || tokenType.equals(TinyScriptTypes.OP_NOT_IN)
                || tokenType.equals(TinyScriptTypes.OP_OR)
                || tokenType.equals(TinyScriptTypes.OP_OR_STR)
                || tokenType.equals(TinyScriptTypes.OP_SUB)
                || tokenType.equals(TinyScriptTypes.OP_TYPE_OF)
        ) {
            return KEYWORD_KEYS;
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

        if (tokenType.equals(TinyScriptTypes.TERM_COMMENT_MULTI_LINE)
                || tokenType.equals(TinyScriptTypes.TERM_COMMENT_SINGLE_LINE)) {
            return COMMENT_KEYS;
        }

        if(tokenType.equals(TinyScriptTypes.TERM_BRACKET_SQUARE_L)
        ||tokenType.equals(TinyScriptTypes.TERM_BRACKET_SQUARE_R)
                ||tokenType.equals(TinyScriptTypes.TERM_COLON)
                ||tokenType.equals(TinyScriptTypes.TERM_COMMA)
                ||tokenType.equals(TinyScriptTypes.TERM_CURLY_L)
                ||tokenType.equals(TinyScriptTypes.TERM_CURLY_R)
                ||tokenType.equals(TinyScriptTypes.TERM_DOLLAR)
                ||tokenType.equals(TinyScriptTypes.TERM_DOT)
                ||tokenType.equals(TinyScriptTypes.TERM_PAREN_L)
                ||tokenType.equals(TinyScriptTypes.TERM_PAREN_R)
                ||tokenType.equals(TinyScriptTypes.TERM_QUESTION)
                ||tokenType.equals(TinyScriptTypes.TERM_SEMICOLON)
                ||tokenType.equals(TinyScriptTypes.TERM_SHARP)
        ){
            return PAREN_KEYS;
        }


        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
