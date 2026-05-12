package i2f.turbo.idea.plugin.funic.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

%%

%{
  public _FunicLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _FunicLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TERM_COMMENT_SINGLE_LINE="//"[^\n]*(\n|\$)
TERM_COMMENT_MULTI_LINE="/"\*([^*]|(\*+([^*/])))*\*+"/"
TERM_CONST_STRING_MULTILINE=```(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)?[ \t\r]*\n(([^\\]|(\\.))*?)```
TERM_CONST_STRING_MULTILINE_QUOTE=\"\"\"(([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*)?[ \t\r]*\n(([^\\]|(\\.))*?)\"\"\"
TERM_CONST_STRING_RENDER=[rR]\"((\\\")+|[^\"])*\"
TERM_CONST_STRING_RENDER_SINGLE=[rR]'((\')+|[^'])*'
TERM_CONST_STRING=\"((\\\")+|[^\"])*\"
TERM_CONST_STRING_SINGLE='((\\\')+|[^'])*'
TERM_CONST_VISITOR=\$(\!)?\{[^}]*}
TERM_CONST_NUMBER_HEX=0[xX][0-9a-fA-F_]+[lL]?
TERM_CONST_NUMBER_OTC=0[oOtT][0-7_]+[lL]?
TERM_CONST_NUMBER_BIN=0[bB][0-7_]+[lL]?
TERM_CONST_NUMBER_SCIEN_2=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[fF]?
TERM_CONST_NUMBER_SCIEN_1=[0-9]+[0-9_]*[eE][-]?[0-9]+[0-9_]*[lL]?
TERM_CONST_NUMBER_FLOAT=[0-9]+[0-9_]*\.[0-9]+[0-9_]*[fF]?
TERM_CONST_NUMBER=[0-9]+[0-9_]*[lL]?
KW_CONST_BOOLEAN=true|false
TERM_CONST_CLASS=([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z_][a-zA-Z0-9_]*)*.class
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
WORD=[a-zA-Z_0-9]+
WS=[ \t\r\n]+

%%
<YYINITIAL> {
  {WHITE_SPACE}                             { return WHITE_SPACE; }

  "null"                                    { return KW_CONST_NULL; }
  "class"                                   { return KW_CONST_CLASS; }
  "func"                                    { return KW_FUNC; }
  "def"                                     { return KW_DEF; }
  "try"                                     { return KW_TRY; }
  "catch"                                   { return KW_CATCH; }
  "finally"                                 { return KW_FINALLY; }
  "throw"                                   { return KW_THROW; }
  "return"                                  { return KW_RETURN; }
  "continue"                                { return KW_CONTINUE; }
  "break"                                   { return KW_BREAK; }
  "for"                                     { return KW_FOR; }
  "do"                                      { return KW_DO; }
  "while"                                   { return KW_WHILE; }
  "if"                                      { return KW_IF; }
  "elif"                                    { return KW_ELIF; }
  "else"                                    { return KW_ELSE; }
  "as"                                      { return KW_AS; }
  "new"                                     { return KW_NEW; }
  "not"                                     { return KW_NOT; }
  "teq"                                     { return KW_TEQ; }
  "tneq"                                    { return KW_TNEQ; }
  "gte"                                     { return KW_GTE; }
  "lte"                                     { return KW_LTE; }
  "gt"                                      { return KW_GT; }
  "lt"                                      { return KW_LT; }
  "neq"                                     { return KW_NEQ; }
  "ne"                                      { return KW_NE; }
  "eq"                                      { return KW_EQ; }
  "in"                                      { return KW_IN; }
  "instanceof"                              { return KW_INSTANCEOF; }
  "is"                                      { return KW_IS; }
  "and"                                     { return KW_AND; }
  "or"                                      { return KW_OR; }
  "go"                                      { return KW_GO; }
  "synchronized"                            { return KW_SYNCHRONIZED; }
  "import"                                  { return KW_IMPORT; }
  "debugger"                                { return KW_DEBUGGER; }
  "("                                       { return TERM_PAREN_L; }
  ")"                                       { return TERM_PAREN_R; }
  ","                                       { return TERM_COMMA; }
  "?."                                      { return TERM_OPTION_DOT; }
  "."                                       { return TERM_DOT; }
  "{"                                       { return TERM_CURLY_L; }
  "}"                                       { return TERM_CURLY_R; }
  ";"                                       { return TERM_SEMICOLON; }
  ":"                                       { return TERM_COLON; }
  "["                                       { return TERM_BRACKET_SQUARE_L; }
  "]"                                       { return TERM_BRACKET_SQUARE_R; }
  "?"                                       { return TERM_QUESTION; }
  "#"                                       { return TERM_SHARP; }
  "@"                                       { return TERM_AT; }
  "..."                                     { return OP_EXTRA; }
  "->"                                      { return OP_EXTEND_TO; }
  "<-"                                      { return OP_RECV_FROM; }
  ".*"                                      { return OP_DOT_STAR; }
  "<?"                                      { return OP_DIAMOND_NAME_L; }
  "?>"                                      { return OP_DIAMOND_NAME_R; }
  "*"                                       { return OP_MUL; }
  "//"                                      { return OP_INT_DIV; }
  "/"                                       { return OP_DIV; }
  "%"                                       { return OP_MOD; }
  "++"                                      { return OP_INCR; }
  "--"                                      { return OP_DECR; }
  "+"                                       { return OP_ADD; }
  "-"                                       { return OP_SUB; }
  ">="                                      { return OP_GTE; }
  "<="                                      { return OP_LTE; }
  "!=="                                     { return OP_TNEQ; }
  "!="                                      { return OP_NE; }
  "<>"                                      { return OP_NEQ; }
  "==="                                     { return OP_TEQ; }
  "=="                                      { return OP_EQ; }
  ">"                                       { return OP_GT; }
  "<"                                       { return OP_LT; }
  "&&"                                      { return OP_AND; }
  "||"                                      { return OP_OR; }
  "!"                                       { return OP_EXCLAM; }
  "<<"                                      { return OP_BIT_LMOV; }
  ">>>"                                     { return OP_BIT_RSMOV; }
  ">>"                                      { return OP_BIT_RMOV; }
  "^"                                       { return OP_BIT_XOR; }
  "&"                                       { return OP_BIT_AND; }
  "|"                                       { return OP_BIT_OR; }
  "~"                                       { return OP_BIT_REVERSE; }
  "="                                       { return OP_ASSIGN; }
  "+="                                      { return OP_ASSIGN_ADD; }
  "-="                                      { return OP_ASSIGN_SUB; }
  "*="                                      { return OP_ASSIGN_MUL; }
  "/="                                      { return OP_ASSIGN_DIV; }
  "%="                                      { return OP_ASSIGN_MOD; }
  "?="                                      { return OP_ASSIGN_IFNULL; }
  ".="                                      { return OP_ASSIGN_NOTNULL; }
  "|>"                                      { return OP_PIPELINE; }
  "::"                                      { return OP_SELF_PIPE; }

  {TERM_COMMENT_SINGLE_LINE}                { return TERM_COMMENT_SINGLE_LINE; }
  {TERM_COMMENT_MULTI_LINE}                 { return TERM_COMMENT_MULTI_LINE; }
  {TERM_CONST_STRING_MULTILINE}             { return TERM_CONST_STRING_MULTILINE; }
  {TERM_CONST_STRING_MULTILINE_QUOTE}       { return TERM_CONST_STRING_MULTILINE_QUOTE; }
  {TERM_CONST_STRING_RENDER}                { return TERM_CONST_STRING_RENDER; }
  {TERM_CONST_STRING_RENDER_SINGLE}         { return TERM_CONST_STRING_RENDER_SINGLE; }
  {TERM_CONST_STRING}                       { return TERM_CONST_STRING; }
  {TERM_CONST_STRING_SINGLE}                { return TERM_CONST_STRING_SINGLE; }
  {TERM_CONST_VISITOR}                      { return TERM_CONST_VISITOR; }
  {TERM_CONST_NUMBER_HEX}                   { return TERM_CONST_NUMBER_HEX; }
  {TERM_CONST_NUMBER_OTC}                   { return TERM_CONST_NUMBER_OTC; }
  {TERM_CONST_NUMBER_BIN}                   { return TERM_CONST_NUMBER_BIN; }
  {TERM_CONST_NUMBER_SCIEN_2}               { return TERM_CONST_NUMBER_SCIEN_2; }
  {TERM_CONST_NUMBER_SCIEN_1}               { return TERM_CONST_NUMBER_SCIEN_1; }
  {TERM_CONST_NUMBER_FLOAT}                 { return TERM_CONST_NUMBER_FLOAT; }
  {TERM_CONST_NUMBER}                       { return TERM_CONST_NUMBER; }
  {KW_CONST_BOOLEAN}                        { return KW_CONST_BOOLEAN; }
  {TERM_CONST_CLASS}                        { return TERM_CONST_CLASS; }
  {IDENTIFIER}                              { return IDENTIFIER; }
  {WORD}                                    { return WORD; }
  {WS}                                      { return WS; }

}

[^] { return BAD_CHARACTER; }
