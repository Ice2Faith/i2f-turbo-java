grammar Ognl;

// 不区分大小写时的字符
fragment A:[aA];
fragment B:[bB];
fragment C:[cC];
fragment D:[dD];
fragment E:[eE];
fragment F:[fF];
fragment G:[gG];
fragment H:[hH];
fragment I:[iI];
fragment J:[jJ];
fragment K:[kK];
fragment L:[lL];
fragment M:[mM];
fragment N:[nN];
fragment O:[oO];
fragment P:[pP];
fragment Q:[qQ];
fragment R:[rR];
fragment S:[sS];
fragment T:[tT];
fragment U:[uU];
fragment V:[vV];
fragment W:[wW];
fragment X:[xX];
fragment Y:[yY];
fragment Z:[zZ];

///////////////////////////////////////////
// tokens
//////////////////////////////////////////

// 渲染文本
fragment ESCAPED_CHAR: '\\' .;
// 普通文本
TERM_CONST_STRING: '"' (ESCAPED_CHAR | ~[\\"])* '"';
TERM_CONST_STRING_SINGLE: '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

// 布尔
KW_CONST_BOOLEAN:
    'true' | 'false'
    ;

// 空值
KW_CONST_NULL:
    'null'
    ;


// 数值
fragment CH_E: E ('-'|'+')?;

fragment CH_0X: '0' X;
fragment CH_0O: '0' O;
fragment CH_0T: '0' T;
fragment CH_0B: '0' B;


fragment TERM_DIGIT:[0-9]; // 数字
fragment TERM_HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
fragment TERM_OTC_LETTER:[0-7_]; // 8进制字符
fragment TERM_BIN_LETTER:[01_]; // 2进制字符

fragment TERM_INTEGER:
    (TERM_DIGIT)+ ('_' TERM_DIGIT+)*
    ;

fragment ID       : [a-zA-Z_][a-zA-Z0-9_]* ;

TERM_CONST_NUMBER_HEX:
    CH_0X (TERM_HEX_LETTER)+ (L|H)?
    ;


TERM_CONST_NUMBER_OTC:
    (CH_0O|CH_0T) (TERM_OTC_LETTER)+ (L|H)?
    ;


TERM_CONST_NUMBER_BIN:
    CH_0B (TERM_BIN_LETTER)+ (L|H)?
    ;

TERM_CONST_NUMBER_SCIEN_2:
     TERM_CONST_NUMBER_FLOAT CH_E TERM_INTEGER (F|H)?
    ;

TERM_CONST_NUMBER_SCIEN_1:
     TERM_INTEGER CH_E TERM_INTEGER (F|H)?
    ;

TERM_CONST_NUMBER_FLOAT:
    TERM_INTEGER '.' TERM_INTEGER (F|H)?
    ;

TERM_CONST_NUMBER:
   TERM_INTEGER  (L|H)?
;
///////////////////////////////////////////
// tokens
//////////////////////////////////////////
KW_NEW: 'new';

KW_NOT: 'not';

KW_GTE: 'gte';
KW_LTE: 'lte';
KW_GT: 'gt';
KW_LT: 'lt';
KW_NEQ: 'neq';
KW_EQ: 'eq';
KW_IN: 'in';
KW_INSTANCEOF: 'instanceof';

KW_SHL: 'shl';
KW_SHR: 'shr';
KW_USHR: 'ushr';
KW_XOR: 'xor';
KW_BAND: 'band';
KW_BOR: 'bor';

KW_AND: 'and';
KW_OR: 'or';



IDENTIFIER: ID;

WS       : [ \t\r\n]+ -> skip ;

root:
    express EOF
;


express:
    circleExpress // 括号表达式
    | newArrayExpress // 新建数组
    | newInstanceExpress // 新建实例对象
    | staticFunctionCall
    | staticPropertyExpress
    | evaluateExpress
    | pseudoLambdaExpress
    | express chainSubExpress
    | express projectingAcrossCollectionExpress
    | express selectFromCollectionExpress
    | express instanceFunctionCallRightPart // 实例函数调用
    | globalFunctionCall // 全局函数调用
    | express squareExpress
    | express propertyExpress
    | prefixOperatorPart express // 前置表达式；高优先级，不能提取为子规则
    | express mathMulDivOperatorPart express // 数学乘除运算；数学运算，需要从左到右，因此不能提取为子规则
    | express mathAddSubOperatorPart express // 数学加减运算；数学运算，需要从左到右，因此不能提取为子规则
    | express compareOperatorPart express // 比较运算符
    | express bitOperatorPart  express // 位运算符号；数学运算，需要从左到右，因此不能提取为子规则
    | express logicalLinkHighOperatorPart express // 逻辑连接符
    | express logicalLinkLowOperatorPart express // 逻辑连接符
    | express thirdOperateRightPart // 三目运算符
    | listCollectionExpress
    | mapCollectionExpress
    | referenceVariableExpress
    | variableExpress
    | constExpress
    | express assignRightPart
    | express commaExpressRightPart+
;

staticFunctionCall:
    typeReference globalFunctionCall
;

globalFunctionCall:
    functionName functionArguments // 全局函数
    ;

circleExpress:
    '(' express ')'
;

newArrayExpress:
    KW_NEW fullName '[' constNumber ']' (listCollectionExpress)?
    | KW_NEW fullName '['  ']' (listCollectionExpress)
;

newInstanceExpress:
    KW_NEW fullName functionArguments
;

fullName:
    IDENTIFIER ('.' IDENTIFIER)*
    ;

chainSubExpress:
    '.' '(' express ')'
;

pseudoLambdaExpress:
    ':' '[' express ']'
;

projectingAcrossCollectionExpress:
    '.' '{' express '}'
;

selectFromCollectionExpress:
    '.' '{' selectFromCollectionModifier express '}'
;

selectFromCollectionModifier:
    '?' | '^' | '$'
;

squareExpress:
    '[' express ']'
;

staticPropertyExpress:
    typeReference IDENTIFIER
;

evaluateExpress:
    '#' IDENTIFIER '(' express? ')'
;

propertyExpress:
    '.' IDENTIFIER
;

instanceFunctionCallRightPart:
    '.'  functionName  functionArguments// 实例函数
    ;

functionName:
    IDENTIFIER
;

functionArguments:
    '(' (express commaExpressRightPart*)? ')'
;

prefixOperatorPart:
    ('!' | KW_NOT | '~' | '-' | '+')
;

mathMulDivOperatorPart:
    ('*' | '/' | '%')
;

mathAddSubOperatorPart:
    ('+' | '-' )
;

bitOperatorPart:
('<<' | KW_SHL | '>>>' | KW_USHR | '>>' | KW_SHR | '^' | KW_XOR  | '&' | KW_BAND  | '|' | KW_BOR  )
;


compareOperatorPart:
    ('>' | KW_GT | '>=' | KW_GTE | '<' | KW_LT | '<=' | KW_LTE | '==' | KW_EQ | '!='  | KW_NEQ | KW_IN | (KW_NOT KW_IN) | KW_INSTANCEOF )
;

logicalLinkHighOperatorPart:
    ('&&' | KW_AND)
;

logicalLinkLowOperatorPart:
    ('||' | KW_OR )
;

assignRightPart:
    '=' express
;

commaExpressRightPart:
    ',' express
;

thirdOperateRightPart:
    '?' express ':' express
;

listCollectionExpress:
    '{' (express commaExpressRightPart*)? '}'
;

mapCollectionExpress:
    '#' typeReference? '{' mapPairs? '}'
;

mapPairs:
    mapPair (',' mapPair)*
;

mapPair:
    (IDENTIFIER | constString) ':' express
;


typeReference:
    '@' fullName '@'
;

referenceVariableExpress:
    '#' IDENTIFIER
;

variableExpress:
    IDENTIFIER
;

constExpress:
    constNull
    | constBoolean
    | constString
    | constNumeric
;

constNull:
    KW_CONST_NULL
;

constBoolean:
    KW_CONST_BOOLEAN
;

constString:
    TERM_CONST_STRING
    | TERM_CONST_STRING_SINGLE
;


constNumeric:
    constFloat
     | constNumber
;

constNumber:
    TERM_CONST_NUMBER_HEX
    | TERM_CONST_NUMBER_OTC
    | TERM_CONST_NUMBER_BIN
    | TERM_CONST_NUMBER
;

constFloat:
    TERM_CONST_NUMBER_SCIEN_2
    |TERM_CONST_NUMBER_SCIEN_1
    |TERM_CONST_NUMBER_FLOAT
;