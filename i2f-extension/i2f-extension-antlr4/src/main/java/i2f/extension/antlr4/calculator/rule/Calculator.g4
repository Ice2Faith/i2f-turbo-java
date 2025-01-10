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


prefixOperator:  // 前置单目运算符
    '~' | 'not' // 位取反
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
    | '>>>' | 'srmov' // 带符号右移
    | '&' | '|'  | 'xor' | '<<' | '>>'  // 与、或、非、异或、左移、右移
    | 'and' | 'or'  | 'lmov' | 'rmov'
;

operatorV3: // 3级双目运算符
     'log'  // log(x,y) 、 sqrt(a,b)
    | '^' | 'pow' // x^y
;

operatorV2: // 2级双目运算符
    | '//' // 整除
    | '*' | '/' | '%'  // 乘、除、模数
    | 'mul' | 'div' | 'mod'
;

operatorV1: // 1级双目运算符
     '+' | '-' // 加、减
     | 'add' | 'sub'
    ;

operatorV0: // 0级双目运算符
    | '>=' | '<=' | '!=' | '<>' | '==' // 大等于、小等于、不等于、不等于、等于
    | 'gte' | 'lte' | 'neq' | 'ne' | 'eq'
    | '>' | '<' // 大于、小于
    | 'gt' | 'lt'
    ;


DIGIT:[0-9]; // 数字
NUM:[0-9]([0-9_])?;
LETTER:[a-zA-Z]; // 字母
HEX_LETTER:[0-9a-fA-F_]; // 16进制字符
OTC_LETTER:[0-7_]; // 8进制字符
BIN_LETTER:[01_]; // 2进制字符
HIGH_LETTER:[0-9a-zA-Z_]; // 自定义进制字符


constNumber: // 常量
     'pi' | 'PI' // 常量 π
    | 'e' | 'E' // 常量 e
    | 'randf' // 随机小数，取 0-1 之间的小数
    ;

TYPE_DEC_NUMBER:
    (NUM)+ ('.' (NUM)+)? ('e' ('-')? (NUM)+)?
    ;

decNumber: // 十进制数字
    TYPE_DEC_NUMBER
    | (DIGIT)+ ('.' (DIGIT)+)?
    ;

TYPE_HEX_NUMBER:
    '0X' (HEX_LETTER)+
    | '0x' (HEX_LETTER)+;

hexNumber: // 16进制数
    TYPE_HEX_NUMBER
    ;

TYPE_OTC_NUMBER:
    '0T' (HEX_LETTER)+
    | '0t' (HEX_LETTER)+
    ;

otcNumber: // 8进制数
    TYPE_OTC_NUMBER
    ;

TYPE_BIN_NUMBER:
    '0B' (HEX_LETTER)+
    | '0b' (HEX_LETTER)+
    ;

binNumber: // 2进制数
    TYPE_BIN_NUMBER
    ;

TYPE_HIGN_NUMBER: '0H' (NUM)+ ':' (HIGH_LETTER)+
    | '0h' (NUM)+ ':' (HIGH_LETTER)+
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