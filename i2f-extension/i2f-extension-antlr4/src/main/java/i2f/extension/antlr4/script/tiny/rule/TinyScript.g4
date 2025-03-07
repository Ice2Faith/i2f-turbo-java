grammar TinyScript;

// user.status=new BigDecimal(${role.perm}.lrtim(Func.rtrim(replace(${user.name}.isEmpty("user",true).length().size(),"s+",12.125f,0x56l,0t27,0b101L,true),";"),",")).intValue()
// 定义一份迷你脚本
// 核心是函数调用
// 可支持多行语句，语句使用;分号分隔
// 每条语句，可以是常量字面值
// 1;1L;"str";true;12.125;0x6ab;0t127;0b1011
// 可以是取引用值
// ${user};${user.name}
// 可以是函数调用,注意，一旦开始了函数调用，之后允许继续函数调用
// replace();String.valueof();org.apache.StringUtils.trimAll();${user}.getName();${user}.getName().length()
// 可以是简单的比较运算语句
// 1+1;1+${count};1>${count};
// 可以是new语句
// new User();new String();new org.apache.User();
// 可以是赋值语句
// count=1;count=${role.count};count=${user}.getRoleList().size()

@header {
    package i2f.extension.antlr4.script.tiny;
}

// Lexer rules
SINGLE_LINE_COMMENT: '//' (~('\n'))* '\n' -> skip;

MULTI_LINE_COMMENT: '/*' (~('*') | '\\' ('*') )* '*/' -> skip;

MULTILINE_STRING: '```' NAMING? [ \t]* '\n' (~('`') | '\\' ('`'|'``'|'```'|'\\'))* '\n' [ \t]* '```';
MULTILINE_QUOTE_STRING: '"""' NAMING? [ \t]* '\n' (~('"') | '\\' ('"'|'""'|'"""'|'\\'))* '\n' [ \t]* '"""';

RENDER_STRING: [rR] QUOTE (ESCAPED_CHAR | ~[\\"])* QUOTE;
RENDER_SINGLE_STRING: [rR] '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

STRING: QUOTE (ESCAPED_CHAR | ~[\\"])* QUOTE;
SINGLE_STRING: '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

TYPE_BOOL:
    'true' | 'false'
    ;

TYPE_NULL:
    'null'
    ;


// 词法规则
QUOTE: '"';
ESCAPED_CHAR: '\\' ( '\\' | '"' | '\'' | 'r' | 't' | 'n' );

TYPE_CLASS:
    NAMING '.' 'class';

NAMING: ID (DOT ID)*;
ID       : [a-zA-Z_][a-zA-Z0-9_]* ;


PREFIX_OPERATOR:
    '!' | 'not'
    ;

LPAREN   : '(' ;
RPAREN   : ')' ;
COMMA    : ',' ;
DOT      : '.' ;
DOLLAR   : '$' ;
LCURLY   : '{' ;
RCURLY   : '}' ;
WS       : [ \t\r\n]+ -> skip ;

REF_EXPRESS : DOLLAR LCURLY (~[}])* RCURLY ;

DIGIT:[0-9]; // 数字
HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
OTC_LETTER:[0-7_]; // 8进制字符
BIN_LETTER:[01_]; // 2进制字符



INT_NUM:
    (DIGIT)+ ('_' DIGIT+)* [lL]?
;

FLOAT_NUM:
    INT_NUM '.' INT_NUM [fF]?
    ;

CH_E: [eE] ('-')?;

SCIEN_NUM_1:
     INT_NUM CH_E INT_NUM [lL]?
    ;

SCIEN_NUM_2:
     FLOAT_NUM CH_E INT_NUM [fF]?
    ;


CH_0X: '0' [xX];

TYPE_HEX_NUMBER:
    CH_0X (HEX_LETTER)+ [lL]?
    ;


CH_0T: '0' [tT];

TYPE_OTC_NUMBER:
    CH_0T (HEX_LETTER)+ [lL]?
    ;



CH_0B: '0' [bB];

TYPE_BIN_NUMBER:
    CH_0B (HEX_LETTER)+ [lL]?
    ;


script:
     express (';' (express)?)*
    ;


express:
    debuggerSegment
    | ifSegment
    | foreachSegment
    | forSegment
    | whileSegment
    | controlSegment
    | trySegment
    | parenSegment
    | prefixOperatorSegment
    | equalValue
    | newInstance
    | invokeFunction
    | constValue
    | refValue
    | jsonValue
    | express ('*' | '/' | '%') express
    | express ( 'in' | 'notin'| 'as' | 'cast'  | 'is' | 'instanceof' | 'typeof') express
    | express ('+' | '-') express
    | express ('>=' | 'gte' | '<=' | 'lte' | '!=' | 'ne' | '<>' | 'neq' | '==' | 'eq' | '>' | 'gt' | '<' | 'lt') express
    | express ('&&' | 'and' | '||' | 'or') express
    ;

debuggerSegment:
    'debugger' (namingBlock)? ('(' conditionBlock ')')?
    ;

trySegment:
    'try' tryBodyBlock ('catch' '(' (classNameBlock ('|' classNameBlock)*) namingBlock ')' catchBodyBlock)* ('finally' finallyBodyBlock)?
    ;

tryBodyBlock:
    scriptBlock
    ;

catchBodyBlock:
    scriptBlock
    ;

finallyBodyBlock:
    scriptBlock
    ;

classNameBlock:
    NAMING
    ;

parenSegment:
    LPAREN express RPAREN
    ;

prefixOperatorSegment:
    PREFIX_OPERATOR express
    ;

controlSegment:
    'break'
    | 'continue'
    | 'return' (express)?
    ;

whileSegment:
    'while' '(' conditionBlock ')' scriptBlock
    ;

forSegment:
    'for' '(' express ';' conditionBlock ';' express ')' scriptBlock
    ;

foreachSegment:
    'foreach' '(' namingBlock ':' express ')' scriptBlock
    ;

namingBlock:
    NAMING
    ;

ifSegment:
    'if' '(' conditionBlock ')' scriptBlock (  'else' 'if' '(' conditionBlock ')' scriptBlock )* ('else' scriptBlock)?
    ;

conditionBlock:
    express
    ;

scriptBlock:
    '{' script '}'
    ;


equalValue:
    NAMING '=' express
    ;

newInstance:
    'new' invokeFunction
    ;

invokeFunction:
    refCall (DOT functionCall)*
    | functionCall (DOT functionCall)*
    ;

functionCall:
    NAMING LPAREN argumentList? RPAREN
    ;

refCall:
    refValue DOT functionCall
    ;

argumentList
    : argument ( COMMA argument )* ;

argument:
    (NAMING ':')? argumentValue
    ;

argumentValue
    : express
    ;

constValue:
    constBool
    | constClass
    | constNull
    | constMultilineString
    | constRenderString
    | constString
    | decNumber
    | hexNumber
    | otcNumber
    | binNumber
    ;

refValue
    : REF_EXPRESS ;

constBool:
    TYPE_BOOL
    ;

constNull:
    TYPE_NULL
    ;

constClass:
    TYPE_CLASS
    ;

constString:
    STRING
    |SINGLE_STRING
    ;

constMultilineString:
    MULTILINE_STRING
    |MULTILINE_QUOTE_STRING
    ;

constRenderString:
    RENDER_STRING
    |RENDER_SINGLE_STRING
    ;

decNumber: // 十进制数字
     SCIEN_NUM_2
    | SCIEN_NUM_1
    | FLOAT_NUM
    | INT_NUM
    | DIGIT
    ;

hexNumber: // 16进制数
    TYPE_HEX_NUMBER
    ;

otcNumber: // 8进制数
    TYPE_OTC_NUMBER
    ;

binNumber: // 2进制数
    TYPE_BIN_NUMBER
    ;

jsonValue:
    invokeFunction
    | constValue
    | refValue
    | jsonArrayValue
    | jsonMapValue
    ;

jsonMapValue:
    LCURLY jsonPairs? RCURLY;

jsonPairs:
    jsonPair
    | jsonPair (',' jsonPair)*
    ;

jsonPair:
    (NAMING|constString) ':' express
    ;

jsonArrayValue:
    '[' jsonItemList? ']'
    ;

jsonItemList:
    express
    | express (',' express)*
    ;