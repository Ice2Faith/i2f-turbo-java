package i2f.turbo.idea.plugin.ognl.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.*;

%%

%{
  public _OgnlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _OgnlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TERM_CONST_STRING=\"((\\\")+|[^\"])*\"
TERM_CONST_STRING_SINGLE='((\\\')+|[^'])*'
KW_CONST_BOOLEAN=true|false
TERM_CONST_NUMBER_HEX=0[xX][0-9a-fA-F_]+[lLhH]?
TERM_CONST_NUMBER_OTC=0[oOtT][0-7_]+[lLhH]?
TERM_CONST_NUMBER_BIN=0[bB][0-7_]+[lLhH]?
TERM_CONST_NUMBER_SCIEN_2=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[fFhH]?
TERM_CONST_NUMBER_SCIEN_1=[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[lLhH]?
TERM_CONST_NUMBER_FLOAT=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[fFhH]?
TERM_CONST_NUMBER=[0-9]+[0-9_]*[lLhH]?
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
WORD=[a-zA-Z_0-9]+
WS=[ \t\r\n]+

%%
<YYINITIAL> {
  {WHITE_SPACE}                     { return WHITE_SPACE; }

  "null"                            { return KW_CONST_NULL; }
  "new"                             { return KW_NEW; }
  "not"                             { return KW_NOT; }
  "gte"                             { return KW_GTE; }
  "lte"                             { return KW_LTE; }
  "gt"                              { return KW_GT; }
  "lt"                              { return KW_LT; }
  "neq"                             { return KW_NEQ; }
  "ne"                              { return KW_NE; }
  "eq"                              { return KW_EQ; }
  "in"                              { return KW_IN; }
  "instanceof"                      { return KW_INSTANCEOF; }
  "shl"                             { return KW_SHL; }
  "shr"                             { return KW_SHR; }
  "ushr"                            { return KW_USHR; }
  "xor"                             { return KW_XOR; }
  "band"                            { return KW_BAND; }
  "bor"                             { return KW_BOR; }
  "and"                             { return KW_AND; }
  "or"                              { return KW_OR; }
  "("                               { return TERM_PAREN_L; }
  ")"                               { return TERM_PAREN_R; }
  ","                               { return TERM_COMMA; }
  "."                               { return TERM_DOT; }
  "{"                               { return TERM_CURLY_L; }
  "}"                               { return TERM_CURLY_R; }
  ":"                               { return TERM_COLON; }
  "["                               { return TERM_BRACKET_SQUARE_L; }
  "]"                               { return TERM_BRACKET_SQUARE_R; }
  "?"                               { return TERM_QUESTION; }
  "$"                               { return TERM_DOLLAR; }
  "#"                               { return TERM_SHARP; }
  "@"                               { return TERM_AT; }
  "*"                               { return OP_MUL; }
  "/"                               { return OP_DIV; }
  "%"                               { return OP_MOD; }
  "+"                               { return OP_ADD; }
  "-"                               { return OP_SUB; }
  ">="                              { return OP_GTE; }
  "<="                              { return OP_LTE; }
  "!="                              { return OP_NE; }
  "=="                              { return OP_EQ; }
  ">"                               { return OP_GT; }
  "<"                               { return OP_LT; }
  "&&"                              { return OP_AND; }
  "||"                              { return OP_OR; }
  "!"                               { return OP_EXCLAM; }
  "<<"                              { return OP_BIT_LMOV; }
  ">>>"                             { return OP_BIT_RSMOV; }
  ">>"                              { return OP_BIT_RMOV; }
  "^"                               { return OP_BIT_XOR; }
  "&"                               { return OP_BIT_AND; }
  "|"                               { return OP_BIT_OR; }
  "~"                               { return OP_BIT_REVERSE; }
  "="                               { return OP_ASSIGN; }

  {TERM_CONST_STRING}               { return TERM_CONST_STRING; }
  {TERM_CONST_STRING_SINGLE}        { return TERM_CONST_STRING_SINGLE; }
  {KW_CONST_BOOLEAN}                { return KW_CONST_BOOLEAN; }
  {TERM_CONST_NUMBER_HEX}           { return TERM_CONST_NUMBER_HEX; }
  {TERM_CONST_NUMBER_OTC}           { return TERM_CONST_NUMBER_OTC; }
  {TERM_CONST_NUMBER_BIN}           { return TERM_CONST_NUMBER_BIN; }
  {TERM_CONST_NUMBER_SCIEN_2}       { return TERM_CONST_NUMBER_SCIEN_2; }
  {TERM_CONST_NUMBER_SCIEN_1}       { return TERM_CONST_NUMBER_SCIEN_1; }
  {TERM_CONST_NUMBER_FLOAT}         { return TERM_CONST_NUMBER_FLOAT; }
  {TERM_CONST_NUMBER}               { return TERM_CONST_NUMBER; }
  {IDENTIFIER}                      { return IDENTIFIER; }
  {WORD}                            { return WORD; }
  {WS}                              { return WS; }

}

[^] { return BAD_CHARACTER; }
