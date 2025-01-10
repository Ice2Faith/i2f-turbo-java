grammar Calculator;

@header {
    package i2f.extension.antlr4.calculator;
}

EQUAL: '=' // 等号
    ;

eval: // 入口
     number (EQUAL)? // 数字
    | expr (EQUAL)?// 表达式
    ;

number: // 数值
    hexNumber // 16进制数字
    | otcNumber // 8进制数字
    | binNumber // 二进制数字
    | highNumber // 自定义进制数字
    | decNumber // 十进制数字
    | constNumber // 常量
    ;


expr: // 表达式
     number // 数字
    | bracket // 括号
    | convertor // 内建函数
    | expr suffixOperator // 后置单运算符
    | prefixOperator expr // 前置单运算符
    | expr operatorV5 expr // 5级双目运算符
    | expr operatorV4 expr // 4级双目运算符
    | expr operatorV3 expr // 3级双目运算符
    | expr operatorV2 expr // 2级双目运算符
    | expr operatorV1 expr // 1级双目运算符
    | expr operatorV0 expr // 0级双目运算符

    ;

bracket: // 括号
     '(' expr ')' // 括号
    ;

convertor: // 函数
     IDENTIFIER  '(' expr (',' expr)* ')' // 函数
    ;

OPER_NOT: [nN][oO][tT];
OPER_ABS: [aA][bB][sS];
OPER_NEG: [nN][eE][gG];
OPER_LN: [lL][nN];
OPER_SIN: [sS][iI][nN];
OPER_COS: [cC][oO][sS];
OPER_TAN: [tT][aA][nN];
OPER_ARC: [aA][rR][cC];
OPER_ARC_SIN: OPER_ARC OPER_SIN;
OPER_ARC_COS: OPER_ARC OPER_COS;
OPER_ARC_TAN: OPER_ARC OPER_TAN;
OPER_ANGLE: [aA][nN][gG][lL][eE];
OPER_RADIAN: [rR][aA][dD][iI][aA][nN];
OPER_FLOOR: [fF][lL][oO][oO][rR];
OPER_ROUND: [rR][oO][uU][nN][dD];
OPER_CEIL: [cC][eE][iI][lL];
OPER_RAND: [rR][aA][nN][dD];
OPER_FEIBO: [fF][eE][iI][bB][oO];

prefixOperator:  // 前置单目运算符
    '~' | OPER_NOT // 位取反
    | OPER_ABS // 绝对值
    | OPER_NEG // 负数
    | OPER_LN // log(e,x)
    | OPER_SIN | OPER_COS | OPER_TAN // 三角函数
    | OPER_ARC_SIN | OPER_ARC_COS | OPER_ARC_TAN // 反三角函数
    | OPER_ANGLE | OPER_RADIAN // 转为角度/弧度
    | OPER_FLOOR | OPER_ROUND | OPER_CEIL // 取整方式，下/舍入/上
    | OPER_RAND // 随机整数
    | OPER_FEIBO // 斐波那契项
    ;

OPER_PER: [pP][eE][rR];

suffixOperator: // 后置单目运算符
     '!' // 阶乘
    | '%%' | OPER_PER // 百分数
    ;

OPER_MULS: [mM][uU][lL][sS];
OPER_ADDS: [aA][dD][dD][sS];

operatorV5: // 5级双目运算符
     '**' | OPER_MULS // 累乘
    | '++' | OPER_ADDS // 累加
;

OPER_MOV:[mM][oO][vV];
OPER_SRMOV: [sS][rR] OPER_MOV;
OPER_XOR:[xX][oO][rR];
OPER_AND:[aA][nN][dD];
OPER_OR:[oO][rR];
OPER_RMOV: [rR] OPER_MOV;
OPER_LMOV: [lL] OPER_MOV;

operatorV4: // 4级双目运算符
    | '>>>' | OPER_SRMOV // 带符号右移
    | '&' | '|'  | OPER_XOR | '<<' | '>>'  // 与、或、非、异或、左移、右移
    | OPER_AND | OPER_OR  | OPER_LMOV | OPER_RMOV
;

OPER_LOG:[lL][oO][gG];
OPER_POW:[pP][oO][wW];

operatorV3: // 3级双目运算符
     OPER_LOG  // log(x,y) 、 sqrt(a,b)
    | '^' | OPER_POW // x^y
;

OPER_MUL:[mM][uU][lL];
OPER_DIV:[dD][iI][vV];
OPER_MOD:[mM][oO][dD];

operatorV2: // 2级双目运算符
    | '//' // 整除
    | '*' | '/' | '%'  // 乘、除、模数
    | OPER_MUL | OPER_DIV | OPER_MOD
;

OPER_ADD:[aA][dD][dD];
OPER_SUB:[sS][uU][bB];

operatorV1: // 1级双目运算符
     '+' | '-' // 加、减
     | OPER_ADD | OPER_SUB
    ;

OPER_GTE: [gG][tT][eE];
OPER_LTE: [lL][tT][eE];
OPER_NEQ: [nN][eE][qQ];
OPER_NE: [nN][eE];
OPER_EQ: [eE][qQ];
OPER_GT: [gG][tT];
OPER_LT: [lL][tT];

operatorV0: // 0级双目运算符
    | '>=' | '<=' | '!=' | '<>' | '==' // 大等于、小等于、不等于、不等于、等于
    | OPER_GTE | OPER_LTE | OPER_NEQ | OPER_NE | OPER_EQ
    | '>' | '<' // 大于、小于
    | OPER_GT | OPER_LT
    ;



DIGIT:[0-9]; // 数字
LETTER:[a-zA-Z]; // 字母
HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
OTC_LETTER:[0-7_]; // 8进制字符
BIN_LETTER:[01_]; // 2进制字符
HIGH_LETTER:[0-9a-zA-Z_]; // 自定义进制字符

CONST_PI: [pP][iI];
CONST_RANDF: [rR][aA][nN][dD][fF];

constNumber: // 常量
     CONST_PI // 常量 π
    | 'e' | 'E' // 常量 e
    | CONST_RANDF // 随机小数，取 0-1 之间的小数
    ;

INT_NUM:
    (DIGIT)+ ('_' DIGIT+)*
;

FLOAT_NUM:
    INT_NUM '.' INT_NUM
    ;

CH_E: [eE] ('-')?;

SCIEN_NUM_1:
     INT_NUM CH_E INT_NUM
    ;

SCIEN_NUM_2:
     FLOAT_NUM CH_E INT_NUM
    ;

decNumber: // 十进制数字
     SCIEN_NUM_2
    | SCIEN_NUM_1
    | FLOAT_NUM
    | INT_NUM
    | DIGIT
    ;

CH_0X: '0' [xX];

TYPE_HEX_NUMBER:
    CH_0X (HEX_LETTER)+
    ;

hexNumber: // 16进制数
    TYPE_HEX_NUMBER
    ;

CH_0T: '0' [tT];

TYPE_OTC_NUMBER:
    CH_0T (HEX_LETTER)+
    ;

otcNumber: // 8进制数
    TYPE_OTC_NUMBER
    ;

CH_0B: '0' [bB];

TYPE_BIN_NUMBER:
    CH_0B (HEX_LETTER)+
    ;

binNumber: // 2进制数
    TYPE_BIN_NUMBER
    ;

CH_0H: '0' [hH];

TYPE_HIGN_NUMBER: CH_0H INT_NUM ':' (HIGH_LETTER)+
    ;

highNumber: // 任意进制数
    TYPE_HIGN_NUMBER
    ;

IDENTIFIER: // 标识符
     ([a-zA-Z_])+ ([0-9a-zA-Z_])* // 字母或数字或下划线开头，并不以数字开头
    ;

WS: // 空白符
    [ \r\n\t]+ -> channel(HIDDEN); // 隐藏空白符

UNRECOGNIZED: .; // 未识别符号