// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl.*;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlElementType;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlTokenType;

public interface OgnlTypes {

    IElementType ASSIGN_RIGHT_PART = new OgnlElementType("ASSIGN_RIGHT_PART");
    IElementType BIT_OPERATOR_PART = new OgnlElementType("BIT_OPERATOR_PART");
    IElementType CHAIN_SUB_EXPRESS = new OgnlElementType("CHAIN_SUB_EXPRESS");
    IElementType CIRCLE_EXPRESS = new OgnlElementType("CIRCLE_EXPRESS");
    IElementType COMMA_EXPRESS_RIGHT_PART = new OgnlElementType("COMMA_EXPRESS_RIGHT_PART");
    IElementType COMPARE_OPERATOR_PART = new OgnlElementType("COMPARE_OPERATOR_PART");
    IElementType CONST_BOOLEAN = new OgnlElementType("CONST_BOOLEAN");
    IElementType CONST_EXPRESS = new OgnlElementType("CONST_EXPRESS");
    IElementType CONST_FLOAT = new OgnlElementType("CONST_FLOAT");
    IElementType CONST_NULL = new OgnlElementType("CONST_NULL");
    IElementType CONST_NUMBER = new OgnlElementType("CONST_NUMBER");
    IElementType CONST_NUMERIC = new OgnlElementType("CONST_NUMERIC");
    IElementType CONST_STRING = new OgnlElementType("CONST_STRING");
    IElementType EVALUATE_EXPRESS = new OgnlElementType("EVALUATE_EXPRESS");
    IElementType EXPRESS = new OgnlElementType("EXPRESS");
    IElementType EXPRESS_SEGMENT = new OgnlElementType("EXPRESS_SEGMENT");
    IElementType FULL_NAME = new OgnlElementType("FULL_NAME");
    IElementType FUNCTION_ARGUMENTS = new OgnlElementType("FUNCTION_ARGUMENTS");
    IElementType FUNCTION_NAME = new OgnlElementType("FUNCTION_NAME");
    IElementType GLOBAL_FUNCTION_CALL = new OgnlElementType("GLOBAL_FUNCTION_CALL");
    IElementType INSTANCE_FUNCTION_CALL_RIGHT_PART = new OgnlElementType("INSTANCE_FUNCTION_CALL_RIGHT_PART");
    IElementType LIST_COLLECTION_EXPRESS = new OgnlElementType("LIST_COLLECTION_EXPRESS");
    IElementType LOGICAL_LINK_HIGH_OPERATOR_PART = new OgnlElementType("LOGICAL_LINK_HIGH_OPERATOR_PART");
    IElementType LOGICAL_LINK_LOW_OPERATOR_PART = new OgnlElementType("LOGICAL_LINK_LOW_OPERATOR_PART");
    IElementType MAP_COLLECTION_EXPRESS = new OgnlElementType("MAP_COLLECTION_EXPRESS");
    IElementType MAP_PAIR = new OgnlElementType("MAP_PAIR");
    IElementType MAP_PAIRS = new OgnlElementType("MAP_PAIRS");
    IElementType MATH_ADD_SUB_OPERATOR_PART = new OgnlElementType("MATH_ADD_SUB_OPERATOR_PART");
    IElementType MATH_MUL_DIV_OPERATOR_PART = new OgnlElementType("MATH_MUL_DIV_OPERATOR_PART");
    IElementType NEW_ARRAY_EXPRESS = new OgnlElementType("NEW_ARRAY_EXPRESS");
    IElementType NEW_INSTANCE_EXPRESS = new OgnlElementType("NEW_INSTANCE_EXPRESS");
    IElementType OPERATOR_SEGMENT = new OgnlElementType("OPERATOR_SEGMENT");
    IElementType PREFIX_OPERATOR_PART = new OgnlElementType("PREFIX_OPERATOR_PART");
    IElementType PROJECTING_ACROSS_COLLECTION_EXPRESS = new OgnlElementType("PROJECTING_ACROSS_COLLECTION_EXPRESS");
    IElementType PROPERTY_EXPRESS = new OgnlElementType("PROPERTY_EXPRESS");
    IElementType PSEUDO_LAMBDA_EXPRESS = new OgnlElementType("PSEUDO_LAMBDA_EXPRESS");
    IElementType REFERENCE_VARIABLE_EXPRESS = new OgnlElementType("REFERENCE_VARIABLE_EXPRESS");
    IElementType SCRIPT = new OgnlElementType("SCRIPT");
    IElementType SELECT_FROM_COLLECTION_EXPRESS = new OgnlElementType("SELECT_FROM_COLLECTION_EXPRESS");
    IElementType SELECT_FROM_COLLECTION_MODIFIER = new OgnlElementType("SELECT_FROM_COLLECTION_MODIFIER");
    IElementType SQUARE_EXPRESS = new OgnlElementType("SQUARE_EXPRESS");
    IElementType STATIC_FUNCTION_CALL = new OgnlElementType("STATIC_FUNCTION_CALL");
    IElementType STATIC_PROPERTY_EXPRESS = new OgnlElementType("STATIC_PROPERTY_EXPRESS");
    IElementType THIRD_OPERATE_RIGHT_PART = new OgnlElementType("THIRD_OPERATE_RIGHT_PART");
    IElementType TYPE_REFERENCE = new OgnlElementType("TYPE_REFERENCE");
    IElementType VARIABLE_EXPRESS = new OgnlElementType("VARIABLE_EXPRESS");

    IElementType IDENTIFIER = new OgnlTokenType("IDENTIFIER");
    IElementType KW_AND = new OgnlTokenType("and");
    IElementType KW_BAND = new OgnlTokenType("band");
    IElementType KW_BOR = new OgnlTokenType("bor");
    IElementType KW_CONST_BOOLEAN = new OgnlTokenType("KW_CONST_BOOLEAN");
    IElementType KW_CONST_NULL = new OgnlTokenType("null");
    IElementType KW_EQ = new OgnlTokenType("eq");
    IElementType KW_GT = new OgnlTokenType("gt");
    IElementType KW_GTE = new OgnlTokenType("gte");
    IElementType KW_IN = new OgnlTokenType("in");
    IElementType KW_INSTANCEOF = new OgnlTokenType("instanceof");
    IElementType KW_LT = new OgnlTokenType("lt");
    IElementType KW_LTE = new OgnlTokenType("lte");
    IElementType KW_NE = new OgnlTokenType("ne");
    IElementType KW_NEQ = new OgnlTokenType("neq");
    IElementType KW_NEW = new OgnlTokenType("new");
    IElementType KW_NOT = new OgnlTokenType("not");
    IElementType KW_OR = new OgnlTokenType("or");
    IElementType KW_SHL = new OgnlTokenType("shl");
    IElementType KW_SHR = new OgnlTokenType("shr");
    IElementType KW_USHR = new OgnlTokenType("ushr");
    IElementType KW_XOR = new OgnlTokenType("xor");
    IElementType OP_ADD = new OgnlTokenType("+");
    IElementType OP_AND = new OgnlTokenType("&&");
    IElementType OP_ASSIGN = new OgnlTokenType("=");
    IElementType OP_BIT_AND = new OgnlTokenType("&");
    IElementType OP_BIT_LMOV = new OgnlTokenType("<<");
    IElementType OP_BIT_OR = new OgnlTokenType("|");
    IElementType OP_BIT_REVERSE = new OgnlTokenType("~");
    IElementType OP_BIT_RMOV = new OgnlTokenType(">>");
    IElementType OP_BIT_RSMOV = new OgnlTokenType(">>>");
    IElementType OP_BIT_XOR = new OgnlTokenType("^");
    IElementType OP_DIV = new OgnlTokenType("/");
    IElementType OP_EQ = new OgnlTokenType("==");
    IElementType OP_EXCLAM = new OgnlTokenType("!");
    IElementType OP_GT = new OgnlTokenType(">");
    IElementType OP_GTE = new OgnlTokenType(">=");
    IElementType OP_LT = new OgnlTokenType("<");
    IElementType OP_LTE = new OgnlTokenType("<=");
    IElementType OP_MOD = new OgnlTokenType("%");
    IElementType OP_MUL = new OgnlTokenType("*");
    IElementType OP_NE = new OgnlTokenType("!=");
    IElementType OP_OR = new OgnlTokenType("||");
    IElementType OP_SUB = new OgnlTokenType("-");
    IElementType TERM_AT = new OgnlTokenType("@");
    IElementType TERM_BRACKET_SQUARE_L = new OgnlTokenType("[");
    IElementType TERM_BRACKET_SQUARE_R = new OgnlTokenType("]");
    IElementType TERM_COLON = new OgnlTokenType(":");
    IElementType TERM_COMMA = new OgnlTokenType(",");
    IElementType TERM_CONST_NUMBER = new OgnlTokenType("TERM_CONST_NUMBER");
    IElementType TERM_CONST_NUMBER_BIN = new OgnlTokenType("TERM_CONST_NUMBER_BIN");
    IElementType TERM_CONST_NUMBER_FLOAT = new OgnlTokenType("TERM_CONST_NUMBER_FLOAT");
    IElementType TERM_CONST_NUMBER_HEX = new OgnlTokenType("TERM_CONST_NUMBER_HEX");
    IElementType TERM_CONST_NUMBER_OTC = new OgnlTokenType("TERM_CONST_NUMBER_OTC");
    IElementType TERM_CONST_NUMBER_SCIEN_1 = new OgnlTokenType("TERM_CONST_NUMBER_SCIEN_1");
    IElementType TERM_CONST_NUMBER_SCIEN_2 = new OgnlTokenType("TERM_CONST_NUMBER_SCIEN_2");
    IElementType TERM_CONST_STRING = new OgnlTokenType("TERM_CONST_STRING");
    IElementType TERM_CONST_STRING_SINGLE = new OgnlTokenType("TERM_CONST_STRING_SINGLE");
    IElementType TERM_CURLY_L = new OgnlTokenType("{");
    IElementType TERM_CURLY_R = new OgnlTokenType("}");
    IElementType TERM_DOLLAR = new OgnlTokenType("$");
    IElementType TERM_DOT = new OgnlTokenType(".");
    IElementType TERM_PAREN_L = new OgnlTokenType("(");
    IElementType TERM_PAREN_R = new OgnlTokenType(")");
    IElementType TERM_QUESTION = new OgnlTokenType("?");
    IElementType TERM_SHARP = new OgnlTokenType("#");
    IElementType WORD = new OgnlTokenType("WORD");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == ASSIGN_RIGHT_PART) {
                return new OgnlAssignRightPartImpl(node);
            } else if (type == BIT_OPERATOR_PART) {
                return new OgnlBitOperatorPartImpl(node);
            } else if (type == CHAIN_SUB_EXPRESS) {
                return new OgnlChainSubExpressImpl(node);
            } else if (type == CIRCLE_EXPRESS) {
                return new OgnlCircleExpressImpl(node);
            } else if (type == COMMA_EXPRESS_RIGHT_PART) {
                return new OgnlCommaExpressRightPartImpl(node);
            } else if (type == COMPARE_OPERATOR_PART) {
                return new OgnlCompareOperatorPartImpl(node);
            } else if (type == CONST_BOOLEAN) {
                return new OgnlConstBooleanImpl(node);
            } else if (type == CONST_EXPRESS) {
                return new OgnlConstExpressImpl(node);
            } else if (type == CONST_FLOAT) {
                return new OgnlConstFloatImpl(node);
            } else if (type == CONST_NULL) {
                return new OgnlConstNullImpl(node);
            } else if (type == CONST_NUMBER) {
                return new OgnlConstNumberImpl(node);
            } else if (type == CONST_NUMERIC) {
                return new OgnlConstNumericImpl(node);
            } else if (type == CONST_STRING) {
                return new OgnlConstStringImpl(node);
            } else if (type == EVALUATE_EXPRESS) {
                return new OgnlEvaluateExpressImpl(node);
            } else if (type == EXPRESS) {
                return new OgnlExpressImpl(node);
            } else if (type == EXPRESS_SEGMENT) {
                return new OgnlExpressSegmentImpl(node);
            } else if (type == FULL_NAME) {
                return new OgnlFullNameImpl(node);
            } else if (type == FUNCTION_ARGUMENTS) {
                return new OgnlFunctionArgumentsImpl(node);
            } else if (type == FUNCTION_NAME) {
                return new OgnlFunctionNameImpl(node);
            } else if (type == GLOBAL_FUNCTION_CALL) {
                return new OgnlGlobalFunctionCallImpl(node);
            } else if (type == INSTANCE_FUNCTION_CALL_RIGHT_PART) {
                return new OgnlInstanceFunctionCallRightPartImpl(node);
            } else if (type == LIST_COLLECTION_EXPRESS) {
                return new OgnlListCollectionExpressImpl(node);
            } else if (type == LOGICAL_LINK_HIGH_OPERATOR_PART) {
                return new OgnlLogicalLinkHighOperatorPartImpl(node);
            } else if (type == LOGICAL_LINK_LOW_OPERATOR_PART) {
                return new OgnlLogicalLinkLowOperatorPartImpl(node);
            } else if (type == MAP_COLLECTION_EXPRESS) {
                return new OgnlMapCollectionExpressImpl(node);
            } else if (type == MAP_PAIR) {
                return new OgnlMapPairImpl(node);
            } else if (type == MAP_PAIRS) {
                return new OgnlMapPairsImpl(node);
            } else if (type == MATH_ADD_SUB_OPERATOR_PART) {
                return new OgnlMathAddSubOperatorPartImpl(node);
            } else if (type == MATH_MUL_DIV_OPERATOR_PART) {
                return new OgnlMathMulDivOperatorPartImpl(node);
            } else if (type == NEW_ARRAY_EXPRESS) {
                return new OgnlNewArrayExpressImpl(node);
            } else if (type == NEW_INSTANCE_EXPRESS) {
                return new OgnlNewInstanceExpressImpl(node);
            } else if (type == OPERATOR_SEGMENT) {
                return new OgnlOperatorSegmentImpl(node);
            } else if (type == PREFIX_OPERATOR_PART) {
                return new OgnlPrefixOperatorPartImpl(node);
            } else if (type == PROJECTING_ACROSS_COLLECTION_EXPRESS) {
                return new OgnlProjectingAcrossCollectionExpressImpl(node);
            } else if (type == PROPERTY_EXPRESS) {
                return new OgnlPropertyExpressImpl(node);
            } else if (type == PSEUDO_LAMBDA_EXPRESS) {
                return new OgnlPseudoLambdaExpressImpl(node);
            } else if (type == REFERENCE_VARIABLE_EXPRESS) {
                return new OgnlReferenceVariableExpressImpl(node);
            } else if (type == SCRIPT) {
                return new OgnlScriptImpl(node);
            } else if (type == SELECT_FROM_COLLECTION_EXPRESS) {
                return new OgnlSelectFromCollectionExpressImpl(node);
            } else if (type == SELECT_FROM_COLLECTION_MODIFIER) {
                return new OgnlSelectFromCollectionModifierImpl(node);
            } else if (type == SQUARE_EXPRESS) {
                return new OgnlSquareExpressImpl(node);
            } else if (type == STATIC_FUNCTION_CALL) {
                return new OgnlStaticFunctionCallImpl(node);
            } else if (type == STATIC_PROPERTY_EXPRESS) {
                return new OgnlStaticPropertyExpressImpl(node);
            } else if (type == THIRD_OPERATE_RIGHT_PART) {
                return new OgnlThirdOperateRightPartImpl(node);
            } else if (type == TYPE_REFERENCE) {
                return new OgnlTypeReferenceImpl(node);
            } else if (type == VARIABLE_EXPRESS) {
                return new OgnlVariableExpressImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
