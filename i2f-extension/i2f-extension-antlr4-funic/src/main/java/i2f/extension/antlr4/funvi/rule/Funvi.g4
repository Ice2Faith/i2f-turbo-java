grammar Funvi;

@header {
    package i2f.extension.antlr4.funvi.grammar;
}

REF_VALUE: (DOLLAR|SHARP) (EXCLAIM)? '{' ( '\\' . | ~[}])* '}';
BLOCK_END: SHARP SHARP;
BLOCK_ELSE: SHARP 'else';
BLOCK_IF: SHARP 'if';
BLOCK_HEAD: SHARP IDENTIFIER;
IDENTIFIER: [a-zA-Z0-9_.]+;
WHITESPACE: [ \t\n\r]+;
PAREN_L: '(';
PAREN_R: ')';
COMMA: ',';
SHARP: '#';
DOLLAR: '$';
EXCLAIM: '!';
COLON: ':';
TEXT: .+?;

root:
    segment* EOF
;

segment:
    block
    | value
    | text
;

block:
    ifBlock // 单独处理 if 多分支块
    | commonBlock
;

commonBlock:
    blockHead PAREN_L parameters? PAREN_R (blockBody BLOCK_END)?
;

ifBlock:
    BLOCK_IF PAREN_L parameters? PAREN_R blockBody elseBlock* BLOCK_END
;

elseBlock:
    BLOCK_ELSE PAREN_L parameters? PAREN_R blockBody
;

blockHead:
    BLOCK_HEAD
;

blockBody:
    segment*
;

parameters:
    parameter (COMMA parameter)*
;

parameter:
    (IDENTIFIER COLON)? (REF_VALUE | IDENTIFIER)
;


value:
    REF_VALUE
    ;

text:
    content+
;

content:
    IDENTIFIER
    |TEXT
    |WHITESPACE
    | COMMA
    | PAREN_L
    | PAREN_R
    | DOLLAR
    | SHARP
    | EXCLAIM
    | COLON
;