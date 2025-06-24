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
TERM_COMMENT_SINGLE_LINE: '//' (~('\n'))* -> skip;

TERM_COMMENT_MULTI_LINE: '/*' .*? '*/' -> skip;

TERM_CONST_STRING_MULTILINE: '```' NAMING? [ \t]* '\n' (~('`') | '\\' ('`'|'``'|'```'|'\\'))* '\n' [ \t]* '```';
TERM_CONST_STRING_MULTILINE_QUOTE: '"""' NAMING? [ \t]* '\n' (~('"') | '\\' ('"'|'""'|'"""'|'\\'))* '\n' [ \t]* '"""';

TERM_CONST_STRING_RENDER: [rR] TERM_QUOTE (ESCAPED_CHAR | ~[\\"])* TERM_QUOTE;
TERM_CONST_STRING_RENDER_SINGLE: [rR] '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

TERM_CONST_STRING: TERM_QUOTE (ESCAPED_CHAR | ~[\\"])* TERM_QUOTE;
TERM_CONST_STRING_SINGLE: '\'' (ESCAPED_CHAR | ~[\\'])* '\'';

TERM_CONST_BOOLEAN:
    'true' | 'false'
    ;

TERM_CONST_NULL:
    'null'
    ;

TERM_CONST_TYPE_CLASS:
    NAMING '.' 'class';


REF_EXPRESS : TERM_DOLLAR ('!')? TERM_CURLY_L (~[}])* TERM_CURLY_R ;


TERM_CONST_NUMBER_SCIEN_2:
     TERM_CONST_NUMBER_FLOAT CH_E TERM_INTEGER [fF]?
    ;

TERM_CONST_NUMBER_SCIEN_1:
     TERM_INTEGER CH_E TERM_INTEGER [lL]?
    ;

TERM_CONST_NUMBER_FLOAT:
    TERM_INTEGER TERM_DOT TERM_INTEGER [fF]?
    ;

TERM_CONST_NUMBER:
   TERM_INTEGER  [lL]?
;


TERM_CONST_NUMBER_HEX:
    CH_0X (TERM_HEX_LETTER)+ [lL]?
    ;


TERM_CONST_NUMBER_OTC:
    CH_0T (TERM_OTC_LETTER)+ [lL]?
    ;


TERM_CONST_NUMBER_BIN:
    CH_0B (TERM_BIN_LETTER)+ [lL]?
    ;

TERM_INTEGER:
    (TERM_DIGIT)+ ('_' TERM_DIGIT+)*
    ;


NAMING: ID (TERM_DOT ID)*;
ROUTE_NAMING: ID (TERM_BRACKET_SQUARE_L TERM_INTEGER TERM_BRACKET_SQUARE_R)? (TERM_DOT ID (TERM_BRACKET_SQUARE_L TERM_INTEGER TERM_BRACKET_SQUARE_R)? )*;
ID       : [a-zA-Z_][a-zA-Z0-9_]* ;


// 词法规则
TERM_QUOTE: '"';
ESCAPED_CHAR: '\\' ( '\\' | '"' | '\'' | 'r' | 't' | 'n' );

TERM_PAREN_L   : '(' ;
TERM_PAREN_R   : ')' ;
TERM_COMMA    : ',' ;
TERM_DOT      : '.' ;
TERM_DOLLAR   : '$' ;
TERM_CURLY_L   : '{' ;
TERM_CURLY_R   : '}' ;

TERM_SEMICOLON : ';';
TERM_COLON : ':';

TERM_BRACKET_SQUARE_L : '[';
TERM_BRACKET_SQUARE_R : ']';

CH_E: [eE] ('-')?;

CH_0X: '0' [xX];
CH_0T: '0' [tT];
CH_0B: '0' [bB];

TERM_DIGIT:[0-9]; // 数字
TERM_HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
TERM_OTC_LETTER:[0-7_]; // 8进制字符
TERM_BIN_LETTER:[01_]; // 2进制字符

WS       : [ \t\r\n]+ -> skip ;


script:
    segments?  EOF
    ;

segments:
     express (TERM_SEMICOLON express)* (TERM_SEMICOLON)?
    ;


express:
    debuggerSegment
    | ifSegment
    | foreachSegment
    | forSegment
    | whileSegment
    | controlSegment
    | trySegment
    | throwSegment
    | parenSegment
    | ('!' | 'not') express
    | equalValue
    | newInstance
    | invokeFunction
    | staticEnumValue
    | constValue
    | refValue
    | jsonValue
    | express ('%')
    | express ( 'as' | 'cast'  | 'is' | 'instanceof' | 'typeof') express
    | express ('*' | '/' | '%') express
    | express ('+' | '-') express
    | express ('in' | 'notin' | '>=' | 'gte' | '<=' | 'lte' | '!=' | 'ne' | '<>' | 'neq' | '==' | 'eq' | '>' | 'gt' | '<' | 'lt') express
    | express ('&&' | 'and' | '||' | 'or') express
    | negtiveSegment
    | express '?' express ':' express
    ;

negtiveSegment:
    '-' express
    ;

debuggerSegment:
    'debugger' (namingBlock)? (TERM_PAREN_L conditionBlock TERM_PAREN_R)?
    ;

trySegment:
    'try' tryBodyBlock ('catch' TERM_PAREN_L (classNameBlock ('|' classNameBlock)*) namingBlock TERM_PAREN_R catchBodyBlock)* ('finally' finallyBodyBlock)?
    ;

throwSegment:
    'throw' express
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
    TERM_PAREN_L express TERM_PAREN_R
    ;


controlSegment:
    'break'
    | 'continue'
    | 'return' (express)?
    ;

whileSegment:
    'while' TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock
    ;

forSegment:
    'for' TERM_PAREN_L express TERM_SEMICOLON conditionBlock TERM_SEMICOLON express TERM_PAREN_R scriptBlock
    ;

foreachSegment:
    'foreach' TERM_PAREN_L namingBlock TERM_COLON express TERM_PAREN_R scriptBlock
    ;

namingBlock:
    NAMING
    ;

ifSegment:
    'if' TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock (  ('else' 'if' | 'elif') TERM_PAREN_L conditionBlock TERM_PAREN_R scriptBlock )* ('else' scriptBlock)?
    ;

conditionBlock:
    express
    ;

scriptBlock:
    TERM_CURLY_L (segments)? TERM_CURLY_R
    ;


equalValue:
    (ROUTE_NAMING|NAMING|extractExpress|staticEnumValue) ('='|'?='|'.='|'+='|'-='|'*='|'/='|'%=') express
    ;

extractExpress:
    '#' TERM_CURLY_L extractPairs? TERM_CURLY_R
    ;

extractPairs:
    extractPair (TERM_COMMA extractPair)*
    ;

extractPair:
    (NAMING|ROUTE_NAMING|constString) (TERM_COLON (NAMING|ROUTE_NAMING|constString))?
    ;

staticEnumValue:
     '@' NAMING
    | NAMING '@' NAMING
    ;

newInstance:
    'new' invokeFunction
    ;

invokeFunction:
    refCall (TERM_DOT functionCall)*
    | functionCall (TERM_DOT functionCall)*
    ;

functionCall:
    NAMING TERM_PAREN_L argumentList? TERM_PAREN_R
    ;

refCall:
    refValue TERM_DOT functionCall
    ;

argumentList:
    argument ( TERM_COMMA argument )*
    ;

argument:
    ((NAMING|constString) TERM_COLON)? argumentValue
    ;

argumentValue:
    express
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

refValue:
    REF_EXPRESS
    ;

constBool:
    TERM_CONST_BOOLEAN
    ;

constNull:
    TERM_CONST_NULL
    ;

constClass:
    TERM_CONST_TYPE_CLASS
    ;

constString:
    TERM_CONST_STRING
    |TERM_CONST_STRING_SINGLE
    ;

constMultilineString:
    TERM_CONST_STRING_MULTILINE
    |TERM_CONST_STRING_MULTILINE_QUOTE
    ;

constRenderString:
    TERM_CONST_STRING_RENDER
    |TERM_CONST_STRING_RENDER_SINGLE
    ;

decNumber: // 十进制数字
     TERM_CONST_NUMBER_SCIEN_2
    | TERM_CONST_NUMBER_SCIEN_1
    | TERM_CONST_NUMBER_FLOAT
    | TERM_CONST_NUMBER
    | TERM_DIGIT
    ;

hexNumber: // 16进制数
    TERM_CONST_NUMBER_HEX
    ;

otcNumber: // 8进制数
    TERM_CONST_NUMBER_OTC
    ;

binNumber: // 2进制数
    TERM_CONST_NUMBER_BIN
    ;

jsonValue:
    invokeFunction
    | constValue
    | refValue
    | jsonArrayValue
    | jsonMapValue
    ;

jsonMapValue:
    TERM_CURLY_L jsonPairs? TERM_CURLY_R
    ;

jsonPairs:
    jsonPair (TERM_COMMA jsonPair)*
    ;

jsonPair:
    (NAMING|constString) TERM_COLON express
    ;

jsonArrayValue:
    TERM_BRACKET_SQUARE_L jsonItemList? TERM_BRACKET_SQUARE_R
    ;

jsonItemList:
    express (TERM_COMMA express)*
    ;