// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptElementType;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptTokenType;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl.*;

public interface TinyScriptTypes {

  IElementType ARGUMENT = new TinyScriptElementType("ARGUMENT");
  IElementType ARGUMENT_LIST = new TinyScriptElementType("ARGUMENT_LIST");
  IElementType ARGUMENT_VALUE = new TinyScriptElementType("ARGUMENT_VALUE");
  IElementType BIN_NUMBER = new TinyScriptElementType("BIN_NUMBER");
  IElementType CATCH_BODY_BLOCK = new TinyScriptElementType("CATCH_BODY_BLOCK");
  IElementType CLASS_NAME_BLOCK = new TinyScriptElementType("CLASS_NAME_BLOCK");
  IElementType COMMENT_SEGMENT = new TinyScriptElementType("COMMENT_SEGMENT");
  IElementType CONDITION_BLOCK = new TinyScriptElementType("CONDITION_BLOCK");
  IElementType CONST_BOOL = new TinyScriptElementType("CONST_BOOL");
  IElementType CONST_CLASS = new TinyScriptElementType("CONST_CLASS");
  IElementType CONST_MULTILINE_STRING = new TinyScriptElementType("CONST_MULTILINE_STRING");
  IElementType CONST_NULL = new TinyScriptElementType("CONST_NULL");
  IElementType CONST_RENDER_STRING = new TinyScriptElementType("CONST_RENDER_STRING");
  IElementType CONST_STRING = new TinyScriptElementType("CONST_STRING");
  IElementType CONST_VALUE = new TinyScriptElementType("CONST_VALUE");
  IElementType CONTROL_SEGMENT = new TinyScriptElementType("CONTROL_SEGMENT");
  IElementType DEBUGGER_SEGMENT = new TinyScriptElementType("DEBUGGER_SEGMENT");
  IElementType DEC_NUMBER = new TinyScriptElementType("DEC_NUMBER");
  IElementType EQUAL_VALUE = new TinyScriptElementType("EQUAL_VALUE");
  IElementType EXPRESS = new TinyScriptElementType("EXPRESS");
  IElementType EXPRESS_SEGMENT = new TinyScriptElementType("EXPRESS_SEGMENT");
  IElementType EXTRACT_EXPRESS = new TinyScriptElementType("EXTRACT_EXPRESS");
  IElementType EXTRACT_PAIR = new TinyScriptElementType("EXTRACT_PAIR");
  IElementType EXTRACT_PAIRS = new TinyScriptElementType("EXTRACT_PAIRS");
  IElementType FINALLY_BODY_BLOCK = new TinyScriptElementType("FINALLY_BODY_BLOCK");
  IElementType FOREACH_SEGMENT = new TinyScriptElementType("FOREACH_SEGMENT");
  IElementType FOR_SEGMENT = new TinyScriptElementType("FOR_SEGMENT");
  IElementType FUNCTION_CALL = new TinyScriptElementType("FUNCTION_CALL");
  IElementType HEX_NUMBER = new TinyScriptElementType("HEX_NUMBER");
  IElementType IF_SEGMENT = new TinyScriptElementType("IF_SEGMENT");
  IElementType INVOKE_FUNCTION = new TinyScriptElementType("INVOKE_FUNCTION");
  IElementType JSON_ARRAY_VALUE = new TinyScriptElementType("JSON_ARRAY_VALUE");
  IElementType JSON_ITEM_LIST = new TinyScriptElementType("JSON_ITEM_LIST");
  IElementType JSON_MAP_VALUE = new TinyScriptElementType("JSON_MAP_VALUE");
  IElementType JSON_PAIR = new TinyScriptElementType("JSON_PAIR");
  IElementType JSON_PAIRS = new TinyScriptElementType("JSON_PAIRS");
  IElementType JSON_VALUE = new TinyScriptElementType("JSON_VALUE");
  IElementType NAMING_BLOCK = new TinyScriptElementType("NAMING_BLOCK");
  IElementType NEGTIVE_SEGMENT = new TinyScriptElementType("NEGTIVE_SEGMENT");
  IElementType NEW_INSTANCE = new TinyScriptElementType("NEW_INSTANCE");
  IElementType OPERATOR_SEGMENT = new TinyScriptElementType("OPERATOR_SEGMENT");
  IElementType OTC_NUMBER = new TinyScriptElementType("OTC_NUMBER");
  IElementType PAREN_SEGMENT = new TinyScriptElementType("PAREN_SEGMENT");
  IElementType PREFIX_OPERATOR_SEGMENT = new TinyScriptElementType("PREFIX_OPERATOR_SEGMENT");
  IElementType REF_CALL = new TinyScriptElementType("REF_CALL");
  IElementType REF_VALUE = new TinyScriptElementType("REF_VALUE");
  IElementType SCRIPT = new TinyScriptElementType("SCRIPT");
  IElementType SCRIPT_BLOCK = new TinyScriptElementType("SCRIPT_BLOCK");
  IElementType SEGMENT = new TinyScriptElementType("SEGMENT");
  IElementType THROW_SEGMENT = new TinyScriptElementType("THROW_SEGMENT");
  IElementType TRY_BODY_BLOCK = new TinyScriptElementType("TRY_BODY_BLOCK");
  IElementType TRY_SEGMENT = new TinyScriptElementType("TRY_SEGMENT");
  IElementType WHILE_SEGMENT = new TinyScriptElementType("WHILE_SEGMENT");

  IElementType ID = new TinyScriptTokenType("ID");
  IElementType KEY_BREAK = new TinyScriptTokenType("break");
  IElementType KEY_CATCH = new TinyScriptTokenType("catch");
  IElementType KEY_CLASS = new TinyScriptTokenType("class");
  IElementType KEY_CONTINUE = new TinyScriptTokenType("continue");
  IElementType KEY_DEBUGGER = new TinyScriptTokenType("debugger");
  IElementType KEY_ELIF = new TinyScriptTokenType("KEY_ELIF");
  IElementType KEY_ELSE = new TinyScriptTokenType("else");
  IElementType KEY_FINALLY = new TinyScriptTokenType("finally");
  IElementType KEY_FOR = new TinyScriptTokenType("for");
  IElementType KEY_FOREACH = new TinyScriptTokenType("foreach");
  IElementType KEY_IF = new TinyScriptTokenType("if");
  IElementType KEY_NEW = new TinyScriptTokenType("new");
  IElementType KEY_RETURN = new TinyScriptTokenType("return");
  IElementType KEY_THROW = new TinyScriptTokenType("throw");
  IElementType KEY_TRY = new TinyScriptTokenType("try");
  IElementType KEY_WHILE = new TinyScriptTokenType("while");
  IElementType NAMING = new TinyScriptTokenType("NAMING");
  IElementType OP_ADD = new TinyScriptTokenType("+");
  IElementType OP_AND = new TinyScriptTokenType("&&");
  IElementType OP_AND_STR = new TinyScriptTokenType("and");
  IElementType OP_AS = new TinyScriptTokenType("as");
  IElementType OP_ASSIGN = new TinyScriptTokenType("=");
  IElementType OP_ASSIGN_ADD = new TinyScriptTokenType("+=");
  IElementType OP_ASSIGN_DIV = new TinyScriptTokenType("/=");
  IElementType OP_ASSIGN_MOD = new TinyScriptTokenType("%=");
  IElementType OP_ASSIGN_MUL = new TinyScriptTokenType("*=");
  IElementType OP_ASSIGN_SUB = new TinyScriptTokenType("-=");
  IElementType OP_CAST = new TinyScriptTokenType("cast");
  IElementType OP_DIV = new TinyScriptTokenType("/");
  IElementType OP_EQ = new TinyScriptTokenType("==");
  IElementType OP_EQ_STR = new TinyScriptTokenType("eq");
  IElementType OP_EXCLAM = new TinyScriptTokenType("!");
  IElementType OP_GT = new TinyScriptTokenType(">");
  IElementType OP_GTE = new TinyScriptTokenType(">=");
  IElementType OP_GTE_STR = new TinyScriptTokenType("gte");
  IElementType OP_GT_STR = new TinyScriptTokenType("gt");
  IElementType OP_IN = new TinyScriptTokenType("in");
  IElementType OP_INSTANCE_OF = new TinyScriptTokenType("instanceof");
  IElementType OP_IS = new TinyScriptTokenType("is");
  IElementType OP_LT = new TinyScriptTokenType("<");
  IElementType OP_LTE = new TinyScriptTokenType("<=");
  IElementType OP_LTE_STR = new TinyScriptTokenType("lte");
  IElementType OP_LT_STR = new TinyScriptTokenType("lt");
  IElementType OP_MOD = new TinyScriptTokenType("%");
  IElementType OP_MUL = new TinyScriptTokenType("*");
  IElementType OP_NE = new TinyScriptTokenType("!=");
  IElementType OP_NEQ = new TinyScriptTokenType("<>");
  IElementType OP_NEQ_STR = new TinyScriptTokenType("neq");
  IElementType OP_NE_STR = new TinyScriptTokenType("ne");
  IElementType OP_NOT = new TinyScriptTokenType("not");
  IElementType OP_NOT_IN = new TinyScriptTokenType("notin");
  IElementType OP_OR = new TinyScriptTokenType("||");
  IElementType OP_OR_STR = new TinyScriptTokenType("or");
  IElementType OP_SUB = new TinyScriptTokenType("-");
  IElementType OP_TYPE_OF = new TinyScriptTokenType("typeof");
  IElementType OP_VERTICAL_BAR = new TinyScriptTokenType("|");
  IElementType REF_EXPRESS = new TinyScriptTokenType("REF_EXPRESS");
  IElementType ROUTE_NAMING = new TinyScriptTokenType("ROUTE_NAMING");
  IElementType TERM_BRACKET_SQUARE_L = new TinyScriptTokenType("[");
  IElementType TERM_BRACKET_SQUARE_R = new TinyScriptTokenType("]");
  IElementType TERM_COLON = new TinyScriptTokenType(":");
  IElementType TERM_COMMA = new TinyScriptTokenType(",");
  IElementType TERM_COMMENT_MULTI_LINE = new TinyScriptTokenType("TERM_COMMENT_MULTI_LINE");
  IElementType TERM_COMMENT_SINGLE_LINE = new TinyScriptTokenType("TERM_COMMENT_SINGLE_LINE");
  IElementType TERM_CONST_BOOLEAN = new TinyScriptTokenType("TERM_CONST_BOOLEAN");
  IElementType TERM_CONST_NULL = new TinyScriptTokenType("null");
  IElementType TERM_CONST_NUMBER = new TinyScriptTokenType("TERM_CONST_NUMBER");
  IElementType TERM_CONST_NUMBER_BIN = new TinyScriptTokenType("TERM_CONST_NUMBER_BIN");
  IElementType TERM_CONST_NUMBER_FLOAT = new TinyScriptTokenType("TERM_CONST_NUMBER_FLOAT");
  IElementType TERM_CONST_NUMBER_HEX = new TinyScriptTokenType("TERM_CONST_NUMBER_HEX");
  IElementType TERM_CONST_NUMBER_OTC = new TinyScriptTokenType("TERM_CONST_NUMBER_OTC");
  IElementType TERM_CONST_NUMBER_SCIEN_1 = new TinyScriptTokenType("TERM_CONST_NUMBER_SCIEN_1");
  IElementType TERM_CONST_NUMBER_SCIEN_2 = new TinyScriptTokenType("TERM_CONST_NUMBER_SCIEN_2");
  IElementType TERM_CONST_STRING = new TinyScriptTokenType("TERM_CONST_STRING");
  IElementType TERM_CONST_STRING_MULTILINE = new TinyScriptTokenType("TERM_CONST_STRING_MULTILINE");
  IElementType TERM_CONST_STRING_MULTILINE_QUOTE = new TinyScriptTokenType("TERM_CONST_STRING_MULTILINE_QUOTE");
  IElementType TERM_CONST_STRING_RENDER = new TinyScriptTokenType("TERM_CONST_STRING_RENDER");
  IElementType TERM_CONST_STRING_RENDER_SINGLE = new TinyScriptTokenType("TERM_CONST_STRING_RENDER_SINGLE");
  IElementType TERM_CONST_STRING_SINGLE = new TinyScriptTokenType("TERM_CONST_STRING_SINGLE");
  IElementType TERM_CONST_TYPE_CLASS = new TinyScriptTokenType("TERM_CONST_TYPE_CLASS");
  IElementType TERM_CURLY_L = new TinyScriptTokenType("{");
  IElementType TERM_CURLY_R = new TinyScriptTokenType("}");
  IElementType TERM_DIGIT = new TinyScriptTokenType("TERM_DIGIT");
  IElementType TERM_DOLLAR = new TinyScriptTokenType("$");
  IElementType TERM_DOT = new TinyScriptTokenType(".");
  IElementType TERM_INTEGER = new TinyScriptTokenType("TERM_INTEGER");
  IElementType TERM_PAREN_L = new TinyScriptTokenType("(");
  IElementType TERM_PAREN_R = new TinyScriptTokenType(")");
  IElementType TERM_QUESTION = new TinyScriptTokenType("?");
  IElementType TERM_SEMICOLON = new TinyScriptTokenType(";");
  IElementType TERM_SHARP = new TinyScriptTokenType("#");
  IElementType WORD = new TinyScriptTokenType("WORD");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARGUMENT) {
        return new TinyScriptArgumentImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new TinyScriptArgumentListImpl(node);
      }
      else if (type == ARGUMENT_VALUE) {
        return new TinyScriptArgumentValueImpl(node);
      }
      else if (type == BIN_NUMBER) {
        return new TinyScriptBinNumberImpl(node);
      }
      else if (type == CATCH_BODY_BLOCK) {
        return new TinyScriptCatchBodyBlockImpl(node);
      }
      else if (type == CLASS_NAME_BLOCK) {
        return new TinyScriptClassNameBlockImpl(node);
      }
      else if (type == COMMENT_SEGMENT) {
        return new TinyScriptCommentSegmentImpl(node);
      }
      else if (type == CONDITION_BLOCK) {
        return new TinyScriptConditionBlockImpl(node);
      }
      else if (type == CONST_BOOL) {
        return new TinyScriptConstBoolImpl(node);
      }
      else if (type == CONST_CLASS) {
        return new TinyScriptConstClassImpl(node);
      }
      else if (type == CONST_MULTILINE_STRING) {
        return new TinyScriptConstMultilineStringImpl(node);
      }
      else if (type == CONST_NULL) {
        return new TinyScriptConstNullImpl(node);
      }
      else if (type == CONST_RENDER_STRING) {
        return new TinyScriptConstRenderStringImpl(node);
      }
      else if (type == CONST_STRING) {
        return new TinyScriptConstStringImpl(node);
      }
      else if (type == CONST_VALUE) {
        return new TinyScriptConstValueImpl(node);
      }
      else if (type == CONTROL_SEGMENT) {
        return new TinyScriptControlSegmentImpl(node);
      }
      else if (type == DEBUGGER_SEGMENT) {
        return new TinyScriptDebuggerSegmentImpl(node);
      }
      else if (type == DEC_NUMBER) {
        return new TinyScriptDecNumberImpl(node);
      }
      else if (type == EQUAL_VALUE) {
        return new TinyScriptEqualValueImpl(node);
      }
      else if (type == EXPRESS) {
        return new TinyScriptExpressImpl(node);
      }
      else if (type == EXPRESS_SEGMENT) {
        return new TinyScriptExpressSegmentImpl(node);
      }
      else if (type == EXTRACT_EXPRESS) {
        return new TinyScriptExtractExpressImpl(node);
      }
      else if (type == EXTRACT_PAIR) {
        return new TinyScriptExtractPairImpl(node);
      }
      else if (type == EXTRACT_PAIRS) {
        return new TinyScriptExtractPairsImpl(node);
      }
      else if (type == FINALLY_BODY_BLOCK) {
        return new TinyScriptFinallyBodyBlockImpl(node);
      }
      else if (type == FOREACH_SEGMENT) {
        return new TinyScriptForeachSegmentImpl(node);
      }
      else if (type == FOR_SEGMENT) {
        return new TinyScriptForSegmentImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new TinyScriptFunctionCallImpl(node);
      }
      else if (type == HEX_NUMBER) {
        return new TinyScriptHexNumberImpl(node);
      }
      else if (type == IF_SEGMENT) {
        return new TinyScriptIfSegmentImpl(node);
      }
      else if (type == INVOKE_FUNCTION) {
        return new TinyScriptInvokeFunctionImpl(node);
      }
      else if (type == JSON_ARRAY_VALUE) {
        return new TinyScriptJsonArrayValueImpl(node);
      }
      else if (type == JSON_ITEM_LIST) {
        return new TinyScriptJsonItemListImpl(node);
      }
      else if (type == JSON_MAP_VALUE) {
        return new TinyScriptJsonMapValueImpl(node);
      }
      else if (type == JSON_PAIR) {
        return new TinyScriptJsonPairImpl(node);
      }
      else if (type == JSON_PAIRS) {
        return new TinyScriptJsonPairsImpl(node);
      }
      else if (type == JSON_VALUE) {
        return new TinyScriptJsonValueImpl(node);
      }
      else if (type == NAMING_BLOCK) {
        return new TinyScriptNamingBlockImpl(node);
      }
      else if (type == NEGTIVE_SEGMENT) {
        return new TinyScriptNegtiveSegmentImpl(node);
      }
      else if (type == NEW_INSTANCE) {
        return new TinyScriptNewInstanceImpl(node);
      }
      else if (type == OPERATOR_SEGMENT) {
        return new TinyScriptOperatorSegmentImpl(node);
      }
      else if (type == OTC_NUMBER) {
        return new TinyScriptOtcNumberImpl(node);
      }
      else if (type == PAREN_SEGMENT) {
        return new TinyScriptParenSegmentImpl(node);
      }
      else if (type == PREFIX_OPERATOR_SEGMENT) {
        return new TinyScriptPrefixOperatorSegmentImpl(node);
      }
      else if (type == REF_CALL) {
        return new TinyScriptRefCallImpl(node);
      }
      else if (type == REF_VALUE) {
        return new TinyScriptRefValueImpl(node);
      }
      else if (type == SCRIPT) {
        return new TinyScriptScriptImpl(node);
      }
      else if (type == SCRIPT_BLOCK) {
        return new TinyScriptScriptBlockImpl(node);
      }
      else if (type == SEGMENT) {
        return new TinyScriptSegmentImpl(node);
      }
      else if (type == THROW_SEGMENT) {
        return new TinyScriptThrowSegmentImpl(node);
      }
      else if (type == TRY_BODY_BLOCK) {
        return new TinyScriptTryBodyBlockImpl(node);
      }
      else if (type == TRY_SEGMENT) {
        return new TinyScriptTrySegmentImpl(node);
      }
      else if (type == WHILE_SEGMENT) {
        return new TinyScriptWhileSegmentImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
