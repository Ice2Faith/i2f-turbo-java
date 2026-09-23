// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;

import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.*;
import static i2f.turbo.idea.plugin.ognl.lang.parser.OgnlParserUtil.*;

import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class OgnlParser implements PsiParser, LightPsiParser {

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
    // OP_ASSIGN express
    public static boolean assignRightPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "assignRightPart")) return false;
        if (!nextTokenIs(b, OP_ASSIGN)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, OP_ASSIGN);
        r = r && express(b, l + 1);
        exit_section_(b, m, ASSIGN_RIGHT_PART, r);
        return r;
    }

    /* ********************************************************** */
    // OP_BIT_LMOV | KW_SHL | OP_BIT_RSMOV | KW_USHR | OP_BIT_RMOV | KW_SHR | OP_BIT_XOR | KW_XOR  | OP_BIT_AND | KW_BAND  | OP_BIT_OR | KW_BOR
    public static boolean bitOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "bitOperatorPart")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BIT_OPERATOR_PART, "<bit operator part>");
        r = consumeToken(b, OP_BIT_LMOV);
        if (!r) r = consumeToken(b, KW_SHL);
        if (!r) r = consumeToken(b, OP_BIT_RSMOV);
        if (!r) r = consumeToken(b, KW_USHR);
        if (!r) r = consumeToken(b, OP_BIT_RMOV);
        if (!r) r = consumeToken(b, KW_SHR);
        if (!r) r = consumeToken(b, OP_BIT_XOR);
        if (!r) r = consumeToken(b, KW_XOR);
        if (!r) r = consumeToken(b, OP_BIT_AND);
        if (!r) r = consumeToken(b, KW_BAND);
        if (!r) r = consumeToken(b, OP_BIT_OR);
        if (!r) r = consumeToken(b, KW_BOR);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_DOT TERM_PAREN_L express TERM_PAREN_R
    public static boolean chainSubExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "chainSubExpress")) return false;
        if (!nextTokenIs(b, TERM_DOT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_DOT, TERM_PAREN_L);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_PAREN_R);
        exit_section_(b, m, CHAIN_SUB_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_PAREN_L express TERM_PAREN_R
    public static boolean circleExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "circleExpress")) return false;
        if (!nextTokenIs(b, TERM_PAREN_L)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_PAREN_L);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_PAREN_R);
        exit_section_(b, m, CIRCLE_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_COMMA express
    public static boolean commaExpressRightPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "commaExpressRightPart")) return false;
        if (!nextTokenIs(b, TERM_COMMA)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_COMMA);
        r = r && express(b, l + 1);
        exit_section_(b, m, COMMA_EXPRESS_RIGHT_PART, r);
        return r;
    }

    /* ********************************************************** */
    // OP_GT | KW_GT | OP_GTE | KW_GTE | OP_LT | KW_LT | OP_LTE | KW_LTE | OP_EQ | KW_EQ | OP_NE | KW_NEQ | KW_IN | (KW_NOT KW_IN) | KW_INSTANCEOF
    public static boolean compareOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "compareOperatorPart")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, COMPARE_OPERATOR_PART, "<compare operator part>");
        r = consumeToken(b, OP_GT);
        if (!r) r = consumeToken(b, KW_GT);
        if (!r) r = consumeToken(b, OP_GTE);
        if (!r) r = consumeToken(b, KW_GTE);
        if (!r) r = consumeToken(b, OP_LT);
        if (!r) r = consumeToken(b, KW_LT);
        if (!r) r = consumeToken(b, OP_LTE);
        if (!r) r = consumeToken(b, KW_LTE);
        if (!r) r = consumeToken(b, OP_EQ);
        if (!r) r = consumeToken(b, KW_EQ);
        if (!r) r = consumeToken(b, OP_NE);
        if (!r) r = consumeToken(b, KW_NEQ);
        if (!r) r = consumeToken(b, KW_IN);
        if (!r) r = compareOperatorPart_13(b, l + 1);
        if (!r) r = consumeToken(b, KW_INSTANCEOF);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // KW_NOT KW_IN
    private static boolean compareOperatorPart_13(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "compareOperatorPart_13")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, KW_NOT, KW_IN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // KW_CONST_BOOLEAN
    public static boolean constBoolean(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constBoolean")) return false;
        if (!nextTokenIs(b, KW_CONST_BOOLEAN)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, KW_CONST_BOOLEAN);
        exit_section_(b, m, CONST_BOOLEAN, r);
        return r;
    }

    /* ********************************************************** */
    // constNull
    //     | constBoolean
    //     | constString
    //     | constNumeric
    public static boolean constExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constExpress")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONST_EXPRESS, "<const express>");
        r = constNull(b, l + 1);
        if (!r) r = constBoolean(b, l + 1);
        if (!r) r = constString(b, l + 1);
        if (!r) r = constNumeric(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_CONST_NUMBER_SCIEN_2
    //     |TERM_CONST_NUMBER_SCIEN_1
    //     |TERM_CONST_NUMBER_FLOAT
    public static boolean constFloat(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constFloat")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONST_FLOAT, "<const float>");
        r = consumeToken(b, TERM_CONST_NUMBER_SCIEN_2);
        if (!r) r = consumeToken(b, TERM_CONST_NUMBER_SCIEN_1);
        if (!r) r = consumeToken(b, TERM_CONST_NUMBER_FLOAT);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // KW_CONST_NULL
    public static boolean constNull(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constNull")) return false;
        if (!nextTokenIs(b, KW_CONST_NULL)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, KW_CONST_NULL);
        exit_section_(b, m, CONST_NULL, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_CONST_NUMBER_HEX
    //     | TERM_CONST_NUMBER_OTC
    //     | TERM_CONST_NUMBER_BIN
    //     | TERM_CONST_NUMBER
    public static boolean constNumber(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constNumber")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONST_NUMBER, "<const number>");
        r = consumeToken(b, TERM_CONST_NUMBER_HEX);
        if (!r) r = consumeToken(b, TERM_CONST_NUMBER_OTC);
        if (!r) r = consumeToken(b, TERM_CONST_NUMBER_BIN);
        if (!r) r = consumeToken(b, TERM_CONST_NUMBER);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // constFloat
    //      | constNumber
    public static boolean constNumeric(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constNumeric")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONST_NUMERIC, "<const numeric>");
        r = constFloat(b, l + 1);
        if (!r) r = constNumber(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_CONST_STRING
    //     | TERM_CONST_STRING_SINGLE
    public static boolean constString(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "constString")) return false;
        if (!nextTokenIs(b, "<const string>", TERM_CONST_STRING, TERM_CONST_STRING_SINGLE)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CONST_STRING, "<const string>");
        r = consumeToken(b, TERM_CONST_STRING);
        if (!r) r = consumeToken(b, TERM_CONST_STRING_SINGLE);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_SHARP IDENTIFIER TERM_PAREN_L express? TERM_PAREN_R
    public static boolean evaluateExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "evaluateExpress")) return false;
        if (!nextTokenIs(b, TERM_SHARP)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_SHARP, IDENTIFIER, TERM_PAREN_L);
        r = r && evaluateExpress_3(b, l + 1);
        r = r && consumeToken(b, TERM_PAREN_R);
        exit_section_(b, m, EVALUATE_EXPRESS, r);
        return r;
    }

    // express?
    private static boolean evaluateExpress_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "evaluateExpress_3")) return false;
        express(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // expressSegment (operatorSegment)*
    public static boolean express(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "express")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, EXPRESS, "<express>");
        r = expressSegment(b, l + 1);
        r = r && express_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (operatorSegment)*
    private static boolean express_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "express_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!express_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "express_1", c)) break;
        }
        return true;
    }

    // (operatorSegment)
    private static boolean express_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "express_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = operatorSegment(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // circleExpress // 括号表达式
    //     | newArrayExpress // 新建数组
    //     | newInstanceExpress // 新建实例对象
    //     | staticFunctionCall // 静态函数调用
    //     | staticPropertyExpress // 静态值获取
    //     | evaluateExpress
    //     | pseudoLambdaExpress
    //     | globalFunctionCall // 全局函数调用
    //     | prefixOperatorPart express // 前置表达式；高优先级，不能提取为子规则
    //     | listCollectionExpress
    //     | mapCollectionExpress
    //     | referenceVariableExpress
    //     | variableExpress
    //     | constExpress
    public static boolean expressSegment(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expressSegment")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, EXPRESS_SEGMENT, "<express segment>");
        r = circleExpress(b, l + 1);
        if (!r) r = newArrayExpress(b, l + 1);
        if (!r) r = newInstanceExpress(b, l + 1);
        if (!r) r = staticFunctionCall(b, l + 1);
        if (!r) r = staticPropertyExpress(b, l + 1);
        if (!r) r = evaluateExpress(b, l + 1);
        if (!r) r = pseudoLambdaExpress(b, l + 1);
        if (!r) r = globalFunctionCall(b, l + 1);
        if (!r) r = expressSegment_8(b, l + 1);
        if (!r) r = listCollectionExpress(b, l + 1);
        if (!r) r = mapCollectionExpress(b, l + 1);
        if (!r) r = referenceVariableExpress(b, l + 1);
        if (!r) r = variableExpress(b, l + 1);
        if (!r) r = constExpress(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // prefixOperatorPart express
    private static boolean expressSegment_8(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expressSegment_8")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = prefixOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER (TERM_DOT IDENTIFIER)*
    public static boolean fullName(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "fullName")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        r = r && fullName_1(b, l + 1);
        exit_section_(b, m, FULL_NAME, r);
        return r;
    }

    // (TERM_DOT IDENTIFIER)*
    private static boolean fullName_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "fullName_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!fullName_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "fullName_1", c)) break;
        }
        return true;
    }

    // TERM_DOT IDENTIFIER
    private static boolean fullName_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "fullName_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_DOT, IDENTIFIER);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_PAREN_L (express commaExpressRightPart*)? TERM_PAREN_R
    public static boolean functionArguments(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "functionArguments")) return false;
        if (!nextTokenIs(b, TERM_PAREN_L)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_PAREN_L);
        r = r && functionArguments_1(b, l + 1);
        r = r && consumeToken(b, TERM_PAREN_R);
        exit_section_(b, m, FUNCTION_ARGUMENTS, r);
        return r;
    }

    // (express commaExpressRightPart*)?
    private static boolean functionArguments_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "functionArguments_1")) return false;
        functionArguments_1_0(b, l + 1);
        return true;
    }

    // express commaExpressRightPart*
    private static boolean functionArguments_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "functionArguments_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = express(b, l + 1);
        r = r && functionArguments_1_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // commaExpressRightPart*
    private static boolean functionArguments_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "functionArguments_1_0_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!commaExpressRightPart(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "functionArguments_1_0_1", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean functionName(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "functionName")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, FUNCTION_NAME, r);
        return r;
    }

    /* ********************************************************** */
    // functionName functionArguments
    public static boolean globalFunctionCall(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "globalFunctionCall")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = functionName(b, l + 1);
        r = r && functionArguments(b, l + 1);
        exit_section_(b, m, GLOBAL_FUNCTION_CALL, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_DOT  functionName  functionArguments
    public static boolean instanceFunctionCallRightPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "instanceFunctionCallRightPart")) return false;
        if (!nextTokenIs(b, TERM_DOT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_DOT);
        r = r && functionName(b, l + 1);
        r = r && functionArguments(b, l + 1);
        exit_section_(b, m, INSTANCE_FUNCTION_CALL_RIGHT_PART, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_CURLY_L (express commaExpressRightPart*)? TERM_CURLY_R
    public static boolean listCollectionExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "listCollectionExpress")) return false;
        if (!nextTokenIs(b, TERM_CURLY_L)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_CURLY_L);
        r = r && listCollectionExpress_1(b, l + 1);
        r = r && consumeToken(b, TERM_CURLY_R);
        exit_section_(b, m, LIST_COLLECTION_EXPRESS, r);
        return r;
    }

    // (express commaExpressRightPart*)?
    private static boolean listCollectionExpress_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "listCollectionExpress_1")) return false;
        listCollectionExpress_1_0(b, l + 1);
        return true;
    }

    // express commaExpressRightPart*
    private static boolean listCollectionExpress_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "listCollectionExpress_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = express(b, l + 1);
        r = r && listCollectionExpress_1_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // commaExpressRightPart*
    private static boolean listCollectionExpress_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "listCollectionExpress_1_0_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!commaExpressRightPart(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "listCollectionExpress_1_0_1", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // OP_AND | KW_AND
    public static boolean logicalLinkHighOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "logicalLinkHighOperatorPart")) return false;
        if (!nextTokenIs(b, "<logical link high operator part>", KW_AND, OP_AND)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, LOGICAL_LINK_HIGH_OPERATOR_PART, "<logical link high operator part>");
        r = consumeToken(b, OP_AND);
        if (!r) r = consumeToken(b, KW_AND);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // OP_OR | KW_OR
    public static boolean logicalLinkLowOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "logicalLinkLowOperatorPart")) return false;
        if (!nextTokenIs(b, "<logical link low operator part>", KW_OR, OP_OR)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, LOGICAL_LINK_LOW_OPERATOR_PART, "<logical link low operator part>");
        r = consumeToken(b, OP_OR);
        if (!r) r = consumeToken(b, KW_OR);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_SHARP typeReference? TERM_CURLY_L mapPairs? TERM_CURLY_R
    public static boolean mapCollectionExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapCollectionExpress")) return false;
        if (!nextTokenIs(b, TERM_SHARP)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_SHARP);
        r = r && mapCollectionExpress_1(b, l + 1);
        r = r && consumeToken(b, TERM_CURLY_L);
        r = r && mapCollectionExpress_3(b, l + 1);
        r = r && consumeToken(b, TERM_CURLY_R);
        exit_section_(b, m, MAP_COLLECTION_EXPRESS, r);
        return r;
    }

    // typeReference?
    private static boolean mapCollectionExpress_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapCollectionExpress_1")) return false;
        typeReference(b, l + 1);
        return true;
    }

    // mapPairs?
    private static boolean mapCollectionExpress_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapCollectionExpress_3")) return false;
        mapPairs(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // (IDENTIFIER | constString) TERM_COLON express
    public static boolean mapPair(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapPair")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, MAP_PAIR, "<map pair>");
        r = mapPair_0(b, l + 1);
        r = r && consumeToken(b, TERM_COLON);
        r = r && express(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // IDENTIFIER | constString
    private static boolean mapPair_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapPair_0")) return false;
        boolean r;
        r = consumeToken(b, IDENTIFIER);
        if (!r) r = constString(b, l + 1);
        return r;
    }

    /* ********************************************************** */
    // mapPair (TERM_COMMA mapPair)*
    public static boolean mapPairs(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapPairs")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, MAP_PAIRS, "<map pairs>");
        r = mapPair(b, l + 1);
        r = r && mapPairs_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (TERM_COMMA mapPair)*
    private static boolean mapPairs_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapPairs_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!mapPairs_1_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "mapPairs_1", c)) break;
        }
        return true;
    }

    // TERM_COMMA mapPair
    private static boolean mapPairs_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mapPairs_1_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_COMMA);
        r = r && mapPair(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // OP_ADD | OP_SUB
    public static boolean mathAddSubOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mathAddSubOperatorPart")) return false;
        if (!nextTokenIs(b, "<math add sub operator part>", OP_ADD, OP_SUB)) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, MATH_ADD_SUB_OPERATOR_PART, "<math add sub operator part>");
        r = consumeToken(b, OP_ADD);
        if (!r) r = consumeToken(b, OP_SUB);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // OP_MUL | OP_DIV | OP_MOD
    public static boolean mathMulDivOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "mathMulDivOperatorPart")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, MATH_MUL_DIV_OPERATOR_PART, "<math mul div operator part>");
        r = consumeToken(b, OP_MUL);
        if (!r) r = consumeToken(b, OP_DIV);
        if (!r) r = consumeToken(b, OP_MOD);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // KW_NEW fullName TERM_BRACKET_SQUARE_L constNumber TERM_BRACKET_SQUARE_R (listCollectionExpress)?
    //     | KW_NEW fullName TERM_BRACKET_SQUARE_L  TERM_BRACKET_SQUARE_R (listCollectionExpress)
    public static boolean newArrayExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress")) return false;
        if (!nextTokenIs(b, KW_NEW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = newArrayExpress_0(b, l + 1);
        if (!r) r = newArrayExpress_1(b, l + 1);
        exit_section_(b, m, NEW_ARRAY_EXPRESS, r);
        return r;
    }

    // KW_NEW fullName TERM_BRACKET_SQUARE_L constNumber TERM_BRACKET_SQUARE_R (listCollectionExpress)?
    private static boolean newArrayExpress_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, KW_NEW);
        r = r && fullName(b, l + 1);
        r = r && consumeToken(b, TERM_BRACKET_SQUARE_L);
        r = r && constNumber(b, l + 1);
        r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
        r = r && newArrayExpress_0_5(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // (listCollectionExpress)?
    private static boolean newArrayExpress_0_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress_0_5")) return false;
        newArrayExpress_0_5_0(b, l + 1);
        return true;
    }

    // (listCollectionExpress)
    private static boolean newArrayExpress_0_5_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress_0_5_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = listCollectionExpress(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // KW_NEW fullName TERM_BRACKET_SQUARE_L  TERM_BRACKET_SQUARE_R (listCollectionExpress)
    private static boolean newArrayExpress_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, KW_NEW);
        r = r && fullName(b, l + 1);
        r = r && consumeTokens(b, 0, TERM_BRACKET_SQUARE_L, TERM_BRACKET_SQUARE_R);
        r = r && newArrayExpress_1_4(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // (listCollectionExpress)
    private static boolean newArrayExpress_1_4(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newArrayExpress_1_4")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = listCollectionExpress(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // KW_NEW fullName functionArguments
    public static boolean newInstanceExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "newInstanceExpress")) return false;
        if (!nextTokenIs(b, KW_NEW)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, KW_NEW);
        r = r && fullName(b, l + 1);
        r = r && functionArguments(b, l + 1);
        exit_section_(b, m, NEW_INSTANCE_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // chainSubExpress
    //     | projectingAcrossCollectionExpress
    //     | selectFromCollectionExpress
    //     | instanceFunctionCallRightPart // 实例函数调用
    //     | squareExpress
    //     | propertyExpress
    //     | mathMulDivOperatorPart express // 数学乘除运算；数学运算，需要从左到右，因此不能提取为子规则
    //     | mathAddSubOperatorPart express // 数学加减运算；数学运算，需要从左到右，因此不能提取为子规则
    //     | compareOperatorPart express // 比较运算符
    //     | bitOperatorPart  express // 位运算符号；数学运算，需要从左到右，因此不能提取为子规则
    //     | logicalLinkHighOperatorPart express // 逻辑连接符
    //     | logicalLinkLowOperatorPart express // 逻辑连接符
    //     | thirdOperateRightPart // 三目运算符
    //     | assignRightPart
    //     | commaExpressRightPart+
    public static boolean operatorSegment(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, OPERATOR_SEGMENT, "<operator segment>");
        r = chainSubExpress(b, l + 1);
        if (!r) r = projectingAcrossCollectionExpress(b, l + 1);
        if (!r) r = selectFromCollectionExpress(b, l + 1);
        if (!r) r = instanceFunctionCallRightPart(b, l + 1);
        if (!r) r = squareExpress(b, l + 1);
        if (!r) r = propertyExpress(b, l + 1);
        if (!r) r = operatorSegment_6(b, l + 1);
        if (!r) r = operatorSegment_7(b, l + 1);
        if (!r) r = operatorSegment_8(b, l + 1);
        if (!r) r = operatorSegment_9(b, l + 1);
        if (!r) r = operatorSegment_10(b, l + 1);
        if (!r) r = operatorSegment_11(b, l + 1);
        if (!r) r = thirdOperateRightPart(b, l + 1);
        if (!r) r = assignRightPart(b, l + 1);
        if (!r) r = operatorSegment_14(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // mathMulDivOperatorPart express
    private static boolean operatorSegment_6(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_6")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = mathMulDivOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // mathAddSubOperatorPart express
    private static boolean operatorSegment_7(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_7")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = mathAddSubOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // compareOperatorPart express
    private static boolean operatorSegment_8(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_8")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = compareOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // bitOperatorPart  express
    private static boolean operatorSegment_9(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_9")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = bitOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // logicalLinkHighOperatorPart express
    private static boolean operatorSegment_10(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_10")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = logicalLinkHighOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // logicalLinkLowOperatorPart express
    private static boolean operatorSegment_11(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_11")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = logicalLinkLowOperatorPart(b, l + 1);
        r = r && express(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // commaExpressRightPart+
    private static boolean operatorSegment_14(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "operatorSegment_14")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = commaExpressRightPart(b, l + 1);
        while (r) {
            int c = current_position_(b);
            if (!commaExpressRightPart(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "operatorSegment_14", c)) break;
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // OP_EXCLAM | KW_NOT | OP_BIT_REVERSE | OP_SUB | OP_ADD
    public static boolean prefixOperatorPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "prefixOperatorPart")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, PREFIX_OPERATOR_PART, "<prefix operator part>");
        r = consumeToken(b, OP_EXCLAM);
        if (!r) r = consumeToken(b, KW_NOT);
        if (!r) r = consumeToken(b, OP_BIT_REVERSE);
        if (!r) r = consumeToken(b, OP_SUB);
        if (!r) r = consumeToken(b, OP_ADD);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_DOT TERM_CURLY_L express TERM_CURLY_R
    public static boolean projectingAcrossCollectionExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "projectingAcrossCollectionExpress")) return false;
        if (!nextTokenIs(b, TERM_DOT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_DOT, TERM_CURLY_L);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_CURLY_R);
        exit_section_(b, m, PROJECTING_ACROSS_COLLECTION_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_DOT IDENTIFIER
    public static boolean propertyExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "propertyExpress")) return false;
        if (!nextTokenIs(b, TERM_DOT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_DOT, IDENTIFIER);
        exit_section_(b, m, PROPERTY_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_COLON TERM_BRACKET_SQUARE_L express TERM_BRACKET_SQUARE_R
    public static boolean pseudoLambdaExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "pseudoLambdaExpress")) return false;
        if (!nextTokenIs(b, TERM_COLON)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_COLON, TERM_BRACKET_SQUARE_L);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
        exit_section_(b, m, PSEUDO_LAMBDA_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_SHARP IDENTIFIER
    public static boolean referenceVariableExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "referenceVariableExpress")) return false;
        if (!nextTokenIs(b, TERM_SHARP)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_SHARP, IDENTIFIER);
        exit_section_(b, m, REFERENCE_VARIABLE_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // script
    static boolean root(PsiBuilder b, int l) {
        return script(b, l + 1);
    }

    /* ********************************************************** */
    // express <<eof>>
    public static boolean script(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "script")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SCRIPT, "<script>");
        r = express(b, l + 1);
        r = r && eof(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_DOT TERM_CURLY_L selectFromCollectionModifier express TERM_CURLY_R
    public static boolean selectFromCollectionExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "selectFromCollectionExpress")) return false;
        if (!nextTokenIs(b, TERM_DOT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, TERM_DOT, TERM_CURLY_L);
        r = r && selectFromCollectionModifier(b, l + 1);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_CURLY_R);
        exit_section_(b, m, SELECT_FROM_COLLECTION_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_QUESTION | OP_BIT_XOR | TERM_DOLLAR
    public static boolean selectFromCollectionModifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "selectFromCollectionModifier")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SELECT_FROM_COLLECTION_MODIFIER, "<select from collection modifier>");
        r = consumeToken(b, TERM_QUESTION);
        if (!r) r = consumeToken(b, OP_BIT_XOR);
        if (!r) r = consumeToken(b, TERM_DOLLAR);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TERM_BRACKET_SQUARE_L express TERM_BRACKET_SQUARE_R
    public static boolean squareExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "squareExpress")) return false;
        if (!nextTokenIs(b, TERM_BRACKET_SQUARE_L)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_BRACKET_SQUARE_L);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
        exit_section_(b, m, SQUARE_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // typeReference globalFunctionCall
    public static boolean staticFunctionCall(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "staticFunctionCall")) return false;
        if (!nextTokenIs(b, TERM_AT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = typeReference(b, l + 1);
        r = r && globalFunctionCall(b, l + 1);
        exit_section_(b, m, STATIC_FUNCTION_CALL, r);
        return r;
    }

    /* ********************************************************** */
    // typeReference IDENTIFIER
    public static boolean staticPropertyExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "staticPropertyExpress")) return false;
        if (!nextTokenIs(b, TERM_AT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = typeReference(b, l + 1);
        r = r && consumeToken(b, IDENTIFIER);
        exit_section_(b, m, STATIC_PROPERTY_EXPRESS, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_QUESTION express TERM_COLON express
    public static boolean thirdOperateRightPart(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "thirdOperateRightPart")) return false;
        if (!nextTokenIs(b, TERM_QUESTION)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_QUESTION);
        r = r && express(b, l + 1);
        r = r && consumeToken(b, TERM_COLON);
        r = r && express(b, l + 1);
        exit_section_(b, m, THIRD_OPERATE_RIGHT_PART, r);
        return r;
    }

    /* ********************************************************** */
    // TERM_AT fullName TERM_AT
    public static boolean typeReference(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "typeReference")) return false;
        if (!nextTokenIs(b, TERM_AT)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, TERM_AT);
        r = r && fullName(b, l + 1);
        r = r && consumeToken(b, TERM_AT);
        exit_section_(b, m, TYPE_REFERENCE, r);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean variableExpress(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "variableExpress")) return false;
        if (!nextTokenIs(b, IDENTIFIER)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, VARIABLE_EXPRESS, r);
        return r;
    }

}
