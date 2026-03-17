grammar OracleGrammar;

@header {
    package i2f.extension.antlr4.xproc4j.oracle.grammar;
}

TERM_COMMENT_SINGLE_LINE: '--' (~('\n'))* -> skip;

TERM_COMMENT_MULTI_LINE: '/*' .*? '*/' -> skip;

TERM_CONST_STRING_SINGLE: '\'' ('\'\'' | ~[\\'])* '\'';

TERM_CONST_NULL:
    'null'
    ;

TERM_CONST_NUMBER_SCIEN_2:
     TERM_CONST_NUMBER_FLOAT CH_E TERM_INTEGER [fF]?
    ;

TERM_CONST_NUMBER_SCIEN_1:
     TERM_INTEGER CH_E TERM_INTEGER [lL]?
    ;

TERM_CONST_NUMBER_FLOAT:
    TERM_INTEGER '.' TERM_INTEGER [fF]?
    ;

TERM_CONST_NUMBER:
   TERM_INTEGER  [lL]?
;

TERM_INTEGER:
    (TERM_DIGIT)+ ('_' TERM_DIGIT+)*
    ;


KEY_AND: [aA][nN][dD];
KEY_OR: [oO][rR];
KEY_IF: [iI][fF];
KEY_THEN: [tT][hH][eE][nN];
KEY_ELSE: [eE][lL][sS][eE];
KEY_END: [eE][nN][dD];
KEY_COMMIT:[cC][oO][mM][mM][iI][tT];
KEY_ROLLBACK:[rR][oO][lL][lL][bB][aA][cC][kK];
KEY_EXECUTE:[eE][xX][eE][cC][uU][tT][eE];
KEY_IMMEDIATE:[iI][mM][mM][eE][dD][iI][aA][tT][eE];
KEY_INTO:[iI][nN][tT][oO];
KEY_IS: [iI][sS];
KEY_LIKE: [lL][iI][kK][eE];
KEY_NOT: [nN][oO][tT];
KEY_BETWEEN: [bB][eE][tT][wW][eE][eE][nN];
KEY_TO: [tT][oO];
KEY_DEFAULT: [dD][eE][fF][aA][uU][lL][tT];
KEY_CREATE: [cC][rR][eE][aA][tT][eE];
KEY_REPLACE: [rR][eE][pP][lL][aA][cC][eE];
KEY_FUNCTION: [fF][uU][nN][cC][tT][iI][oO][nN];
KEY_PROCEDURE: [pP][rR][oO][cC][eE][dD][uU][rR][eE];
KEY_IN: [iI][nN];
KEY_OUT: [oO][uU][tT];

fragment TERM_DIGIT:[0-9]; // 数字
fragment CH_E: [eE] ('-')?;
fragment ID       : [a-zA-Z_$][a-zA-Z0-9_$]* ;
IDENTIFIER: ID ('.' ID)*;


WS       : [ \t\r\n]+ -> skip ;

convert:
    script EOF
;

script:
    segment (';' segment)* ';' ?
;

segment:
    executeImmediadeVariableSegment
    |declareProcedureSegment
    |declareVariableSegment
    | assignSegment
    | functionSegment
    | ifElseSegment
    | commitSegment
    | rollbackSegment
    | variableSegment
    | conditionSegment
;

declareProcedureSegment:
    KEY_CREATE (KEY_OR KEY_REPLACE) (KEY_FUNCTION|KEY_PROCEDURE) IDENTIFIER '(' argumentDeclareListSegment? ')'
    ;

argumentDeclareListSegment:
    argumentDeclareSegment (',' argumentDeclareSegment)*
;

argumentDeclareSegment:
    IDENTIFIER (KEY_IN|KEY_OUT)? IDENTIFIER
;

declareVariableSegment:
    sqlIdentifier sqlDataType (KEY_DEFAULT variableSegment)?
;

sqlDataType:
    sqlIdentifier ('(' sqlNumber (',' sqlNumber)? ')')?
;

conditionSegment:
    variableSegment ('>=' | '<=' | '!=' | '<>' | '>' | '<' | '=' | KEY_LIKE) variableSegment
    | conditionIsNullSegment
    | conditionIsNotNullSegment
    | conditionNotLikeSegment
    | conditionBetweenSegment
    | conditionInSegment
    | conditionNotInSegment
    ;

conditionInSegment:
    variableSegment KEY_IN '(' variableListSegment ')'
    ;

conditionNotInSegment:
    variableSegment KEY_NOT KEY_IN '(' variableListSegment ')'
    ;

variableListSegment:
    variableSegment (',' variableSegment)*
;

conditionBetweenSegment:
    variableSegment KEY_BETWEEN variableSegment KEY_TO variableSegment
    ;

conditionNotLikeSegment:
    variableSegment KEY_NOT KEY_LIKE variableSegment
    ;

conditionIsNotNullSegment:
    variableSegment KEY_IS KEY_NOT TERM_CONST_NULL
    ;

conditionIsNullSegment:
    variableSegment KEY_IS TERM_CONST_NULL
    ;


conditionCompositeSegment:
    conditionSegment (KEY_AND|KEY_OR conditionSegment)*
;

ifElseSegment:
    KEY_IF conditionCompositeSegment KEY_THEN script (KEY_ELSE KEY_IF conditionCompositeSegment KEY_THEN script)* (KEY_ELSE script)? KEY_END KEY_IF?
;

variableSegment:
    variableSegment ('||' variableSegment)+
    | sqlString
    | sqlNumber
    | sqlNull
    | sqlIdentifier
;

functionSegment:
    sqlIdentifier '(' argumentListSegment? ')'
;

argumentListSegment:
    variableSegment (',' variableSegment)*
;

commitSegment:
    KEY_COMMIT
    ;

rollbackSegment:
    KEY_ROLLBACK
    ;

executeImmediadeVariableSegment:
    KEY_EXECUTE KEY_IMMEDIATE IDENTIFIER (KEY_INTO IDENTIFIER)?
;

assignSegment:
    sqlIdentifier (':='|'=') segment
;

sqlNull:
    TERM_CONST_NULL
    ;

sqlIdentifier:
    IDENTIFIER
;

sqlNumber:
       TERM_CONST_NUMBER_SCIEN_2
        | TERM_CONST_NUMBER_SCIEN_1
        | TERM_CONST_NUMBER_FLOAT
        | TERM_CONST_NUMBER
;

sqlString:
    TERM_CONST_STRING_SINGLE
    ;