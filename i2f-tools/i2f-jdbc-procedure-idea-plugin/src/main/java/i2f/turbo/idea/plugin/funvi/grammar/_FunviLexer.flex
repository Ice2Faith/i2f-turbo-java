package i2f.turbo.idea.plugin.funvi.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.*;

%%

%{
  public _FunviLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _FunviLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TERM_REF_VALUE=[$|#][!]?[{]((\\.)+|[^}])*}
TERM_BLOCK_END=[#][#]
TERM_BLOCK_ELSE=[#]else
TERM_BLOCK_IF=[#]if
TERM_BLOCK_HEAD=[#][a-zA-Z0-9_\\.]+
TERM_IDENTIFIER=[a-zA-Z0-9_\\.]+
TERM_WHITESPACE=[ \t\n\r]+
TERM_TEXT=.+?

%%
<YYINITIAL> {
  {WHITE_SPACE}           { return WHITE_SPACE; }

  "("                     { return OP_PAREN_L; }
  ")"                     { return OP_PAREN_R; }
  ","                     { return OP_COMMA; }
  "#"                     { return OP_SHARP; }
  "$"                     { return OP_DOLLAR; }
  "!"                     { return OP_EXCLAIM; }
  ":"                     { return OP_COLON; }

  {TERM_REF_VALUE}        { return TERM_REF_VALUE; }
  {TERM_BLOCK_END}        { return TERM_BLOCK_END; }
  {TERM_BLOCK_ELSE}       { return TERM_BLOCK_ELSE; }
  {TERM_BLOCK_IF}         { return TERM_BLOCK_IF; }
  {TERM_BLOCK_HEAD}       { return TERM_BLOCK_HEAD; }
  {TERM_IDENTIFIER}       { return TERM_IDENTIFIER; }
  {TERM_WHITESPACE}       { return TERM_WHITESPACE; }
  {TERM_TEXT}             { return TERM_TEXT; }

}

[^] { return BAD_CHARACTER; }
