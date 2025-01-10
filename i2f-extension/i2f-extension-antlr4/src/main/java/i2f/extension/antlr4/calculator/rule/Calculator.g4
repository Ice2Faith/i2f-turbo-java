grammar Calculator;

@header {
    package i2f.extension.antlr4.calculator;
}

EQUAL: '=' // 等号
    ;

eval: // 入口
     number // 数字
    | expr // 表达式
    | number EQUAL // 数字，带等号后缀
    | expr EQUAL // 表达式，带等号后缀
    ;

number: // 数值
     CONST // 常量
    | DEC_NUMBER // 十进制数字
    | HEX_NUMBER // 16进制数字
    | OTC_NUMBER // 8进制数字
    | BIN_NUMBER // 二进制数字
    | HIGH_NUMBER // 自定义进制数字
    ;


expr: // 表达式
     bracket // 括号
    | convertor // 内建函数
    | expr suffixOperator // 后置单运算符
    | prefixOperator expr // 前置单运算符
    | expr operatorV5 expr // 5级双目运算符
    | expr operatorV4 expr // 4级双目运算符
    | expr operatorV3 expr // 3级双目运算符
    | expr operatorV2 expr // 2级双目运算符
    | expr operatorV1 expr // 1级双目运算符
    | expr operatorV0 expr // 0级双目运算符
    | number // 数字
    ;

convertor: // 函数
     IDENTIFIER  '(' expr (',' expr)* ')' // 函数
    ;

bracket: // 括号
     '(' expr ')' // 括号
    ;

prefixOperator:  // 前置单目运算符
     '+' | '-'
    | 'abs' // 绝对值
    | 'neg' // 负数
    | 'ln' // log(e,x)
    | 'sin' | 'cos' | 'tan' // 三角函数
    | 'arcsin' | 'arccos' | 'arctan' // 反三角函数
    | 'angle' | 'radian' // 转为角度/弧度
    | 'floor' | 'round' | 'ceil' // 取整方式，下/舍入/上
    | 'rand' // 随机整数
    | 'feibo' // 斐波那契项
    ;

suffixOperator: // 后置单目运算符
     '!' // 阶乘
    | '%' | 'per' // 百分数
    ;

operatorV5: // 5级双目运算符
     '**' | 'muls' // 累乘
    | '++' | 'adds' // 累加
;

operatorV4: // 4级双目运算符
     '&' | '|' | '~' | 'xor' | '<<' | '>>' // 与、或、非、异或、左移、右移
    | 'and' | 'or' | 'not' | 'lmov' | 'rmov'
;

operatorV3: // 3级双目运算符
     'log' | 'sqrt' // log(x,y) 、 sqrt(a,b)
    | '^' | 'pow' // x^y
;

operatorV2: // 2级双目运算符
     '*' | '/' | '%' // 乘、除、模数
    | 'mul' | 'div' | 'mod'
;

operatorV1: // 1级双目运算符
     '+' | '-' // 加、减
    ;

operatorV0: // 0级双目运算符
    | '>=' | '<=' | '!=' | '<>' | '==' // 大等于、小等于、不等于、不等于、等于
    | '>' | '<' // 大于、小于
    ;


fragment DIGIT:[0-9]; // 数字
fragment LETTER:[a-zA-Z]; // 字母

CONST: // 常量
     [pP][iI] // 常量 π
    | [eE] // 常量 e
    | 'randf' // 随机小数，取 0-1 之间的小数
    ;

DEC_NUMBER: // 十进制数字
    (DIGIT)+ // 整数
    | (DIGIT)+ '.' (DIGIT)+ // 小数
    | (DIGIT)+ '.' (DIGIT) [eE] (DIGIT)+ // 科学计数法
    | (DIGIT)+ '.' (DIGIT) [eE] '-' (DIGIT)+ // 科学计数法
    ;

HEX_NUMBER: // 16进制数
    '0' [xX] ([0-9a-fA-F])+
    ;

OTC_NUMBER: // 8进制数
    '0' [tT] ([0-7])+
    ;

BIN_NUMBER: // 2进制数
    '0' [bB] ([01])+
    ;

HIGH_NUMBER: // 任意进制数
    '0' [hH] (DIGIT)+ ':' ([0-9a-zA-Z])+ // 0h12:56ab
    ;

IDENTIFIER: // 标识符
    (LETTER|'_')+ // 字母或下划线构成
    | (LETTER|'_')+ (DIGIT|LETTER|'_')+ // 字母或数字或下划线开头，并不以数字开头
    ;

WS: // 空白符
    [ \r\n\t]+ -> channel(HIDDEN); // 隐藏空白符

UNRECOGNIZED: .; // 未识别符号