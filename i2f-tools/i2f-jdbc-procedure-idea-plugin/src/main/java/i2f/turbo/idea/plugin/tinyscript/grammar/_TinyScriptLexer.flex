package i2f.turbo.idea.plugin.tinyscript.grammar.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.*;

%%

%{
  public _TinyScriptLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _TinyScriptLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TERM_COMMENT_SINGLE_LINE="//"[^\n]*\n
TERM_COMMENT_MULTI_LINE="/"\*([^*]|(\*+([^*/])))*\*+"/"
TERM_CONST_STRING_MULTILINE=```(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)?[ \t\r]*\n(([^\\]|(\\.))*?)```
TERM_CONST_STRING_MULTILINE_QUOTE=\"\"\"(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)?[ \t\r]*\n(([^\\]|(\\.))*?)\"\"\"
TERM_CONST_STRING_RENDER=[rR]\"((\\\")+|[^\"])*\"
TERM_CONST_STRING_RENDER_SINGLE=[rR]'((\')+|[^'])*'
TERM_CONST_STRING=\"((\\\")+|[^\"])*\"
TERM_CONST_STRING_SINGLE='((\\\')+|[^'])*'
TERM_CONST_BOOLEAN=true|false
TERM_CONST_TYPE_CLASS=(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\.class
REF_EXPRESS=\$(\!)?\{[^}]*}
TERM_CONST_NUMBER_SCIEN_2=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[fF]?
TERM_CONST_NUMBER_SCIEN_1=[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[lL]?
TERM_CONST_NUMBER_FLOAT=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[fF]?
TERM_CONST_NUMBER=[0-9]+[0-9_]*[lL]?
TERM_CONST_NUMBER_HEX=0[xX][0-9a-fA-F_]+[lL]?
TERM_CONST_NUMBER_OTC=0[tT][0-7_]+[lL]?
TERM_CONST_NUMBER_BIN=0[bB][0-7_]+[lL]?
TERM_INTEGER=[0-9]+[0-9_]*
NAMING=(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)
ROUTE_NAMING=(([a-zA-Z_][a-zA-Z0-9_]*(\[[0-9]+[0-9_]*])?)(\.[a-zA-Z_][a-zA-Z0-9_]*(\[[0-9]+[0-9_]*])?)*)
ID=[a-zA-Z_][a-zA-Z0-9_]*
TERM_DIGIT=[0-9]
WORD=[a-zA-Z_0-9]+
WS=[ \t\r\n]+

%%
<YYINITIAL> {
  {WHITE_SPACE}                             { return WHITE_SPACE; }

  "null"                                    { return TERM_CONST_NULL; }
  "("                                       { return TERM_PAREN_L; }
  ")"                                       { return TERM_PAREN_R; }
  ","                                       { return TERM_COMMA; }
  "."                                       { return TERM_DOT; }
  "$"                                       { return TERM_DOLLAR; }
  "{"                                       { return TERM_CURLY_L; }
  "}"                                       { return TERM_CURLY_R; }
  ";"                                       { return TERM_SEMICOLON; }
  ":"                                       { return TERM_COLON; }
  "["                                       { return TERM_BRACKET_SQUARE_L; }
  "]"                                       { return TERM_BRACKET_SQUARE_R; }
  "?"                                       { return TERM_QUESTION; }
  "#"                                       { return TERM_SHARP; }
  "*"                                       { return OP_MUL; }
  "/"                                       { return OP_DIV; }
  "%"                                       { return OP_MOD; }
  "in"                                      { return OP_IN; }
  "notin"                                   { return OP_NOT_IN; }
  "as"                                      { return OP_AS; }
  "cast"                                    { return OP_CAST; }
  "is"                                      { return OP_IS; }
  "instanceof"                              { return OP_INSTANCE_OF; }
  "typeof"                                  { return OP_TYPE_OF; }
  "+"                                       { return OP_ADD; }
  "-"                                       { return OP_SUB; }
  ">="                                      { return OP_GTE; }
  "gte"                                     { return OP_GTE_STR; }
  "<="                                      { return OP_LTE; }
  "lte"                                     { return OP_LTE_STR; }
  "!="                                      { return OP_NE; }
  "ne"                                      { return OP_NE_STR; }
  "<>"                                      { return OP_NEQ; }
  "neq"                                     { return OP_NEQ_STR; }
  "=="                                      { return OP_EQ; }
  "eq"                                      { return OP_EQ_STR; }
  ">"                                       { return OP_GT; }
  "gt"                                      { return OP_GT_STR; }
  "<"                                       { return OP_LT; }
  "lt"                                      { return OP_LT_STR; }
  "&&"                                      { return OP_AND; }
  "and"                                     { return OP_AND_STR; }
  "||"                                      { return OP_OR; }
  "or"                                      { return OP_OR_STR; }
  "!"                                       { return OP_EXCLAM; }
  "not"                                     { return OP_NOT; }
  "="                                       { return OP_ASSIGN; }
  "+="                                      { return OP_ASSIGN_ADD; }
  "-="                                      { return OP_ASSIGN_SUB; }
  "*="                                      { return OP_ASSIGN_MUL; }
  "/="                                      { return OP_ASSIGN_DIV; }
  "%="                                      { return OP_ASSIGN_MOD; }
  "|"                                       { return OP_VERTICAL_BAR; }
  "class"                                   { return KEY_CLASS; }
  "debugger"                                { return KEY_DEBUGGER; }
  "try"                                     { return KEY_TRY; }
  "catch"                                   { return KEY_CATCH; }
  "finally"                                 { return KEY_FINALLY; }
  "throw"                                   { return KEY_THROW; }
  "break"                                   { return KEY_BREAK; }
  "continue"                                { return KEY_CONTINUE; }
  "return"                                  { return KEY_RETURN; }
  "while"                                   { return KEY_WHILE; }
  "for"                                     { return KEY_FOR; }
  "foreach"                                 { return KEY_FOREACH; }
  "if"                                      { return KEY_IF; }
  "else"                                    { return KEY_ELSE; }
  "new"                                     { return KEY_NEW; }
  "KEY_ELIF"                                { return KEY_ELIF; }

  {TERM_COMMENT_SINGLE_LINE}                { return TERM_COMMENT_SINGLE_LINE; }
  {TERM_COMMENT_MULTI_LINE}                 { return TERM_COMMENT_MULTI_LINE; }
  {TERM_CONST_STRING_MULTILINE}             { return TERM_CONST_STRING_MULTILINE; }
  {TERM_CONST_STRING_MULTILINE_QUOTE}       { return TERM_CONST_STRING_MULTILINE_QUOTE; }
  {TERM_CONST_STRING_RENDER}                { return TERM_CONST_STRING_RENDER; }
  {TERM_CONST_STRING_RENDER_SINGLE}         { return TERM_CONST_STRING_RENDER_SINGLE; }
  {TERM_CONST_STRING}                       { return TERM_CONST_STRING; }
  {TERM_CONST_STRING_SINGLE}                { return TERM_CONST_STRING_SINGLE; }
  {TERM_CONST_BOOLEAN}                      { return TERM_CONST_BOOLEAN; }
  {TERM_CONST_TYPE_CLASS}                   { return TERM_CONST_TYPE_CLASS; }
  {REF_EXPRESS}                             { return REF_EXPRESS; }
  {TERM_CONST_NUMBER_SCIEN_2}               { return TERM_CONST_NUMBER_SCIEN_2; }
  {TERM_CONST_NUMBER_SCIEN_1}               { return TERM_CONST_NUMBER_SCIEN_1; }
  {TERM_CONST_NUMBER_FLOAT}                 { return TERM_CONST_NUMBER_FLOAT; }
  {TERM_CONST_NUMBER}                       { return TERM_CONST_NUMBER; }
  {TERM_CONST_NUMBER_HEX}                   { return TERM_CONST_NUMBER_HEX; }
  {TERM_CONST_NUMBER_OTC}                   { return TERM_CONST_NUMBER_OTC; }
  {TERM_CONST_NUMBER_BIN}                   { return TERM_CONST_NUMBER_BIN; }
  {TERM_INTEGER}                            { return TERM_INTEGER; }
  {NAMING}                                  { return NAMING; }
  {ROUTE_NAMING}                            { return ROUTE_NAMING; }
  {ID}                                      { return ID; }
  {TERM_DIGIT}                              { return TERM_DIGIT; }
  {WORD}                                    { return WORD; }
  {WS}                                      { return WS; }

}

[^] { return BAD_CHARACTER; }
