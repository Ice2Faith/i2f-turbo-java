// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;
import static i2f.turbo.idea.plugin.funic.lang.parser.FunicParserUtil.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class FunicParser implements PsiParser, LightPsiParser {

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
  // (OP_ASSIGN | OP_ASSIGN_ADD | OP_ASSIGN_SUB | OP_ASSIGN_MUL | OP_ASSIGN_DIV | OP_ASSIGN_MOD | OP_ASSIGN_IFNULL | OP_ASSIGN_NOTNULL) express
  public static boolean assignRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignRightPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_RIGHT_PART, "<assign right part>");
    r = assignRightPart_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OP_ASSIGN | OP_ASSIGN_ADD | OP_ASSIGN_SUB | OP_ASSIGN_MUL | OP_ASSIGN_DIV | OP_ASSIGN_MOD | OP_ASSIGN_IFNULL | OP_ASSIGN_NOTNULL
  private static boolean assignRightPart_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignRightPart_0")) return false;
    boolean r;
    r = consumeToken(b, OP_ASSIGN);
    if (!r) r = consumeToken(b, OP_ASSIGN_ADD);
    if (!r) r = consumeToken(b, OP_ASSIGN_SUB);
    if (!r) r = consumeToken(b, OP_ASSIGN_MUL);
    if (!r) r = consumeToken(b, OP_ASSIGN_DIV);
    if (!r) r = consumeToken(b, OP_ASSIGN_MOD);
    if (!r) r = consumeToken(b, OP_ASSIGN_IFNULL);
    if (!r) r = consumeToken(b, OP_ASSIGN_NOTNULL);
    return r;
  }

  /* ********************************************************** */
  // (OP_RECV_FROM express)+
  public static boolean awaitExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "awaitExpress")) return false;
    if (!nextTokenIs(b, OP_RECV_FROM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = awaitExpress_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!awaitExpress_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "awaitExpress", c)) break;
    }
    exit_section_(b, m, AWAIT_EXPRESS, r);
    return r;
  }

  // OP_RECV_FROM express
  private static boolean awaitExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "awaitExpress_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_RECV_FROM);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_BIT_LMOV | OP_BIT_RSMOV | OP_BIT_RMOV | OP_BIT_XOR  | OP_BIT_AND  | OP_BIT_OR
  public static boolean bitOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitOperatorPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BIT_OPERATOR_PART, "<bit operator part>");
    r = consumeToken(b, OP_BIT_LMOV);
    if (!r) r = consumeToken(b, OP_BIT_RSMOV);
    if (!r) r = consumeToken(b, OP_BIT_RMOV);
    if (!r) r = consumeToken(b, OP_BIT_XOR);
    if (!r) r = consumeToken(b, OP_BIT_AND);
    if (!r) r = consumeToken(b, OP_BIT_OR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KW_BREAK
  public static boolean breakExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "breakExpress")) return false;
    if (!nextTokenIs(b, KW_BREAK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_BREAK);
    exit_section_(b, m, BREAK_EXPRESS, r);
    return r;
  }

  /* ********************************************************** */
  // KW_AS (typeClass| express)
  public static boolean castAsRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "castAsRightPart")) return false;
    if (!nextTokenIs(b, KW_AS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_AS);
    r = r && castAsRightPart_1(b, l + 1);
    exit_section_(b, m, CAST_AS_RIGHT_PART, r);
    return r;
  }

  // typeClass| express
  private static boolean castAsRightPart_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "castAsRightPart_1")) return false;
    boolean r;
    r = typeClass(b, l + 1);
    if (!r) r = express(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // KW_CATCH TERM_PAREN_L (fullName (OP_BIT_OR fullName)*) IDENTIFIER TERM_PAREN_R scriptBlock
  //     | KW_CATCH TERM_PAREN_L IDENTIFIER TERM_PAREN_R scriptBlock
  public static boolean catchBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock")) return false;
    if (!nextTokenIs(b, KW_CATCH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catchBlock_0(b, l + 1);
    if (!r) r = catchBlock_1(b, l + 1);
    exit_section_(b, m, CATCH_BLOCK, r);
    return r;
  }

  // KW_CATCH TERM_PAREN_L (fullName (OP_BIT_OR fullName)*) IDENTIFIER TERM_PAREN_R scriptBlock
  private static boolean catchBlock_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_CATCH, TERM_PAREN_L);
    r = r && catchBlock_0_2(b, l + 1);
    r = r && consumeTokens(b, 0, IDENTIFIER, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // fullName (OP_BIT_OR fullName)*
  private static boolean catchBlock_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fullName(b, l + 1);
    r = r && catchBlock_0_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_BIT_OR fullName)*
  private static boolean catchBlock_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock_0_2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!catchBlock_0_2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "catchBlock_0_2_1", c)) break;
    }
    return true;
  }

  // OP_BIT_OR fullName
  private static boolean catchBlock_0_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock_0_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_BIT_OR);
    r = r && fullName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_CATCH TERM_PAREN_L IDENTIFIER TERM_PAREN_R scriptBlock
  private static boolean catchBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catchBlock_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_CATCH, TERM_PAREN_L, IDENTIFIER, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
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
  // OP_TEQ | KW_TEQ | OP_TNEQ | KW_TNEQ | OP_GT | KW_GT | OP_GTE | KW_GTE | OP_LT | KW_LT | OP_LTE | KW_LTE | OP_EQ | KW_EQ | OP_NE | OP_NEQ | KW_NEQ | KW_NE | KW_IN | (KW_NOT KW_IN) | KW_INSTANCEOF | KW_IS
  public static boolean compareOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compareOperatorPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPARE_OPERATOR_PART, "<compare operator part>");
    r = consumeToken(b, OP_TEQ);
    if (!r) r = consumeToken(b, KW_TEQ);
    if (!r) r = consumeToken(b, OP_TNEQ);
    if (!r) r = consumeToken(b, KW_TNEQ);
    if (!r) r = consumeToken(b, OP_GT);
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
    if (!r) r = consumeToken(b, OP_NEQ);
    if (!r) r = consumeToken(b, KW_NEQ);
    if (!r) r = consumeToken(b, KW_NE);
    if (!r) r = consumeToken(b, KW_IN);
    if (!r) r = compareOperatorPart_19(b, l + 1);
    if (!r) r = consumeToken(b, KW_INSTANCEOF);
    if (!r) r = consumeToken(b, KW_IS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // KW_NOT KW_IN
  private static boolean compareOperatorPart_19(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compareOperatorPart_19")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_NOT, KW_IN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_PAREN_L express TERM_PAREN_R
  public static boolean conditionBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditionBlock")) return false;
    if (!nextTokenIs(b, TERM_PAREN_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_PAREN_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, CONDITION_BLOCK, r);
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
  // constRenderString
  //     | constString
  public static boolean constCharSequence(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constCharSequence")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_CHAR_SEQUENCE, "<const char sequence>");
    r = constRenderString(b, l + 1);
    if (!r) r = constString(b, l + 1);
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
  // TERM_CONST_STRING_MULTILINE
  //     |TERM_CONST_STRING_MULTILINE_QUOTE
  public static boolean constMultiString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constMultiString")) return false;
    if (!nextTokenIs(b, "<const multi string>", TERM_CONST_STRING_MULTILINE, TERM_CONST_STRING_MULTILINE_QUOTE))
      return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_MULTI_STRING, "<const multi string>");
    r = consumeToken(b, TERM_CONST_STRING_MULTILINE);
    if (!r) r = consumeToken(b, TERM_CONST_STRING_MULTILINE_QUOTE);
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
  // TERM_CONST_STRING_RENDER
  //     |TERM_CONST_STRING_RENDER_SINGLE
  public static boolean constRenderString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constRenderString")) return false;
    if (!nextTokenIs(b, "<const render string>", TERM_CONST_STRING_RENDER, TERM_CONST_STRING_RENDER_SINGLE))
      return false;
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
  // constMultiString
  //     | constCharSequence
  //     | constNumeric
  //     | constBoolean
  //     | constNull
  public static boolean constValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_VALUE, "<const value>");
    r = constMultiString(b, l + 1);
    if (!r) r = constCharSequence(b, l + 1);
    if (!r) r = constNumeric(b, l + 1);
    if (!r) r = constBoolean(b, l + 1);
    if (!r) r = constNull(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KW_CONTINUE
  public static boolean continueExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continueExpress")) return false;
    if (!nextTokenIs(b, KW_CONTINUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_CONTINUE);
    exit_section_(b, m, CONTINUE_EXPRESS, r);
    return r;
  }

  /* ********************************************************** */
  // KW_DEBUGGER fullName? (TERM_PAREN_L express TERM_PAREN_R)?
  public static boolean debuggerExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerExpress")) return false;
    if (!nextTokenIs(b, KW_DEBUGGER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_DEBUGGER);
    r = r && debuggerExpress_1(b, l + 1);
    r = r && debuggerExpress_2(b, l + 1);
    exit_section_(b, m, DEBUGGER_EXPRESS, r);
    return r;
  }

  // fullName?
  private static boolean debuggerExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerExpress_1")) return false;
    fullName(b, l + 1);
    return true;
  }

  // (TERM_PAREN_L express TERM_PAREN_R)?
  private static boolean debuggerExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerExpress_2")) return false;
    debuggerExpress_2_0(b, l + 1);
    return true;
  }

  // TERM_PAREN_L express TERM_PAREN_R
  private static boolean debuggerExpress_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "debuggerExpress_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_PAREN_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KW_DO scriptBlock KW_WHILE conditionBlock
  public static boolean doWhileExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "doWhileExpress")) return false;
    if (!nextTokenIs(b, KW_DO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_DO);
    r = r && scriptBlock(b, l + 1);
    r = r && consumeToken(b, KW_WHILE);
    r = r && conditionBlock(b, l + 1);
    exit_section_(b, m, DO_WHILE_EXPRESS, r);
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
  // circleExpress // 括号表达式
  //     | newInstanceExpress // 新建实例对象
  //     | newArrayExpress // 新建数组
  //     | ifElseExpress // if-else 语句
  //     | whileExpress // while 语句
  //     | doWhileExpress // do-while 语句
  //     | foreachExpress // 集合迭代语句
  //     | forLoopExpress // for 循环语句
  //     | forRangeExpress // for-range 语句
  //     | breakExpress // break 语句
  //     | continueExpress // continue 语句
  //     | returnExpress // return 语句
  //     | throwExpress // throw 语句
  //     | importExpress // 导入语句
  //     | tryCatchFinallyExpress // try-catch-finally 语句
  //     | functionDeclareExpress // 函数声明语句
  //     | lambdaExpress // Lambda语句
  //     | goRunExpress // 启动线程语句
  //     | awaitExpress // 等待异步执行的结果返回
  //     | synchronizedExpress // 同步语句
  //     | staticFunctionCall // 静态函数调用
  //     | globalFunctionCall // 全局函数调用
  //     | staticFieldValue // 静态值获取
  //     | prefixOperatorPart express // 前置表达式；高优先级，不能提取为子规则
  //     | incrDecrPrefixOperatorPart express // 前置自增表达式；高优先级，不能提取为子规则
  //     | listValueExpress // 列表表达式
  //     | mapValueExpress // 映射表达式
  //     | scriptBlock // 语句块
  //     | debuggerExpress
  //     | KW_DEF extractExpress // 等号赋值表达式，后接 assignRightPart
  //     | extractExpress // 等号赋值表达式，后接 assignRightPart
  //     | KW_DEF express // 等号赋值表达式，后接 assignRightPart
  //     | valueSegment
  public static boolean expressSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESS_SEGMENT, "<express segment>");
    r = circleExpress(b, l + 1);
    if (!r) r = newInstanceExpress(b, l + 1);
    if (!r) r = newArrayExpress(b, l + 1);
    if (!r) r = ifElseExpress(b, l + 1);
    if (!r) r = whileExpress(b, l + 1);
    if (!r) r = doWhileExpress(b, l + 1);
    if (!r) r = foreachExpress(b, l + 1);
    if (!r) r = forLoopExpress(b, l + 1);
    if (!r) r = forRangeExpress(b, l + 1);
    if (!r) r = breakExpress(b, l + 1);
    if (!r) r = continueExpress(b, l + 1);
    if (!r) r = returnExpress(b, l + 1);
    if (!r) r = throwExpress(b, l + 1);
    if (!r) r = importExpress(b, l + 1);
    if (!r) r = tryCatchFinallyExpress(b, l + 1);
    if (!r) r = functionDeclareExpress(b, l + 1);
    if (!r) r = lambdaExpress(b, l + 1);
    if (!r) r = goRunExpress(b, l + 1);
    if (!r) r = awaitExpress(b, l + 1);
    if (!r) r = synchronizedExpress(b, l + 1);
    if (!r) r = staticFunctionCall(b, l + 1);
    if (!r) r = globalFunctionCall(b, l + 1);
    if (!r) r = staticFieldValue(b, l + 1);
    if (!r) r = expressSegment_23(b, l + 1);
    if (!r) r = expressSegment_24(b, l + 1);
    if (!r) r = listValueExpress(b, l + 1);
    if (!r) r = mapValueExpress(b, l + 1);
    if (!r) r = scriptBlock(b, l + 1);
    if (!r) r = debuggerExpress(b, l + 1);
    if (!r) r = expressSegment_29(b, l + 1);
    if (!r) r = extractExpress(b, l + 1);
    if (!r) r = expressSegment_31(b, l + 1);
    if (!r) r = valueSegment(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // prefixOperatorPart express
  private static boolean expressSegment_23(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment_23")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = prefixOperatorPart(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // incrDecrPrefixOperatorPart express
  private static boolean expressSegment_24(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment_24")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = incrDecrPrefixOperatorPart(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_DEF extractExpress
  private static boolean expressSegment_29(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment_29")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_DEF);
    r = r && extractExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_DEF express
  private static boolean expressSegment_31(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressSegment_31")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_DEF);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
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
  // (express) (TERM_COLON (express))?
  public static boolean extractPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXTRACT_PAIR, "<extract pair>");
    r = extractPair_0(b, l + 1);
    r = r && extractPair_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (express)
  private static boolean extractPair_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_COLON (express))?
  private static boolean extractPair_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1")) return false;
    extractPair_1_0(b, l + 1);
    return true;
  }

  // TERM_COLON (express)
  private static boolean extractPair_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COLON);
    r = r && extractPair_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (express)
  private static boolean extractPair_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extractPair_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = express(b, l + 1);
    exit_section_(b, m, null, r);
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
  // OP_EXCLAM
  public static boolean factorRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factorRightPart")) return false;
    if (!nextTokenIs(b, OP_EXCLAM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_EXCLAM);
    exit_section_(b, m, FACTOR_RIGHT_PART, r);
    return r;
  }

  /* ********************************************************** */
  // KW_FOR TERM_PAREN_L express? TERM_SEMICOLON express? TERM_SEMICOLON express? TERM_PAREN_R scriptBlock
  public static boolean forLoopExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forLoopExpress")) return false;
    if (!nextTokenIs(b, KW_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_FOR, TERM_PAREN_L);
    r = r && forLoopExpress_2(b, l + 1);
    r = r && consumeToken(b, TERM_SEMICOLON);
    r = r && forLoopExpress_4(b, l + 1);
    r = r && consumeToken(b, TERM_SEMICOLON);
    r = r && forLoopExpress_6(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, FOR_LOOP_EXPRESS, r);
    return r;
  }

  // express?
  private static boolean forLoopExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forLoopExpress_2")) return false;
    express(b, l + 1);
    return true;
  }

  // express?
  private static boolean forLoopExpress_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forLoopExpress_4")) return false;
    express(b, l + 1);
    return true;
  }

  // express?
  private static boolean forLoopExpress_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forLoopExpress_6")) return false;
    express(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // KW_FOR TERM_PAREN_L IDENTIFIER express OP_EXTRA express TERM_PAREN_R scriptBlock
  public static boolean forRangeExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forRangeExpress")) return false;
    if (!nextTokenIs(b, KW_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_FOR, TERM_PAREN_L, IDENTIFIER);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, OP_EXTRA);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, FOR_RANGE_EXPRESS, r);
    return r;
  }

  /* ********************************************************** */
  // KW_FOR TERM_PAREN_L IDENTIFIER TERM_COLON express TERM_PAREN_R scriptBlock
  public static boolean foreachExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachExpress")) return false;
    if (!nextTokenIs(b, KW_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_FOR, TERM_PAREN_L, IDENTIFIER, TERM_COLON);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, FOREACH_EXPRESS, r);
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
  // IDENTIFIER TERM_COLON express
  //     | express
  public static boolean functionArgument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArgument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_ARGUMENT, "<function argument>");
    r = functionArgument_0(b, l + 1);
    if (!r) r = express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IDENTIFIER TERM_COLON express
  private static boolean functionArgument_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArgument_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, TERM_COLON);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_PAREN_L (functionArgument (TERM_COMMA functionArgument)*)? TERM_PAREN_R
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

  // (functionArgument (TERM_COMMA functionArgument)*)?
  private static boolean functionArguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArguments_1")) return false;
    functionArguments_1_0(b, l + 1);
    return true;
  }

  // functionArgument (TERM_COMMA functionArgument)*
  private static boolean functionArguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = functionArgument(b, l + 1);
    r = r && functionArguments_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_COMMA functionArgument)*
  private static boolean functionArguments_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArguments_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!functionArguments_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionArguments_1_0_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA functionArgument
  private static boolean functionArguments_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionArguments_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && functionArgument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (KW_FUNC|KW_DEF) IDENTIFIER functionDeclareParameters functionDeclareReturn? scriptBlock
  public static boolean functionDeclareExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareExpress")) return false;
    if (!nextTokenIs(b, "<function declare express>", KW_DEF, KW_FUNC)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARE_EXPRESS, "<function declare express>");
    r = functionDeclareExpress_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    r = r && functionDeclareParameters(b, l + 1);
    r = r && functionDeclareExpress_3(b, l + 1);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // KW_FUNC|KW_DEF
  private static boolean functionDeclareExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareExpress_0")) return false;
    boolean r;
    r = consumeToken(b, KW_FUNC);
    if (!r) r = consumeToken(b, KW_DEF);
    return r;
  }

  // functionDeclareReturn?
  private static boolean functionDeclareExpress_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareExpress_3")) return false;
    functionDeclareReturn(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TERM_PAREN_L ( functionParameter (TERM_COMMA functionParameter)*)? TERM_PAREN_R
  public static boolean functionDeclareParameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareParameters")) return false;
    if (!nextTokenIs(b, TERM_PAREN_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_PAREN_L);
    r = r && functionDeclareParameters_1(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    exit_section_(b, m, FUNCTION_DECLARE_PARAMETERS, r);
    return r;
  }

  // ( functionParameter (TERM_COMMA functionParameter)*)?
  private static boolean functionDeclareParameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareParameters_1")) return false;
    functionDeclareParameters_1_0(b, l + 1);
    return true;
  }

  // functionParameter (TERM_COMMA functionParameter)*
  private static boolean functionDeclareParameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareParameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = functionParameter(b, l + 1);
    r = r && functionDeclareParameters_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_COMMA functionParameter)*
  private static boolean functionDeclareParameters_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareParameters_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!functionDeclareParameters_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionDeclareParameters_1_0_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA functionParameter
  private static boolean functionDeclareParameters_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareParameters_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && functionParameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_COLON fullName
  public static boolean functionDeclareReturn(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclareReturn")) return false;
    if (!nextTokenIs(b, TERM_COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COLON);
    r = r && fullName(b, l + 1);
    exit_section_(b, m, FUNCTION_DECLARE_RETURN, r);
    return r;
  }

  /* ********************************************************** */
  // OP_DIAMOND_NAME_L express OP_DIAMOND_NAME_R // 函数名支持从变量获取
  //     | IDENTIFIER
  public static boolean functionName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionName")) return false;
    if (!nextTokenIs(b, "<function name>", IDENTIFIER, OP_DIAMOND_NAME_L)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_NAME, "<function name>");
    r = functionName_0(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OP_DIAMOND_NAME_L express OP_DIAMOND_NAME_R
  private static boolean functionName_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionName_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_DIAMOND_NAME_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, OP_DIAMOND_NAME_R);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // fullName IDENTIFIER
  //     | IDENTIFIER TERM_COLON fullName
  //     |IDENTIFIER
  public static boolean functionParameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParameter")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = functionParameter_0(b, l + 1);
    if (!r) r = functionParameter_1(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FUNCTION_PARAMETER, r);
    return r;
  }

  // fullName IDENTIFIER
  private static boolean functionParameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParameter_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fullName(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // IDENTIFIER TERM_COLON fullName
  private static boolean functionParameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParameter_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, TERM_COLON);
    r = r && fullName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // functionName functionArguments
  public static boolean globalFunctionCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "globalFunctionCall")) return false;
    if (!nextTokenIs(b, "<global function call>", IDENTIFIER, OP_DIAMOND_NAME_L)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GLOBAL_FUNCTION_CALL, "<global function call>");
    r = functionName(b, l + 1);
    r = r && functionArguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KW_GO lambdaExpress
  //     | KW_GO scriptBlock
  //     | KW_GO express
  public static boolean goRunExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goRunExpress")) return false;
    if (!nextTokenIs(b, KW_GO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = goRunExpress_0(b, l + 1);
    if (!r) r = goRunExpress_1(b, l + 1);
    if (!r) r = goRunExpress_2(b, l + 1);
    exit_section_(b, m, GO_RUN_EXPRESS, r);
    return r;
  }

  // KW_GO lambdaExpress
  private static boolean goRunExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goRunExpress_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_GO);
    r = r && lambdaExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_GO scriptBlock
  private static boolean goRunExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goRunExpress_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_GO);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_GO express
  private static boolean goRunExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goRunExpress_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_GO);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KW_IF conditionBlock scriptBlock ((KW_ELSE KW_IF|KW_ELIF) conditionBlock scriptBlock)* (KW_ELSE scriptBlock)?
  public static boolean ifElseExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress")) return false;
    if (!nextTokenIs(b, KW_IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_IF);
    r = r && conditionBlock(b, l + 1);
    r = r && scriptBlock(b, l + 1);
    r = r && ifElseExpress_3(b, l + 1);
    r = r && ifElseExpress_4(b, l + 1);
    exit_section_(b, m, IF_ELSE_EXPRESS, r);
    return r;
  }

  // ((KW_ELSE KW_IF|KW_ELIF) conditionBlock scriptBlock)*
  private static boolean ifElseExpress_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ifElseExpress_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ifElseExpress_3", c)) break;
    }
    return true;
  }

  // (KW_ELSE KW_IF|KW_ELIF) conditionBlock scriptBlock
  private static boolean ifElseExpress_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ifElseExpress_3_0_0(b, l + 1);
    r = r && conditionBlock(b, l + 1);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_ELSE KW_IF|KW_ELIF
  private static boolean ifElseExpress_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, KW_ELSE, KW_IF);
    if (!r) r = consumeToken(b, KW_ELIF);
    exit_section_(b, m, null, r);
    return r;
  }

  // (KW_ELSE scriptBlock)?
  private static boolean ifElseExpress_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress_4")) return false;
    ifElseExpress_4_0(b, l + 1);
    return true;
  }

  // KW_ELSE scriptBlock
  private static boolean ifElseExpress_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifElseExpress_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_ELSE);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KW_IMPORT fullName (OP_DOT_STAR)?
  public static boolean importExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importExpress")) return false;
    if (!nextTokenIs(b, KW_IMPORT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_IMPORT);
    r = r && fullName(b, l + 1);
    r = r && importExpress_2(b, l + 1);
    exit_section_(b, m, IMPORT_EXPRESS, r);
    return r;
  }

  // (OP_DOT_STAR)?
  private static boolean importExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importExpress_2")) return false;
    consumeToken(b, OP_DOT_STAR);
    return true;
  }

  /* ********************************************************** */
  // OP_INCR | OP_DECR
  public static boolean incrDecrAfterRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incrDecrAfterRightPart")) return false;
    if (!nextTokenIs(b, "<incr decr after right part>", OP_DECR, OP_INCR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INCR_DECR_AFTER_RIGHT_PART, "<incr decr after right part>");
    r = consumeToken(b, OP_INCR);
    if (!r) r = consumeToken(b, OP_DECR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // OP_INCR | OP_DECR
  public static boolean incrDecrPrefixOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incrDecrPrefixOperatorPart")) return false;
    if (!nextTokenIs(b, "<incr decr prefix operator part>", OP_DECR, OP_INCR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INCR_DECR_PREFIX_OPERATOR_PART, "<incr decr prefix operator part>");
    r = consumeToken(b, OP_INCR);
    if (!r) r = consumeToken(b, OP_DECR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (TERM_DOT|TERM_OPTION_DOT) IDENTIFIER
  public static boolean instanceFieldValueRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instanceFieldValueRightPart")) return false;
    if (!nextTokenIs(b, "<instance field value right part>", TERM_DOT, TERM_OPTION_DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INSTANCE_FIELD_VALUE_RIGHT_PART, "<instance field value right part>");
    r = instanceFieldValueRightPart_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TERM_DOT|TERM_OPTION_DOT
  private static boolean instanceFieldValueRightPart_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instanceFieldValueRightPart_0")) return false;
    boolean r;
    r = consumeToken(b, TERM_DOT);
    if (!r) r = consumeToken(b, TERM_OPTION_DOT);
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
  // (IDENTIFIER | constString | constRenderString) TERM_COLON express
  //     | variableValue
  public static boolean keyValueExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyValueExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEY_VALUE_EXPRESS, "<key value express>");
    r = keyValueExpress_0(b, l + 1);
    if (!r) r = variableValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (IDENTIFIER | constString | constRenderString) TERM_COLON express
  private static boolean keyValueExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyValueExpress_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = keyValueExpress_0_0(b, l + 1);
    r = r && consumeToken(b, TERM_COLON);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IDENTIFIER | constString | constRenderString
  private static boolean keyValueExpress_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyValueExpress_0_0")) return false;
    boolean r;
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = constString(b, l + 1);
    if (!r) r = constRenderString(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // functionArguments OP_EXTEND_TO scriptBlock
  public static boolean lambdaExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambdaExpress")) return false;
    if (!nextTokenIs(b, TERM_PAREN_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = functionArguments(b, l + 1);
    r = r && consumeToken(b, OP_EXTEND_TO);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, LAMBDA_EXPRESS, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_BRACKET_SQUARE_L (unpackListExpress (TERM_COMMA unpackListExpress)*)? TERM_COMMA? TERM_BRACKET_SQUARE_R
  public static boolean listValueExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress")) return false;
    if (!nextTokenIs(b, TERM_BRACKET_SQUARE_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_BRACKET_SQUARE_L);
    r = r && listValueExpress_1(b, l + 1);
    r = r && listValueExpress_2(b, l + 1);
    r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
    exit_section_(b, m, LIST_VALUE_EXPRESS, r);
    return r;
  }

  // (unpackListExpress (TERM_COMMA unpackListExpress)*)?
  private static boolean listValueExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress_1")) return false;
    listValueExpress_1_0(b, l + 1);
    return true;
  }

  // unpackListExpress (TERM_COMMA unpackListExpress)*
  private static boolean listValueExpress_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unpackListExpress(b, l + 1);
    r = r && listValueExpress_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_COMMA unpackListExpress)*
  private static boolean listValueExpress_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!listValueExpress_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "listValueExpress_1_0_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA unpackListExpress
  private static boolean listValueExpress_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && unpackListExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TERM_COMMA?
  private static boolean listValueExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listValueExpress_2")) return false;
    consumeToken(b, TERM_COMMA);
    return true;
  }

  /* ********************************************************** */
  // OP_AND | KW_AND | OP_OR | KW_OR
  public static boolean logicalLinkOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalLinkOperatorPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGICAL_LINK_OPERATOR_PART, "<logical link operator part>");
    r = consumeToken(b, OP_AND);
    if (!r) r = consumeToken(b, KW_AND);
    if (!r) r = consumeToken(b, OP_OR);
    if (!r) r = consumeToken(b, KW_OR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CURLY_L (unpackMapExpress (TERM_COMMA unpackMapExpress)* )? TERM_COMMA? TERM_CURLY_R
  public static boolean mapValueExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CURLY_L);
    r = r && mapValueExpress_1(b, l + 1);
    r = r && mapValueExpress_2(b, l + 1);
    r = r && consumeToken(b, TERM_CURLY_R);
    exit_section_(b, m, MAP_VALUE_EXPRESS, r);
    return r;
  }

  // (unpackMapExpress (TERM_COMMA unpackMapExpress)* )?
  private static boolean mapValueExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress_1")) return false;
    mapValueExpress_1_0(b, l + 1);
    return true;
  }

  // unpackMapExpress (TERM_COMMA unpackMapExpress)*
  private static boolean mapValueExpress_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unpackMapExpress(b, l + 1);
    r = r && mapValueExpress_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TERM_COMMA unpackMapExpress)*
  private static boolean mapValueExpress_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mapValueExpress_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mapValueExpress_1_0_1", c)) break;
    }
    return true;
  }

  // TERM_COMMA unpackMapExpress
  private static boolean mapValueExpress_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_COMMA);
    r = r && unpackMapExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TERM_COMMA?
  private static boolean mapValueExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapValueExpress_2")) return false;
    consumeToken(b, TERM_COMMA);
    return true;
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
  // OP_MUL | OP_INT_DIV | OP_DIV | OP_MOD
  public static boolean mathMulDivOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathMulDivOperatorPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MATH_MUL_DIV_OPERATOR_PART, "<math mul div operator part>");
    r = consumeToken(b, OP_MUL);
    if (!r) r = consumeToken(b, OP_INT_DIV);
    if (!r) r = consumeToken(b, OP_DIV);
    if (!r) r = consumeToken(b, OP_MOD);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KW_NEW fullName TERM_BRACKET_SQUARE_L constNumber TERM_BRACKET_SQUARE_R (OP_EXTEND_TO listValueExpress)?
  //     | KW_NEW fullName TERM_BRACKET_SQUARE_L  TERM_BRACKET_SQUARE_R (OP_EXTEND_TO listValueExpress)
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

  // KW_NEW fullName TERM_BRACKET_SQUARE_L constNumber TERM_BRACKET_SQUARE_R (OP_EXTEND_TO listValueExpress)?
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

  // (OP_EXTEND_TO listValueExpress)?
  private static boolean newArrayExpress_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newArrayExpress_0_5")) return false;
    newArrayExpress_0_5_0(b, l + 1);
    return true;
  }

  // OP_EXTEND_TO listValueExpress
  private static boolean newArrayExpress_0_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newArrayExpress_0_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_EXTEND_TO);
    r = r && listValueExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // KW_NEW fullName TERM_BRACKET_SQUARE_L  TERM_BRACKET_SQUARE_R (OP_EXTEND_TO listValueExpress)
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

  // OP_EXTEND_TO listValueExpress
  private static boolean newArrayExpress_1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newArrayExpress_1_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_EXTEND_TO);
    r = r && listValueExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // KW_NEW fullName functionArguments (OP_EXTEND_TO mapValueExpress)?
  public static boolean newInstanceExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newInstanceExpress")) return false;
    if (!nextTokenIs(b, KW_NEW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_NEW);
    r = r && fullName(b, l + 1);
    r = r && functionArguments(b, l + 1);
    r = r && newInstanceExpress_3(b, l + 1);
    exit_section_(b, m, NEW_INSTANCE_EXPRESS, r);
    return r;
  }

  // (OP_EXTEND_TO mapValueExpress)?
  private static boolean newInstanceExpress_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newInstanceExpress_3")) return false;
    newInstanceExpress_3_0(b, l + 1);
    return true;
  }

  // OP_EXTEND_TO mapValueExpress
  private static boolean newInstanceExpress_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newInstanceExpress_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_EXTEND_TO);
    r = r && mapValueExpress(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // instanceFunctionCallRightPart // 实例函数调用
  //     | instanceFieldValueRightPart // 实例值获取
  //     | squareQuoteRightPart // 中括号取值表达式
  //     | factorRightPart // 阶乘/百分号后置表达式
  //     | incrDecrAfterRightPart // 后置自增表达式
  //     | mathMulDivOperatorPart express // 数学乘除运算；数学运算，需要从左到右，因此不能提取为子规则
  //     | percentRightPart
  //     | mathAddSubOperatorPart express // 数学加减运算；数学运算，需要从左到右，因此不能提取为子规则
  //     | castAsRightPart // 类型转换
  //     | compareOperatorPart express // 比较运算符
  //     | logicalLinkOperatorPart express // 逻辑连接符
  //     | bitOperatorPart  express // 位运算符号；数学运算，需要从左到右，因此不能提取为子规则
  //     | thirdOperateRightPart // 三目运算符
  //     | pipelineFunctionExpress+ // 管道函数
  //     | assignRightPart
  public static boolean operatorSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR_SEGMENT, "<operator segment>");
    r = instanceFunctionCallRightPart(b, l + 1);
    if (!r) r = instanceFieldValueRightPart(b, l + 1);
    if (!r) r = squareQuoteRightPart(b, l + 1);
    if (!r) r = factorRightPart(b, l + 1);
    if (!r) r = incrDecrAfterRightPart(b, l + 1);
    if (!r) r = operatorSegment_5(b, l + 1);
    if (!r) r = percentRightPart(b, l + 1);
    if (!r) r = operatorSegment_7(b, l + 1);
    if (!r) r = castAsRightPart(b, l + 1);
    if (!r) r = operatorSegment_9(b, l + 1);
    if (!r) r = operatorSegment_10(b, l + 1);
    if (!r) r = operatorSegment_11(b, l + 1);
    if (!r) r = thirdOperateRightPart(b, l + 1);
    if (!r) r = operatorSegment_13(b, l + 1);
    if (!r) r = assignRightPart(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // mathMulDivOperatorPart express
  private static boolean operatorSegment_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_5")) return false;
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
  private static boolean operatorSegment_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compareOperatorPart(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // logicalLinkOperatorPart express
  private static boolean operatorSegment_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = logicalLinkOperatorPart(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // bitOperatorPart  express
  private static boolean operatorSegment_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bitOperatorPart(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pipelineFunctionExpress+
  private static boolean operatorSegment_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorSegment_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pipelineFunctionExpress(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!pipelineFunctionExpress(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "operatorSegment_13", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_MOD
  public static boolean percentRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "percentRightPart")) return false;
    if (!nextTokenIs(b, OP_MOD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_MOD);
    exit_section_(b, m, PERCENT_RIGHT_PART, r);
    return r;
  }

  /* ********************************************************** */
  // OP_PIPELINE ( staticFunctionCall | staticFieldValue | OP_SELF_PIPE? (globalFunctionCall|functionName)   | functionName)
  public static boolean pipelineFunctionExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipelineFunctionExpress")) return false;
    if (!nextTokenIs(b, OP_PIPELINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_PIPELINE);
    r = r && pipelineFunctionExpress_1(b, l + 1);
    exit_section_(b, m, PIPELINE_FUNCTION_EXPRESS, r);
    return r;
  }

  // staticFunctionCall | staticFieldValue | OP_SELF_PIPE? (globalFunctionCall|functionName)   | functionName
  private static boolean pipelineFunctionExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipelineFunctionExpress_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = staticFunctionCall(b, l + 1);
    if (!r) r = staticFieldValue(b, l + 1);
    if (!r) r = pipelineFunctionExpress_1_2(b, l + 1);
    if (!r) r = functionName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_SELF_PIPE? (globalFunctionCall|functionName)
  private static boolean pipelineFunctionExpress_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipelineFunctionExpress_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pipelineFunctionExpress_1_2_0(b, l + 1);
    r = r && pipelineFunctionExpress_1_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_SELF_PIPE?
  private static boolean pipelineFunctionExpress_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipelineFunctionExpress_1_2_0")) return false;
    consumeToken(b, OP_SELF_PIPE);
    return true;
  }

  // globalFunctionCall|functionName
  private static boolean pipelineFunctionExpress_1_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipelineFunctionExpress_1_2_1")) return false;
    boolean r;
    r = globalFunctionCall(b, l + 1);
    if (!r) r = functionName(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // OP_EXCLAM | KW_NOT | OP_BIT_REVERSE | OP_SUB
  public static boolean prefixOperatorPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefixOperatorPart")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIX_OPERATOR_PART, "<prefix operator part>");
    r = consumeToken(b, OP_EXCLAM);
    if (!r) r = consumeToken(b, KW_NOT);
    if (!r) r = consumeToken(b, OP_BIT_REVERSE);
    if (!r) r = consumeToken(b, OP_SUB);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_VISITOR
  public static boolean refValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refValue")) return false;
    if (!nextTokenIs(b, TERM_CONST_VISITOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_VISITOR);
    exit_section_(b, m, REF_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // KW_RETURN express?
  public static boolean returnExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnExpress")) return false;
    if (!nextTokenIs(b, KW_RETURN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_RETURN);
    r = r && returnExpress_1(b, l + 1);
    exit_section_(b, m, RETURN_EXPRESS, r);
    return r;
  }

  // express?
  private static boolean returnExpress_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnExpress_1")) return false;
    express(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // script
  static boolean root(PsiBuilder b, int l) {
    return script(b, l + 1);
  }

  /* ********************************************************** */
  // segment+
  //     | express (TERM_SEMICOLON)? <<eof>>
  //     | commentSegment <<eof>>
  public static boolean script(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT, "<script>");
    r = script_0(b, l + 1);
    if (!r) r = script_1(b, l + 1);
    if (!r) r = script_2(b, l + 1);
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

  // commentSegment <<eof>>
  private static boolean script_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = commentSegment(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CURLY_L script? TERM_CURLY_R
  public static boolean scriptBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBlock")) return false;
    if (!nextTokenIs(b, TERM_CURLY_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CURLY_L);
    r = r && scriptBlock_1(b, l + 1);
    r = r && consumeToken(b, TERM_CURLY_R);
    exit_section_(b, m, SCRIPT_BLOCK, r);
    return r;
  }

  // script?
  private static boolean scriptBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBlock_1")) return false;
    script(b, l + 1);
    return true;
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
  // TERM_BRACKET_SQUARE_L express TERM_BRACKET_SQUARE_R
  public static boolean squareQuoteRightPart(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "squareQuoteRightPart")) return false;
    if (!nextTokenIs(b, TERM_BRACKET_SQUARE_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_BRACKET_SQUARE_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_BRACKET_SQUARE_R);
    exit_section_(b, m, SQUARE_QUOTE_RIGHT_PART, r);
    return r;
  }

  /* ********************************************************** */
  // typeMember IDENTIFIER
  public static boolean staticFieldValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticFieldValue")) return false;
    if (!nextTokenIs(b, "<static field value>", IDENTIFIER, TERM_CONST_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATIC_FIELD_VALUE, "<static field value>");
    r = typeMember(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // typeMember functionName functionArguments
  public static boolean staticFunctionCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticFunctionCall")) return false;
    if (!nextTokenIs(b, "<static function call>", IDENTIFIER, TERM_CONST_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATIC_FUNCTION_CALL, "<static function call>");
    r = typeMember(b, l + 1);
    r = r && functionName(b, l + 1);
    r = r && functionArguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // KW_SYNCHRONIZED  TERM_PAREN_L express TERM_PAREN_R scriptBlock
  public static boolean synchronizedExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "synchronizedExpress")) return false;
    if (!nextTokenIs(b, KW_SYNCHRONIZED)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KW_SYNCHRONIZED, TERM_PAREN_L);
    r = r && express(b, l + 1);
    r = r && consumeToken(b, TERM_PAREN_R);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, SYNCHRONIZED_EXPRESS, r);
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
  // KW_THROW express
  public static boolean throwExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throwExpress")) return false;
    if (!nextTokenIs(b, KW_THROW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_THROW);
    r = r && express(b, l + 1);
    exit_section_(b, m, THROW_EXPRESS, r);
    return r;
  }

  /* ********************************************************** */
  // KW_TRY scriptBlock catchBlock* (KW_FINALLY scriptBlock)?
  public static boolean tryCatchFinallyExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tryCatchFinallyExpress")) return false;
    if (!nextTokenIs(b, KW_TRY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_TRY);
    r = r && scriptBlock(b, l + 1);
    r = r && tryCatchFinallyExpress_2(b, l + 1);
    r = r && tryCatchFinallyExpress_3(b, l + 1);
    exit_section_(b, m, TRY_CATCH_FINALLY_EXPRESS, r);
    return r;
  }

  // catchBlock*
  private static boolean tryCatchFinallyExpress_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tryCatchFinallyExpress_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!catchBlock(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tryCatchFinallyExpress_2", c)) break;
    }
    return true;
  }

  // (KW_FINALLY scriptBlock)?
  private static boolean tryCatchFinallyExpress_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tryCatchFinallyExpress_3")) return false;
    tryCatchFinallyExpress_3_0(b, l + 1);
    return true;
  }

  // KW_FINALLY scriptBlock
  private static boolean tryCatchFinallyExpress_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tryCatchFinallyExpress_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_FINALLY);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TERM_CONST_CLASS
  public static boolean typeClass(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeClass")) return false;
    if (!nextTokenIs(b, TERM_CONST_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TERM_CONST_CLASS);
    exit_section_(b, m, TYPE_CLASS, r);
    return r;
  }

  /* ********************************************************** */
  // typeClass TERM_DOT
  //     | fullName TERM_AT
  //     | fullName OP_SELF_PIPE
  public static boolean typeMember(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeMember")) return false;
    if (!nextTokenIs(b, "<type member>", IDENTIFIER, TERM_CONST_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_MEMBER, "<type member>");
    r = typeMember_0(b, l + 1);
    if (!r) r = typeMember_1(b, l + 1);
    if (!r) r = typeMember_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // typeClass TERM_DOT
  private static boolean typeMember_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeMember_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeClass(b, l + 1);
    r = r && consumeToken(b, TERM_DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // fullName TERM_AT
  private static boolean typeMember_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeMember_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fullName(b, l + 1);
    r = r && consumeToken(b, TERM_AT);
    exit_section_(b, m, null, r);
    return r;
  }

  // fullName OP_SELF_PIPE
  private static boolean typeMember_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeMember_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fullName(b, l + 1);
    r = r && consumeToken(b, OP_SELF_PIPE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_EXTRA? express
  public static boolean unpackListExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unpackListExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNPACK_LIST_EXPRESS, "<unpack list express>");
    r = unpackListExpress_0(b, l + 1);
    r = r && express(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OP_EXTRA?
  private static boolean unpackListExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unpackListExpress_0")) return false;
    consumeToken(b, OP_EXTRA);
    return true;
  }

  /* ********************************************************** */
  // OP_EXTRA express
  //     | keyValueExpress
  public static boolean unpackMapExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unpackMapExpress")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNPACK_MAP_EXPRESS, "<unpack map express>");
    r = unpackMapExpress_0(b, l + 1);
    if (!r) r = keyValueExpress(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OP_EXTRA express
  private static boolean unpackMapExpress_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unpackMapExpress_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_EXTRA);
    r = r && express(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // constValue
  //     | refValue
  //     | variableValue
  //     | typeClass
  public static boolean valueSegment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueSegment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_SEGMENT, "<value segment>");
    r = constValue(b, l + 1);
    if (!r) r = refValue(b, l + 1);
    if (!r) r = variableValue(b, l + 1);
    if (!r) r = typeClass(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean variableValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableValue")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, VARIABLE_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // KW_WHILE conditionBlock scriptBlock
  public static boolean whileExpress(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileExpress")) return false;
    if (!nextTokenIs(b, KW_WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KW_WHILE);
    r = r && conditionBlock(b, l + 1);
    r = r && scriptBlock(b, l + 1);
    exit_section_(b, m, WHILE_EXPRESS, r);
    return r;
  }

}
