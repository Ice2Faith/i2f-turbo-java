grammar Funic;

@header {
    package i2f.extension.antlr4.script.funic.grammar;
}

// Lexer rules
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

// 注释
TERM_COMMENT_SINGLE_LINE: '//' (~('\n'))* -> skip;
TERM_COMMENT_MULTI_LINE: '/*' .*? '*/' -> skip;

// 多行文本
TERM_CONST_STRING_MULTILINE: '```' NAMING? [ \t]* '\n' (~('`') | '\\' ('`'|'``'|'```'|'\\'))* '\n' [ \t]* '```';
TERM_CONST_STRING_MULTILINE_QUOTE: '"""' NAMING? [ \t]* '\n' (~('"') | '\\' ('"'|'""'|'"""'|'\\'))* '\n' [ \t]* '"""';

// 渲染文本
fragment ESCAPED_CHAR: '\\' .;
TERM_CONST_STRING_RENDER: R '"' (ESCAPED_CHAR | ~[\\"])* '"';
TERM_CONST_STRING_RENDER_SINGLE: R '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

// 普通文本
TERM_CONST_STRING: '"' (ESCAPED_CHAR | ~[\\"])* '"';
TERM_CONST_STRING_SINGLE: '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

// 数值
fragment CH_E: E ('-'|'+')?;

fragment CH_0X: '0' X;
fragment CH_0O: '0' O;
fragment CH_0B: '0' B;


fragment TERM_DIGIT:[0-9]; // 数字
fragment TERM_HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
fragment TERM_OTC_LETTER:[0-7_]; // 8进制字符
fragment TERM_BIN_LETTER:[01_]; // 2进制字符

fragment TERM_INTEGER:
    (TERM_DIGIT)+ ('_' TERM_DIGIT+)*
    ;

fragment ID       : [a-zA-Z_$][a-zA-Z0-9_$]* ;
fragment NAMING: ID ('.' ID)*;

TERM_CONST_NUMBER_HEX:
    CH_0X (TERM_HEX_LETTER)+ L?
    ;


TERM_CONST_NUMBER_OTC:
    CH_0O (TERM_OTC_LETTER)+ L?
    ;


TERM_CONST_NUMBER_BIN:
    CH_0B (TERM_BIN_LETTER)+ L?
    ;

TERM_CONST_NUMBER_SCIEN_2:
     TERM_CONST_NUMBER_FLOAT CH_E TERM_INTEGER F?
    ;

TERM_CONST_NUMBER_SCIEN_1:
     TERM_INTEGER CH_E TERM_INTEGER F?
    ;

TERM_CONST_NUMBER_FLOAT:
    TERM_INTEGER '.' TERM_INTEGER F?
    ;

TERM_CONST_NUMBER:
   TERM_INTEGER  L?
;

// 布尔
KW_CONST_BOOLEAN:
    'true' | 'false'
    ;

// 空值
KW_CONST_NULL:
    'null'
    ;

// 类
KW_CONST_CLASS:
    'class'
    ;

KW_FUNC: 'func';
KW_DEF: 'def';

KW_TRY: 'try';
KW_CATCH: 'catch';
KW_FINALLY: 'finally';

KW_THROW: 'throw';

KW_RETURN: 'return';

KW_CONTINUE: 'continue';
KW_BREAK: 'break';

KW_FOR: 'for';
KW_DO: 'do';
KW_WHILE: 'while';

KW_IF: 'if';
KW_ELIF: 'elif';
KW_ELSE: 'else';

KW_AS: 'as';

KW_NEW: 'new';

KW_NOT: 'not';

KW_TEQ: 'teq';
KW_TNEQ: 'tneq';
KW_GTE: 'gte';
KW_LTE: 'lte';
KW_GT: 'gt';
KW_LT: 'lt';
KW_NEQ: 'neq';
KW_EQ: 'eq';
KW_IN: 'in';
KW_INSTANCEOF: 'instanceof';
KW_IS: 'is';

KW_AND: 'and';
KW_OR: 'or';

KW_GO: 'go';
KW_SYNCHRONIZED: 'synchronized';

KW_IMPORT: 'import';

IDENTIFIER: ID;

WS       : [ \t\r\n]+ -> skip ;

root:
    script EOF
;

script:
    express (';' express)* (';')?
;

express:
    circleExpress // 括号表达式
    | express instanceFunctionCallRightPart // 实例函数调用
    | newInstanceExpress // 新建实例对象
    | newArrayExpress // 新建数组
    | ifElseExpress // if-else 语句
    | whileExpress // while 语句
    | doWhileExpress // do-while 语句
    | foreachExpress // 集合迭代语句
    | forLoopExpress // for 循环语句
    | forRangeExpress // for-range 语句
    | breakExpress // break 语句
    | continueExpress // continue 语句
    | returnExpress // return 语句
    | throwExpress // throw 语句
    | importExpress // 导入语句
    | tryCatchFinallyExpress // try-catch-finally 语句
    | functionDeclareExpress // 函数声明语句
    | lambdaExpress // Lambda语句
    | goRunExpress // 启动线程语句
    | awaitExpress // 等待异步执行的结果返回
    | synchronizedExpress // 同步语句
    | staticFunctionCall // 静态函数调用
    | globalFunctionCall // 全局函数调用
    | express instanceFieldValueRightPart // 实例值获取
    | staticFieldValue // 静态值获取
    | express squareQuoteRightPart // 中括号取值表达式
    | express factorPercentRightPart // 阶乘/百分号后置表达式
    | prefixOperatorPart express // 前置表达式；高优先级，不能提取为子规则
    | incrDecrPrefixOperatorPart express // 前置自增表达式；高优先级，不能提取为子规则
    | express incrDecrAfterRightPart // 后置自增表达式
    | express mathMulDivOperatorPart express // 数学乘除运算；数学运算，需要从左到右，因此不能提取为子规则
    | express mathAddSubOperatorPart express // 数学加减运算；数学运算，需要从左到右，因此不能提取为子规则
    | express castAsRightPart // 类型转换
    | express compareOperatorPart express // 比较运算符
    | express logicalLinkOperatorPart express // 逻辑连接符
    | express bitOperatorPart  express // 位运算符号；数学运算，需要从左到右，因此不能提取为子规则
    | listValueExpress // 列表表达式
    | mapValueExpress // 映射表达式
    | express thirdOperateRightPart // 三目运算符
    | express pipelineFunctionExpress+ // 管道函数
    | scriptBlock // 语句块
    | KW_DEF express assignRightPart // 等号赋值表达式
    | express assignRightPart // 等号赋值表达式
    | valueSegment // 字面值常量
;

logicalLinkOperatorPart:
    ('&&' | KW_AND | '||' | KW_OR )
;


compareOperatorPart:
    ('===' | KW_TEQ | '!==' | KW_TNEQ | '>' | KW_GT | '>=' | KW_GTE | '<' | KW_LT | '<=' | KW_LTE | '==' | KW_EQ | '!=' | '<>' | KW_NEQ | KW_IN | (KW_NOT KW_IN) | KW_INSTANCEOF | KW_IS )
;

bitOperatorPart:
('<<' | '>>>' | '>>' | '^'  | '&'  | '|'  )
;

mathAddSubOperatorPart:
('+' | '-' )
;

mathMulDivOperatorPart:
    ('*' | '//' | '/' | '%')
;

incrDecrPrefixOperatorPart:
    ('++' | '--')
;

prefixOperatorPart:
    ('!' | KW_NOT | '~' | '-')
;

// :: 用来访问表示上一个管道传递过来的成员函数
// 参数，支持使用 $_ 作为占位符表示传递过来的参数作为第几个值(暂未实现)，默认第一个值
pipelineFunctionExpress:
   '|>' (staticFunctionCall | IDENTIFIER | '::'? (globalFunctionCall|IDENTIFIER)) // 函数链
;

synchronizedExpress:
   KW_SYNCHRONIZED  '(' express ')' scriptBlock
;

// 直接捕获声明时的参数列表
lambdaExpress:
    functionArguments '->' scriptBlock
;

importExpress:
    KW_IMPORT fullName ('.*')?
;

// 主要是用来异步运行lambda或者函数
goRunExpress:
    KW_GO lambdaExpress
    | KW_GO scriptBlock
    | KW_GO express
;

awaitExpress:
   ('<-' express)+
;


functionDeclareExpress:
    (KW_FUNC|KW_DEF) IDENTIFIER functionDeclareParameters functionDeclareReturn? scriptBlock
;

functionDeclareReturn:
    ':' fullName
;

functionDeclareParameters:
    '(' ( functionParameter (',' functionParameter)*)? ')'
;

functionParameter:
    fullName IDENTIFIER
    | IDENTIFIER ':' fullName
    |IDENTIFIER
;

tryCatchFinallyExpress:
    KW_TRY scriptBlock catchBlock* (KW_FINALLY scriptBlock)?
;

catchBlock:
    KW_CATCH '(' (fullName ('|' fullName)*)? IDENTIFIER ')' scriptBlock
    ;

throwExpress:
    KW_THROW express
;

returnExpress:
    KW_RETURN express?
;

continueExpress:
    KW_CONTINUE
;

breakExpress:
    KW_BREAK
;

// for(i 0...20) 这种循环
forRangeExpress:
    KW_FOR '(' IDENTIFIER constNumber '...' constNumber ')' scriptBlock
;

// for(i=0;i<10;i++) 这种循环
forLoopExpress:
    KW_FOR '(' express? ';' express? ';' express? ')' scriptBlock
;

// for(i : list) 这种循环
foreachExpress:
    KW_FOR '(' IDENTIFIER ':' express ')' scriptBlock
;

doWhileExpress:
    KW_DO scriptBlock KW_WHILE conditionBlock
;

whileExpress:
    KW_WHILE conditionBlock scriptBlock
    ;

ifElseExpress:
    KW_IF conditionBlock scriptBlock ((KW_ELSE KW_IF|KW_ELIF) conditionBlock scriptBlock)* (KW_ELSE scriptBlock)?
;

conditionBlock:
'(' express ')'
;

scriptBlock:
    '{' script? '}'
;

// a as int.class 进行类型转换
castAsRightPart:
    KW_AS typeClass
;

// {a:1,...user,b}
mapValueExpress:
    '{' (unpackMapExpress (',' unpackMapExpress)* )? ','? '}'
;

unpackMapExpress:
    '...' express
    | keyValueExpress
;

keyValueExpress:
    (IDENTIFIER | constString | constRenderString) ':' express
    | variableValue
;



thirdOperateRightPart:
    '?' express ':' express
;

instanceFieldValueRightPart:
    ('.'|'.?') IDENTIFIER // 实例属性
;

circleExpress:
    '(' express ')'
;

newArrayExpress:
    KW_NEW fullName '[' constNumber ']'
;

newInstanceExpress:
    KW_NEW fullName functionArguments
;

instanceFunctionCallRightPart:
    '.'  IDENTIFIER  functionArguments// 实例函数
    ;

globalFunctionCall:
    IDENTIFIER functionArguments // 全局函数
    ;

// [0],["name"] 这种下标或者属性名访问
squareQuoteRightPart:
    '[' express ']'
;

factorPercentRightPart:
    ('!' | '%')
;



incrDecrAfterRightPart:
    ('++' | '--')
;

assignRightPart:
    ('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '?=' | '.=') express
;

staticFieldValue:
    typeMember IDENTIFIER // 静态属性
;

staticFunctionCall:
    typeMember IDENTIFIER functionArguments // 静态函数
;

functionArguments:
    '(' (functionArgument (',' functionArgument)*)? ')'
;

functionArgument:
    IDENTIFIER ':' express
    | express
;

listValueExpress:
    '[' (unpackListExpress (',' unpackListExpress)*)? ','? ']'
;

unpackListExpress:
    '...'? express
;

fullName:
    IDENTIFIER ('.' IDENTIFIER)*
    ;

typeClass:
    fullName '.' KW_CONST_CLASS
    ;

typeReference:
    '@' fullName
    ;

typeMember:
    typeClass '.'
    | typeReference '.'
    | fullName '::'
;

valueSegment:
    constValue
    | variableValue
    | typeClass
;

variableValue:
    IDENTIFIER
;

constValue:
    constMultiString
    | constCharSequence
    | constNumeric
    | constBoolean
    | constNull
;

constCharSequence:
    constRenderString
    | constString
;

constString:
    TERM_CONST_STRING
    |TERM_CONST_STRING_SINGLE
    ;

constRenderString:
    TERM_CONST_STRING_RENDER
    |TERM_CONST_STRING_RENDER_SINGLE
    ;

constMultiString:
TERM_CONST_STRING_MULTILINE
    |TERM_CONST_STRING_MULTILINE_QUOTE
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

constBoolean:
    KW_CONST_BOOLEAN
;

constNull:
    KW_CONST_NULL
;