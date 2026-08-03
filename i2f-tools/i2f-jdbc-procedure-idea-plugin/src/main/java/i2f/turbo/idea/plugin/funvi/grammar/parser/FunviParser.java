// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.*;
import static i2f.turbo.idea.plugin.funvi.lang.parser.FunviParserUtil.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class FunviParser implements PsiParser, LightPsiParser {

    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, null);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        r = parse_root_(t, b);
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b) {
        return parse_root_(t, b, 0);
    }

    static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return root(b, l + 1);
    }

    /* ********************************************************** */
    // ifBlock // 单独处理 if 多分支块
    //     | commonBlock
    public static boolean block(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "block")) return false;
        if (!nextTokenIs(b, "<block>", TERM_BLOCK_HEAD, TERM_BLOCK_IF)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BLOCK, "<block>");
        r = ifBlock(b, l + 1);
        if (!r) r = commonBlock(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // segment*
    public static boolean blockBody(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "blockBody")) return false;
        Marker m = enter_section_(b, l, _NONE_, BLOCK_BODY, "<block body>");
        while (true) {
            int c = current_position_(b);
            if (!segment(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "blockBody", c)) break;
        }
        exit_section_(b, l, m, true, false, null);
        return true;
    }

    /* ********************************************************** */
    // TERM_BLOCK_HEAD
    public static boolean blockHeadSegment(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "blockHeadSegment")) return false;
        if (!nextTokenIs(b, TERM_BLOCK_HEAD)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_BLOCK_HEAD);
        exit_section_(b, m, BLOCK_HEAD_SEGMENT, r);
        return r;
    }

    /* ********************************************************** */
    // blockHeadSegment OP_PAREN_L parameters? OP_PAREN_R (blockBody TERM_BLOCK_END)?
    public static boolean commonBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "commonBlock")) return false;
        if (!nextTokenIs(b, TERM_BLOCK_HEAD)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = blockHeadSegment(b, l + 1);
        r = r && consumeToken(b, OP_PAREN_L);
        r = r && commonBlock_2(b, l + 1);
        r = r && consumeToken(b, OP_PAREN_R);
        r = r && commonBlock_4(b, l + 1);
        exit_section_(b, m, COMMON_BLOCK, r);
        return r;
    }

    // parameters?
    private static boolean commonBlock_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "commonBlock_2")) return false;
        parameters(b, l + 1);
        return true;
    }

    // (blockBody TERM_BLOCK_END)?
    private static boolean commonBlock_4(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "commonBlock_4")) return false;
        commonBlock_4_0(b, l + 1);
        return true;
    }

    // blockBody TERM_BLOCK_END
    private static boolean commonBlock_4_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "commonBlock_4_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = blockBody(b, l + 1);
        r = r && consumeToken(b, TERM_BLOCK_END);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_IDENTIFIER
    //     |TERM_TEXT
    //     |TERM_WHITESPACE
    //     | OP_COMMA
    //     | OP_PAREN_L
    //     | OP_PAREN_R
    //     | OP_DOLLAR
    //     | OP_SHARP
    //     | OP_EXCLAIM
    //     | OP_COLON
    public static boolean content(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "content")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONTENT, "<content>");
        r = consumeToken(b, TERM_IDENTIFIER);
        if (!r) r = consumeToken(b, TERM_TEXT);
        if (!r) r = consumeToken(b, TERM_WHITESPACE);
        if (!r) r = consumeToken(b, OP_COMMA);
        if (!r) r = consumeToken(b, OP_PAREN_L);
        if (!r) r = consumeToken(b, OP_PAREN_R);
        if (!r) r = consumeToken(b, OP_DOLLAR);
        if (!r) r = consumeToken(b, OP_SHARP);
        if (!r) r = consumeToken(b, OP_EXCLAIM);
        if (!r) r = consumeToken(b, OP_COLON);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_BLOCK_ELSE OP_PAREN_L parameters? OP_PAREN_R blockBody
    public static boolean elseBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "elseBlock")) return false;
        if (!nextTokenIs(b, TERM_BLOCK_ELSE)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_BLOCK_ELSE, OP_PAREN_L);
        r = r && elseBlock_2(b, l + 1);
        r = r && consumeToken(b, OP_PAREN_R);
        r = r && blockBody(b, l + 1);
        exit_section_(b, m, ELSE_BLOCK, r);
        return r;
    }

    // parameters?
    private static boolean elseBlock_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "elseBlock_2")) return false;
        parameters(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // TERM_BLOCK_IF OP_PAREN_L parameters? OP_PAREN_R blockBody elseBlock* TERM_BLOCK_END
    public static boolean ifBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ifBlock")) return false;
        if (!nextTokenIs(b, TERM_BLOCK_IF)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_BLOCK_IF, OP_PAREN_L);
        r = r && ifBlock_2(b, l + 1);
        r = r && consumeToken(b, OP_PAREN_R);
        r = r && blockBody(b, l + 1);
        r = r && ifBlock_5(b, l + 1);
        r = r && consumeToken(b, TERM_BLOCK_END);
        exit_section_(b, m, IF_BLOCK, r);
        return r;
    }

    // parameters?
    private static boolean ifBlock_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ifBlock_2")) return false;
        parameters(b, l + 1);
        return true;
    }

    // elseBlock*
    private static boolean ifBlock_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ifBlock_5")) return false;
        while (true) {
            int c = current_position_(b);
            if (!elseBlock(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "ifBlock_5", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // (TERM_IDENTIFIER OP_COLON)? (TERM_REF_VALUE | TERM_IDENTIFIER)
    public static boolean parameter(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameter")) return false;
        if (!nextTokenIs(b, "<parameter>", TERM_IDENTIFIER, TERM_REF_VALUE)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
        r = parameter_0(b, l + 1);
        r = r && parameter_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (TERM_IDENTIFIER OP_COLON)?
    private static boolean parameter_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameter_0")) return false;
        parameter_0_0(b, l + 1);
        return true;
    }

    // TERM_IDENTIFIER OP_COLON
    private static boolean parameter_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameter_0_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_IDENTIFIER, OP_COLON);
        exit_section_(b, m, null, r);
        return r;
    }

    // TERM_REF_VALUE | TERM_IDENTIFIER
    private static boolean parameter_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameter_1")) return false;
        boolean r;
        r = consumeToken(b, TERM_REF_VALUE);
        if (!r) r = consumeToken(b, TERM_IDENTIFIER);
        return r;
    }

    /* ********************************************************** */
    // parameter (OP_COMMA parameter)*
    public static boolean parameters(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameters")) return false;
        if (!nextTokenIs(b, "<parameters>", TERM_IDENTIFIER, TERM_REF_VALUE)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, PARAMETERS, "<parameters>");
        r = parameter(b, l + 1);
        r = r && parameters_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (OP_COMMA parameter)*
    private static boolean parameters_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameters_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!parameters_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "parameters_1", c)) break;
        }
        return true;
    }

    // OP_COMMA parameter
    private static boolean parameters_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parameters_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, OP_COMMA);
        r = r && parameter(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // segment* <<eof>>
    static boolean root(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "root")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = root_0(b, l + 1);
        r = r && eof(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // segment*
    private static boolean root_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "root_0")) return false;
        while (true) {
            int c = current_position_(b);
            if (!segment(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "root_0", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // block
    //     | value
    //     | textSegment
    public static boolean segment(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "segment")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SEGMENT, "<segment>");
        r = block(b, l + 1);
        if (!r) r = value(b, l + 1);
        if (!r) r = textSegment(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // content+
    public static boolean textSegment(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "textSegment")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TEXT_SEGMENT, "<text segment>");
        r = content(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!content(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "textSegment", c)) break;
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_REF_VALUE
    public static boolean value(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value")) return false;
        if (!nextTokenIs(b, TERM_REF_VALUE)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_REF_VALUE);
        exit_section_(b, m, VALUE, r);
        return r;
    }

}
