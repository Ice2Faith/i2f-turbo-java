// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.*;
import static i2f.turbo.idea.plugin.tinyscript.lang.parser.TinyScriptParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TinyScriptParser implements PsiParser, LightPsiParser {

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
  // ((NAMING|constString) TERM_COLON)? argumentValue
  public static boolean argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT, "<argument>");
    r = argument_0(b, l + 1);
    r = r && argumentValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((NAMING|constString) TERM_COLON)?
  private static boolean argument_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_0")) return false;
    argument_0_0(b, l + 1);
    return true;
  }

  // (NAMING|constString) TERM_COLON
  private static boolean argument_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument_0_0_0(b, l + 1);
    r = r && consumeToken(b, TERM_COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // NAMING|constString
  private static boolean argument_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_0_0_0")) return false;
    boolean r;
    r = consumeToken(b, NAMING);
    if (!r) r = constString(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // argument ( TERM_COMMA argument )*
  public static boolean argumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, "<argument list>");
    r = argument(b, l + 1);
    r = r && argumentList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( TERM_COMMA argument )*
  private static boolean argumentList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argumentList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argumentList_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA argument
  private static boolean argumentList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // express
  public static boolean argumentValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_VALUE, "<argument value>");
    r = express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_NUMBER_BIN
  public static boolean binNumber(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binNumber")) return false;
    if (!nextTokenIs(b, TERM_CONST_NUMBER_BIN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_NUMBER_BIN);
    exit_section_(b, m, BIN_NUMBER, r);
    return r;
  }

  /* ********************************************************** */
  // scriptBlock
  public static boolean catchBodyBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBodyBlock")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scriptBlock(b, l + 1);
    exit_section_(b, m, CATCH_BODY_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // NAMING
  public static boolean classNameBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNameBlock")) return false;
    if (!nextTokenIs(b, NAMING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMING);
    exit_section_(b, m, CLASS_NAME_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_COMMENT_SINGLE_LINE | TERM_COMMENT_MULTI_LINE
  public static boolean commentSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commentSegment")) return false;
    if (!nextTokenIs(b, "<comment segment>", TERM_COMMENT_MULTI_LINE, TERM_COMMENT_SINGLE_LINE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMENT_SEGMENT, "<comment segment>");
    r = consumeToken(b, TERM_COMMENT_SINGLE_LINE);
    if (!r) r = consumeToken(b, TERM_COMMENT_MULTI_LINE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // express
  public static boolean conditionBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditionBlock")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONDITION_BLOCK, "<condition block>");
    r = express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_BOOLEAN
  public static boolean constBool(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constBool")) return false;
    if (!nextTokenIs(b, TERM_CONST_BOOLEAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_BOOLEAN);
    exit_section_(b, m, CONST_BOOL, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_TYPE_CLASS
  public static boolean constClass(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constClass")) return false;
    if (!nextTokenIs(b, TERM_CONST_TYPE_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_TYPE_CLASS);
    exit_section_(b, m, CONST_CLASS, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_STRING_MULTILINE
  //     |TERM_CONST_STRING_MULTILINE_QUOTE
  public static boolean constMultilineString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constMultilineString")) return false;
    if (!nextTokenIs(b, "<const multiline string>", TERM_CONST_STRING_MULTILINE, TERM_CONST_STRING_MULTILINE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_MULTILINE_STRING, "<const multiline string>");
    r = consumeToken(b, TERM_CONST_STRING_MULTILINE);
    if (!r) r = consumeToken(b, TERM_CONST_STRING_MULTILINE_QUOTE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_NULL
  public static boolean constNull(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constNull")) return false;
    if (!nextTokenIs(b, TERM_CONST_NULL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_NULL);
    exit_section_(b, m, CONST_NULL, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_STRING_RENDER
  //     |TERM_CONST_STRING_RENDER_SINGLE
  public static boolean constRenderString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constRenderString")) return false;
    if (!nextTokenIs(b, "<const render string>", TERM_CONST_STRING_RENDER, TERM_CONST_STRING_RENDER_SINGLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_RENDER_STRING, "<const render string>");
    r = consumeToken(b, TERM_CONST_STRING_RENDER);
    if (!r) r = consumeToken(b, TERM_CONST_STRING_RENDER_SINGLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_STRING
  //     |TERM_CONST_STRING_SINGLE
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
  // constBool
  //     | constClass
  //     | constNull
  //     | constMultilineString
  //     | constRenderString
  //     | constString
  //     | decNumber
  //     | hexNumber
  //     | otcNumber
  //     | binNumber
  public static boolean constValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_VALUE, "<const value>");
    r = constBool(b, l + 1);
    if (!r) r = constClass(b, l + 1);
    if (!r) r = constNull(b, l + 1);
    if (!r) r = constMultilineString(b, l + 1);
    if (!r) r = constRenderString(b, l + 1);
    if (!r) r = constString(b, l + 1);
    if (!r) r = decNumber(b, l + 1);
    if (!r) r = hexNumber(b, l + 1);
    if (!r) r = otcNumber(b, l + 1);
    if (!r) r = binNumber(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KEY_BREAK
  //     | KEY_CONTINUE
  //     | KEY_RETURN (express)?
  public static boolean controlSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controlSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONTROL_SEGMENT, "<control segment>");
    r = consumeToken(b, KEY_BREAK);
    if (!r) r = consumeToken(b, KEY_CONTINUE);
    if (!r) r = controlSegment_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // KEY_RETURN (express)?
  private static boolean controlSegment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controlSegment_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_RETURN);
    r = r && controlSegment_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (express)?
  private static boolean controlSegment_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controlSegment_2_1")) return false;
    controlSegment_2_1_0(b, l + 1);
    return true;
  }

  // (express)
  private static boolean controlSegment_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controlSegment_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_DEBUGGER (namingBlock)? (TERM_PAREN_L conditionBlock TERM_PAREN_R)?
  public static boolean debuggerSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerSegment")) return false;
    if (!nextTokenIs(b, KEY_DEBUGGER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_DEBUGGER);
    r = r && debuggerSegment_1(b, l + 1);
    r = r && debuggerSegment_2(b, l + 1);
    exit_section_(b, m, DEBUGGER_SEGMENT, r);
    return r;
  }

  // (namingBlock)?
  private static boolean debuggerSegment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerSegment_1")) return false;
    debuggerSegment_1_0(b, l + 1);
    return true;
  }

  // (namingBlock)
  private static boolean debuggerSegment_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerSegment_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namingBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_PAREN_L conditionBlock TERM_PAREN_R)?
  private static boolean debuggerSegment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerSegment_2")) return false;
    debuggerSegment_2_0(b, l + 1);
    return true;
  }

  // TERM_PAREN_L conditionBlock TERM_PAREN_R
  private static boolean debuggerSegment_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerSegment_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_PAREN_L);
    r = r && conditionBlock(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_NUMBER_SCIEN_2
  //     | TERM_CONST_NUMBER_SCIEN_1
  //     | TERM_CONST_NUMBER_FLOAT
  //     | TERM_CONST_NUMBER
  //     | TERM_DIGIT
  public static boolean decNumber(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decNumber")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEC_NUMBER, "<dec number>");
    r = consumeToken(b, TERM_CONST_NUMBER_SCIEN_2);
    if (!r) r = consumeToken(b, TERM_CONST_NUMBER_SCIEN_1);
    if (!r) r = consumeToken(b, TERM_CONST_NUMBER_FLOAT);
    if (!r) r = consumeToken(b, TERM_CONST_NUMBER);
    if (!r) r = consumeToken(b, TERM_DIGIT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (ROUTE_NAMING|NAMING|extractExpress) (OP_ASSIGN) express
  public static boolean equalValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equalValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQUAL_VALUE, "<equal value>");
    r = equalValue_0(b, l + 1);
    r = r && consumeToken(b, OP_ASSIGN);
    r = r && express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ROUTE_NAMING|NAMING|extractExpress
  private static boolean equalValue_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equalValue_0")) return false;
    boolean r;
    r = consumeToken(b, ROUTE_NAMING);
    if (!r) r = consumeToken(b, NAMING);
    if (!r) r = extractExpress(b, l + 1);
    return r;
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
  // debuggerSegment
  //         | ifSegment
  //         | foreachSegment
  //         | forSegment
  //         | whileSegment
  //         | controlSegment
  //         | trySegment
  //         | throwSegment
  //         | parenSegment
  //         | prefixOperatorSegment
  //         | equalValue
  //         | newInstance
  //         | invokeFunction
  //         | constValue
  //         | refValue
  //         | jsonValue
  //         | negtiveSegment
  public static boolean expressSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESS_SEGMENT, "<express segment>");
    r = debuggerSegment(b, l + 1);
    if (!r) r = ifSegment(b, l + 1);
    if (!r) r = foreachSegment(b, l + 1);
    if (!r) r = forSegment(b, l + 1);
    if (!r) r = whileSegment(b, l + 1);
    if (!r) r = controlSegment(b, l + 1);
    if (!r) r = trySegment(b, l + 1);
    if (!r) r = throwSegment(b, l + 1);
    if (!r) r = parenSegment(b, l + 1);
    if (!r) r = prefixOperatorSegment(b, l + 1);
    if (!r) r = equalValue(b, l + 1);
    if (!r) r = newInstance(b, l + 1);
    if (!r) r = invokeFunction(b, l + 1);
    if (!r) r = constValue(b, l + 1);
    if (!r) r = refValue(b, l + 1);
    if (!r) r = jsonValue(b, l + 1);
    if (!r) r = negtiveSegment(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_SHARP TERM_CURLY_L extractPairs? TERM_CURLY_R
  public static boolean extractExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractExpress")) return false;
    if (!nextTokenIs(b, TERM_SHARP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TERM_SHARP, TERM_CURLY_L);
    r = r && extractExpress_2(b, l + 1);
    r = r && consumeToken(b, TERM_CURLY_R);
    exit_section_(b, m, EXTRACT_EXPRESS, r);
    return r;
  }

  // extractPairs?
  private static boolean extractExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractExpress_2")) return false;
    extractPairs(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (NAMING|ROUTE_NAMING|constString) (TERM_COLON (NAMING|ROUTE_NAMING|constString))?
  public static boolean extractPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXTRACT_PAIR, "<extract pair>");
    r = extractPair_0(b, l + 1);
    r = r && extractPair_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NAMING|ROUTE_NAMING|constString
  private static boolean extractPair_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_0")) return false;
    boolean r;
    r = consumeToken(b, NAMING);
    if (!r) r = consumeToken(b, ROUTE_NAMING);
    if (!r) r = constString(b, l + 1);
    return r;
  }

  // (TERM_COLON (NAMING|ROUTE_NAMING|constString))?
  private static boolean extractPair_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1")) return false;
    extractPair_1_0(b, l + 1);
    return true;
  }

  // TERM_COLON (NAMING|ROUTE_NAMING|constString)
  private static boolean extractPair_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COLON);
    r = r && extractPair_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NAMING|ROUTE_NAMING|constString
  private static boolean extractPair_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1_0_1")) return false;
    boolean r;
    r = consumeToken(b, NAMING);
    if (!r) r = consumeToken(b, ROUTE_NAMING);
    if (!r) r = constString(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // extractPair (TERM_COMMA extractPair)*
  public static boolean extractPairs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPairs")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXTRACT_PAIRS, "<extract pairs>");
    r = extractPair(b, l + 1);
    r = r && extractPairs_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TERM_COMMA extractPair)*
  private static boolean extractPairs_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPairs_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!extractPairs_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "extractPairs_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA extractPair
  private static boolean extractPairs_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPairs_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && extractPair(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // scriptBlock
  public static boolean finallyBodyBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "finallyBodyBlock")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scriptBlock(b, l + 1);
    exit_section_(b, m, FINALLY_BODY_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_FOR TERM_PAREN_L express TERM_SEMICOLON conditionBlock TERM_SEMICOLON express TERM_PAREN_R scriptBlock
  public static boolean forSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forSegment")) return false;
    if (!nextTokenIs(b, KEY_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_FOR, TERM_PAREN_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_SEMICOLON);
    r = r && conditionBlock(b, l + 1);
    r = r && consumeToken(b, TERM_SEMICOLON);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, FOR_SEGMENT, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_FOREACH TERM_PAREN_L namingBlock TERM_COLON express TERM_PAREN_R scriptBlock
  public static boolean foreachSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachSegment")) return false;
    if (!nextTokenIs(b, KEY_FOREACH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_FOREACH, TERM_PAREN_L);
    r = r && namingBlock(b, l + 1);
    r = r && consumeToken(b, TERM_COLON);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, FOREACH_SEGMENT, r);
    return r;
  }

  /* ********************************************************** */
  // NAMING TERM_PAREN_L argumentList? TERM_PAREN_R
  public static boolean functionCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionCall")) return false;
    if (!nextTokenIs(b, NAMING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NAMING, TERM_PAREN_L);
    r = r && functionCall_2(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // argumentList?
  private static boolean functionCall_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionCall_2")) return false;
    argumentList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TERM_CONST_NUMBER_HEX
  public static boolean hexNumber(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hexNumber")) return false;
    if (!nextTokenIs(b, TERM_CONST_NUMBER_HEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_NUMBER_HEX);
    exit_section_(b, m, HEX_NUMBER, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_IF TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock (  (KEY_ELSE KEY_IF | KEY_ELIF) TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock )* (KEY_ELSE scriptBlock)?
  public static boolean ifSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment")) return false;
    if (!nextTokenIs(b, KEY_IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_IF, TERM_PAREN_L);
    r = r && conditionBlock(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    r = r && ifSegment_5(b, l + 1);
    r = r && ifSegment_6(b, l + 1);
    exit_section_(b, m, IF_SEGMENT, r);
    return r;
  }

  // (  (KEY_ELSE KEY_IF | KEY_ELIF) TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock )*
  private static boolean ifSegment_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ifSegment_5_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ifSegment_5", c)) break;
    }
    return true;
  }

  // (KEY_ELSE KEY_IF | KEY_ELIF) TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock
  private static boolean ifSegment_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ifSegment_5_0_0(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_L);
    r = r && conditionBlock(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KEY_ELSE KEY_IF | KEY_ELIF
  private static boolean ifSegment_5_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment_5_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, KEY_ELSE, KEY_IF);
    if (!r) r = consumeToken(b, KEY_ELIF);
    exit_section_(b, m, null, r);
    return r;
  }

  // (KEY_ELSE scriptBlock)?
  private static boolean ifSegment_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment_6")) return false;
    ifSegment_6_0(b, l + 1);
    return true;
  }

  // KEY_ELSE scriptBlock
  private static boolean ifSegment_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifSegment_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_ELSE);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // refCall (TERM_DOT functionCall)*
  //     | functionCall (TERM_DOT functionCall)*
  public static boolean invokeFunction(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction")) return false;
    if (!nextTokenIs(b, "<invoke function>", NAMING, REF_EXPRESS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INVOKE_FUNCTION, "<invoke function>");
    r = invokeFunction_0(b, l + 1);
    if (!r) r = invokeFunction_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // refCall (TERM_DOT functionCall)*
  private static boolean invokeFunction_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = refCall(b, l + 1);
    r = r && invokeFunction_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_DOT functionCall)*
  private static boolean invokeFunction_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!invokeFunction_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "invokeFunction_0_1", c)) break;
    }
    return true;
  }

  // TERM_DOT functionCall
  private static boolean invokeFunction_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_DOT);
    r = r && functionCall(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // functionCall (TERM_DOT functionCall)*
  private static boolean invokeFunction_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = functionCall(b, l + 1);
    r = r && invokeFunction_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_DOT functionCall)*
  private static boolean invokeFunction_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!invokeFunction_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "invokeFunction_1_1", c)) break;
    }
    return true;
  }

  // TERM_DOT functionCall
  private static boolean invokeFunction_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invokeFunction_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_DOT);
    r = r && functionCall(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_BRACKET_SQUARE_L jsonItemList? TERM_BRACKET_SQUARE_R
  public static boolean jsonArrayValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArrayValue")) return false;
    if (!nextTokenIs(b, TERM_BRACKET_SQUARE_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_BRACKET_SQUARE_L);
    r = r && jsonArrayValue_1(b, l + 1);
    r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
    exit_section_(b, m, JSON_ARRAY_VALUE, r);
    return r;
  }

  // jsonItemList?
  private static boolean jsonArrayValue_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArrayValue_1")) return false;
    jsonItemList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // express (TERM_COMMA express)*
  public static boolean jsonItemList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonItemList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_ITEM_LIST, "<json item list>");
    r = express(b, l + 1);
    r = r && jsonItemList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TERM_COMMA express)*
  private static boolean jsonItemList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonItemList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonItemList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonItemList_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA express
  private static boolean jsonItemList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonItemList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CURLY_L jsonPairs? TERM_CURLY_R
  public static boolean jsonMapValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonMapValue")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CURLY_L);
    r = r && jsonMapValue_1(b, l + 1);
    r = r && consumeToken(b, TERM_CURLY_R);
    exit_section_(b, m, JSON_MAP_VALUE, r);
    return r;
  }

  // jsonPairs?
  private static boolean jsonMapValue_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonMapValue_1")) return false;
    jsonPairs(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (NAMING|constString) TERM_COLON express
  public static boolean jsonPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPair")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_PAIR, "<json pair>");
    r = jsonPair_0(b, l + 1);
    r = r && consumeToken(b, TERM_COLON);
    r = r && express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NAMING|constString
  private static boolean jsonPair_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPair_0")) return false;
    boolean r;
    r = consumeToken(b, NAMING);
    if (!r) r = constString(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // jsonPair (TERM_COMMA jsonPair)*
  public static boolean jsonPairs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairs")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_PAIRS, "<json pairs>");
    r = jsonPair(b, l + 1);
    r = r && jsonPairs_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TERM_COMMA jsonPair)*
  private static boolean jsonPairs_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairs_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonPairs_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonPairs_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA jsonPair
  private static boolean jsonPairs_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairs_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && jsonPair(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // invokeFunction
  //     | constValue
  //     | refValue
  //     | jsonArrayValue
  //     | jsonMapValue
  public static boolean jsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_VALUE, "<json value>");
    r = invokeFunction(b, l + 1);
    if (!r) r = constValue(b, l + 1);
    if (!r) r = refValue(b, l + 1);
    if (!r) r = jsonArrayValue(b, l + 1);
    if (!r) r = jsonMapValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NAMING
  public static boolean namingBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namingBlock")) return false;
    if (!nextTokenIs(b, NAMING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMING);
    exit_section_(b, m, NAMING_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // OP_SUB express
  public static boolean negtiveSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negtiveSegment")) return false;
    if (!nextTokenIs(b, OP_SUB)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_SUB);
    r = r && express(b, l + 1);
    exit_section_(b, m, NEGTIVE_SEGMENT, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_NEW invokeFunction
  public static boolean newInstance(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newInstance")) return false;
    if (!nextTokenIs(b, KEY_NEW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_NEW);
    r = r && invokeFunction(b, l + 1);
    exit_section_(b, m, NEW_INSTANCE, r);
    return r;
  }

  /* ********************************************************** */
  // ( OP_AS | OP_CAST  | OP_IS | OP_INSTANCE_OF | OP_TYPE_OF) express
  //     |  (OP_MUL | OP_DIV | OP_MOD) express
  //     | (OP_ADD | OP_SUB) express
  //     | (OP_IN | OP_NOT_IN| OP_GTE | OP_GTE_STR | OP_LTE | OP_LTE_STR | OP_NE | OP_NE_STR | OP_NEQ | OP_NEQ_STR | OP_EQ | OP_EQ_STR | OP_GT | OP_GT_STR | OP_LT | OP_LT_STR) express
  //     | (OP_AND | OP_AND_STR | OP_OR | OP_OR_STR) express
  //     | OP_MOD
  //     | TERM_QUESTION express TERM_COLON express
  public static boolean operatorSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR_SEGMENT, "<operator segment>");
    r = operatorSegment_0(b, l + 1);
    if (!r) r = operatorSegment_1(b, l + 1);
    if (!r) r = operatorSegment_2(b, l + 1);
    if (!r) r = operatorSegment_3(b, l + 1);
    if (!r) r = operatorSegment_4(b, l + 1);
    if (!r) r = consumeToken(b, OP_MOD);
    if (!r) r = operatorSegment_6(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( OP_AS | OP_CAST  | OP_IS | OP_INSTANCE_OF | OP_TYPE_OF) express
  private static boolean operatorSegment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operatorSegment_0_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_AS | OP_CAST  | OP_IS | OP_INSTANCE_OF | OP_TYPE_OF
  private static boolean operatorSegment_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_AS);
    if (!r) r = consumeToken(b, OP_CAST);
    if (!r) r = consumeToken(b, OP_IS);
    if (!r) r = consumeToken(b, OP_INSTANCE_OF);
    if (!r) r = consumeToken(b, OP_TYPE_OF);
    return r;
  }

  // (OP_MUL | OP_DIV | OP_MOD) express
  private static boolean operatorSegment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operatorSegment_1_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_MUL | OP_DIV | OP_MOD
  private static boolean operatorSegment_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_1_0")) return false;
    boolean r;
    r = consumeToken(b, OP_MUL);
    if (!r) r = consumeToken(b, OP_DIV);
    if (!r) r = consumeToken(b, OP_MOD);
    return r;
  }

  // (OP_ADD | OP_SUB) express
  private static boolean operatorSegment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operatorSegment_2_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_ADD | OP_SUB
  private static boolean operatorSegment_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_2_0")) return false;
    boolean r;
    r = consumeToken(b, OP_ADD);
    if (!r) r = consumeToken(b, OP_SUB);
    return r;
  }

  // (OP_IN | OP_NOT_IN| OP_GTE | OP_GTE_STR | OP_LTE | OP_LTE_STR | OP_NE | OP_NE_STR | OP_NEQ | OP_NEQ_STR | OP_EQ | OP_EQ_STR | OP_GT | OP_GT_STR | OP_LT | OP_LT_STR) express
  private static boolean operatorSegment_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operatorSegment_3_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_IN | OP_NOT_IN| OP_GTE | OP_GTE_STR | OP_LTE | OP_LTE_STR | OP_NE | OP_NE_STR | OP_NEQ | OP_NEQ_STR | OP_EQ | OP_EQ_STR | OP_GT | OP_GT_STR | OP_LT | OP_LT_STR
  private static boolean operatorSegment_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_3_0")) return false;
    boolean r;
    r = consumeToken(b, OP_IN);
    if (!r) r = consumeToken(b, OP_NOT_IN);
    if (!r) r = consumeToken(b, OP_GTE);
    if (!r) r = consumeToken(b, OP_GTE_STR);
    if (!r) r = consumeToken(b, OP_LTE);
    if (!r) r = consumeToken(b, OP_LTE_STR);
    if (!r) r = consumeToken(b, OP_NE);
    if (!r) r = consumeToken(b, OP_NE_STR);
    if (!r) r = consumeToken(b, OP_NEQ);
    if (!r) r = consumeToken(b, OP_NEQ_STR);
    if (!r) r = consumeToken(b, OP_EQ);
    if (!r) r = consumeToken(b, OP_EQ_STR);
    if (!r) r = consumeToken(b, OP_GT);
    if (!r) r = consumeToken(b, OP_GT_STR);
    if (!r) r = consumeToken(b, OP_LT);
    if (!r) r = consumeToken(b, OP_LT_STR);
    return r;
  }

  // (OP_AND | OP_AND_STR | OP_OR | OP_OR_STR) express
  private static boolean operatorSegment_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operatorSegment_4_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_AND | OP_AND_STR | OP_OR | OP_OR_STR
  private static boolean operatorSegment_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_4_0")) return false;
    boolean r;
    r = consumeToken(b, OP_AND);
    if (!r) r = consumeToken(b, OP_AND_STR);
    if (!r) r = consumeToken(b, OP_OR);
    if (!r) r = consumeToken(b, OP_OR_STR);
    return r;
  }

  // TERM_QUESTION express TERM_COLON express
  private static boolean operatorSegment_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_QUESTION);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_COLON);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_NUMBER_OTC
  public static boolean otcNumber(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "otcNumber")) return false;
    if (!nextTokenIs(b, TERM_CONST_NUMBER_OTC)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_NUMBER_OTC);
    exit_section_(b, m, OTC_NUMBER, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_PAREN_L express TERM_PAREN_R
  public static boolean parenSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenSegment")) return false;
    if (!nextTokenIs(b, TERM_PAREN_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_PAREN_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, PAREN_SEGMENT, r);
    return r;
  }

  /* ********************************************************** */
  // (OP_EXCLAM | OP_NOT) express
  public static boolean prefixOperatorSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefixOperatorSegment")) return false;
    if (!nextTokenIs(b, "<prefix operator segment>", OP_EXCLAM, OP_NOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIX_OPERATOR_SEGMENT, "<prefix operator segment>");
    r = prefixOperatorSegment_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OP_EXCLAM | OP_NOT
  private static boolean prefixOperatorSegment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefixOperatorSegment_0")) return false;
    boolean r;
    r = consumeToken(b, OP_EXCLAM);
    if (!r) r = consumeToken(b, OP_NOT);
    return r;
  }

  /* ********************************************************** */
  // refValue TERM_DOT functionCall
  public static boolean refCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refCall")) return false;
    if (!nextTokenIs(b, REF_EXPRESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = refValue(b, l + 1);
    r = r && consumeToken(b, TERM_DOT);
    r = r && functionCall(b, l + 1);
    exit_section_(b, m, REF_CALL, r);
    return r;
  }

  /* ********************************************************** */
  // REF_EXPRESS
  public static boolean refValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refValue")) return false;
    if (!nextTokenIs(b, REF_EXPRESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REF_EXPRESS);
    exit_section_(b, m, REF_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // script
  static boolean root(PsiBuilder b, int l) {
    return script(b, l + 1);
  }

  /* ********************************************************** */
  // segment+
  //     |express (TERM_SEMICOLON)? <<eof>>
  public static boolean script(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT, "<script>");
    r = script_0(b, l + 1);
    if (!r) r = script_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // segment+
  private static boolean script_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = segment(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!segment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "script_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // express (TERM_SEMICOLON)? <<eof>>
  private static boolean script_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = express(b, l + 1);
    r = r && script_1_1(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_SEMICOLON)?
  private static boolean script_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_1_1")) return false;
    consumeToken(b, TERM_SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TERM_CURLY_L script TERM_CURLY_R
  public static boolean scriptBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBlock")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CURLY_L);
    r = r && script(b, l + 1);
    r = r && consumeToken(b, TERM_CURLY_R);
    exit_section_(b, m, SCRIPT_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // express TERM_SEMICOLON
  //     | commentSegment
  public static boolean segment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "segment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEGMENT, "<segment>");
    r = segment_0(b, l + 1);
    if (!r) r = commentSegment(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // express TERM_SEMICOLON
  private static boolean segment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "segment_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = express(b, l + 1);
    r = r && consumeToken(b, TERM_SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_THROW express
  public static boolean throwSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throwSegment")) return false;
    if (!nextTokenIs(b, KEY_THROW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_THROW);
    r = r && express(b, l + 1);
    exit_section_(b, m, THROW_SEGMENT, r);
    return r;
  }

  /* ********************************************************** */
  // scriptBlock
  public static boolean tryBodyBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tryBodyBlock")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scriptBlock(b, l + 1);
    exit_section_(b, m, TRY_BODY_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_TRY tryBodyBlock (KEY_CATCH TERM_PAREN_L (classNameBlock (OP_VERTICAL_BAR classNameBlock)*) namingBlock TERM_PAREN_R catchBodyBlock)* (KEY_FINALLY finallyBodyBlock)?
  public static boolean trySegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment")) return false;
    if (!nextTokenIs(b, KEY_TRY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_TRY);
    r = r && tryBodyBlock(b, l + 1);
    r = r && trySegment_2(b, l + 1);
    r = r && trySegment_3(b, l + 1);
    exit_section_(b, m, TRY_SEGMENT, r);
    return r;
  }

  // (KEY_CATCH TERM_PAREN_L (classNameBlock (OP_VERTICAL_BAR classNameBlock)*) namingBlock TERM_PAREN_R catchBodyBlock)*
  private static boolean trySegment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trySegment_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "trySegment_2", c)) break;
    }
    return true;
  }

  // KEY_CATCH TERM_PAREN_L (classNameBlock (OP_VERTICAL_BAR classNameBlock)*) namingBlock TERM_PAREN_R catchBodyBlock
  private static boolean trySegment_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_CATCH, TERM_PAREN_L);
    r = r && trySegment_2_0_2(b, l + 1);
    r = r && namingBlock(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && catchBodyBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // classNameBlock (OP_VERTICAL_BAR classNameBlock)*
  private static boolean trySegment_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_2_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = classNameBlock(b, l + 1);
    r = r && trySegment_2_0_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_VERTICAL_BAR classNameBlock)*
  private static boolean trySegment_2_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_2_0_2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trySegment_2_0_2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "trySegment_2_0_2_1", c)) break;
    }
    return true;
  }

  // OP_VERTICAL_BAR classNameBlock
  private static boolean trySegment_2_0_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_2_0_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_VERTICAL_BAR);
    r = r && classNameBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (KEY_FINALLY finallyBodyBlock)?
  private static boolean trySegment_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_3")) return false;
    trySegment_3_0(b, l + 1);
    return true;
  }

  // KEY_FINALLY finallyBodyBlock
  private static boolean trySegment_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trySegment_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY_FINALLY);
    r = r && finallyBodyBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KEY_WHILE TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock
  public static boolean whileSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileSegment")) return false;
    if (!nextTokenIs(b, KEY_WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_WHILE, TERM_PAREN_L);
    r = r && conditionBlock(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, WHILE_SEGMENT, r);
    return r;
  }

}
