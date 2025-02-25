# TinyScript 表达式引擎

- 主要用于提供嵌套函数调用的功能
- 另外支持多条语句执行
- 调用返回最后一条语句执行的结果
- 基于一个根上下文进行操作

## 核心设计

- 核心使用antlr4生成与解析语法规则
- 暴露TinyScript.script()方法
- 方法声明如下

```shell
public static Object script(String formula, Object context);
```

- 函数说明
- 对formula表达式进行执行，表达式允许对context进行读取
- 最终返回最后一条formula语句执行的结果
- 同时，需要记住，formula是允许对context进行读写的
- 常见的context可能是一个Map对象或者一个普通对象

## 表达式规则

### 注释

- 注释和Java等大多数语言一样

```shell
// 单行注释
/* 多行注释 */
```

- 多行注释内部的*号需要使用\进行转义
- 这是一个特殊的地方

### 基础数据类型

- 包含，int,long，float,double,null,boolean,string基础类型
- int书写

```shell
1;
123;
0xabc;
0t754;
0b10110;
```

- 默认情况下，不带小数点的，都是int类型
- 其中0x表示16进制，0t表示8进制，0b表示2进制
- long书写

```shell
1L;
123L;
0xabcL;
0t754L;
0b10110L;
```

- long的规则和int类型基本一致，只不过在最后添加字符L，大小写均可
- double书写

```shell
1.1;
1.1e6;
```

- 默认情况下，带小数点的都是double类型
- 没有0x/0t/0b这种写法
- 支持科学计数法
- float书写

```shell
1.1F;
1.1e6F;
```

- float的规则和double类型基本一致，只不过在最后添加字符F，大小写均可
- null书写规则

```shell
null;
```

- 固定关键字，区分大小写
- boolean书写规则

```shell
true;
false;
```

- 固定两个关键字，区分大小写
- string书写规则

```shell
"aa\r\n\t \"\\bb\'"
```

- 书写规则和各种语言中的基本一致，支持使用转义符号转义
- 模板字符串

```shell
R"abc ${count} def $${size} gh"
```

- 书写规则和字符串一样，但是前置的双引号["]之前使用字符R引导，引导字符不区分大小写
- 其中的变量捕获，和取值语句是一样的
- 如果有${符号需要保留，可以提前将这两字符拿出来，作为一个捕获变量
- 多行字符串

```shell
\```trim.align.render
  | 123
  |     456
  |  789 ${count}
\```
```

- 用法使用三个连续的反引号[`]包含
- 如果内部有反引号[`]，则需要使用[\\]进行转义
- 所表示的内容不包含开始引导符与结束引导符所在的行
- 也就是说，首行和尾行不算在内的
- 并且首行和尾行只允许有空白符，不能出现其他字符
- 当然，首行允许带有特性
- 开头的引导符号之后可以紧跟着几个特性
    - trim 进行trim
    - align 表示只保留每行的第一个[|]符号之后的内容，可用于保持对其，缺点就是每行都需要这个引导符号
    - render 表示要进行模板字符串渲染
    - 特性之间通过[.]符号分割连接，特性从左至右依次执行
    - 也就是说，允许重复使用特性
    - 例如：trim.render.trim

### 复杂数据类型

- 支持JSON数据类型
- 使用上和JSON的规则差不多
- 但是融合了脚本语言的特性
- 实际上，内部将会转换为LinkedHashMap和ArrayList对象
- 直接看例子

```shell
[
  {
    name: "xxx",
    "age": 12,
    roles: ["admin","logger",3,true],
    image: ${images.defaultImage},
    status: decode(${user.status},1,"正常",0,"禁用"),
    platform.prefer: "windows"
  }
]
```

- 从这个例子中，便可以看到
- 大体和JSON类似
- 但是具有一些JS构建对象的便利性
- 可以使用函数调用decode
- 可以不用引号包含键名name/roles
- 可以使用${}从环境中取值

## 语法介绍

- 语句分隔
- 表达式允许有多条语句，语句之间使用分号[;]分隔
- 例如

```shell
1;
2.15;
true;
"hello"
```

- 取值语句
- 表达式使用${}来访问context中的值
- 定义

```shell
${要访问的变量路径}
```

- 例如：

```shell
${user.name}
${user.roles[0].name}
```

- 赋值语句
- 修改context里面的值，通过赋值语句修改
- 定义

```shell
修改或保存的变量路径 = 变量值
```

- 举例

```shell
name = "zhang";
age = 12;
role = {
  name: "root",
  id: 123L
};
status: ${config.user.defaultStatus};
user.roles[1].name="logger";
```

- 括号表达式
- 定义

```shell
(...)
```

- 举例

```shell
1+(1+1);
1+(2-${cnt});
```

- 前置操作符表达式
- 定义

```shell
操作符 表达式
```

- 操作符说明

```shell
! 取反
not 取反
```

- 举例

```shell
!${flag};
not ${flag};
```

- 创建对象
- 定义

```shell
new 类名(参数列表)
```

- 一些常用的类名可以不写完整的全限定类名
- 例如java.lang/java.util/java.time等包下面的
- 可以直接写简短类名，例如Date
- 举例

```shell
new String("xxx");
new Date();
new org.apache.User(1L,"xxx",${status},decode(${str},"是",1,0));
```

- 函数调用
- 函数名需要携带类名，也就是全限定函数名
- 一些常用的类名可以不写完整的类名，同创建对象时的类名
- 不过，有一些情况下，用户可以定义自己的内建函数
- 这种函数只需要写函数名即可
- 内建函数需要用户自己注册到TinyScript.BUILTIN_METHOD中
- 这是一个静态变量，向其中添加即可
- 通过调用TinyScript.registryBuiltinMethod(Method)方法实现
- 默认情况下，具有一些内建函数，在String类的join/format方法，Integer/Long/Float/Double/Boolean类的parse/to开头的方法
- 定义

```shell
函数名(参数列表)
```

- 举例

```shell
String.valueOf(1);
org.apache.Test.run("xxx",true);
```

- 函数支持链式调用
- 和java链式调用一致
- 举例

```shell
String.valueOf(1).repeat(2).length();
```

- 可以在值上面调用函数

```shell
${user}.getName().length();
```

- 双目运算符
- 也就是需要两个运算参数左值和右值，中间是操作符的表达式
- 常见的例如加减乘除
- 定义

```shell
表达式 运算符 表达式
```

- 运算符含义说明

```shell
>= 大于等于
gte 大于等于
<= 小于等于
lte 小于等于
!= 不等于
ne 不等于
<> 不等于
neq 不等于
== 相等
eq 相等
> 大于
gt 大于 
< 小于
lt 小于
in 元素在其中
notin 元素不在其中
&& 逻辑与
and 逻辑与
|| 逻辑或
or 逻辑或
+ 加
- 减
* 乘
/ 除
% 取模，结果一定是int
```

- 举例

```shell
1>2;
"xxx"+1;
```

- if-else条件语句
- 定义

```shell
if(条件表达式){
  语句块
} else if(条件表达式){
  语句块
} else {
  语句块
};
```

- 用法和Java中类似
- 条件语句块比较特殊，不一样需要时boolean值
- 内部会自动转换为boolean值
- 比如，null空值,""空字符串,{}空Map,[]空Collection都认为是false
- 需要注意的是，if语句也算是一条语句，因此最后需要添加分号[;]结尾
- 举例

```shell
if(${num}>0){
    ok=1;
}else if(${role}){
    ok=2;
}else{
    ok=3;
};
```

- foreach循环语句
- 定义

```shell
foreach(迭代变量名 : 被迭代值){
  迭代循环体
};
```

- 用法和Java中类似
- 需要注意的是，foreach语句也算是一条语句，因此最后需要添加分号[;]结尾
- 举例

```shell
sum=0;
foreach(item : [1,2,3,4,5]){
  sum=${sum}+${item};
}
foreach(item : ${arr}){
  if(${arr}==null){
    continue;
  };
  sum=${sum}+${item};
  if(${sum}>10){
    break;
  };
}
${sum};
```

- 在这个例子中,介绍了大多数情况下foreach语句的使用场景
- 包含直接使用立即值[1,2,3,4,5]进行迭代和使用引用值${arr}进行迭代
- 同时演示了结合if进行continue和break控制的场景

- for循环语句
- 定义

```shell
for(初始化语句 ; 条件语句 ; 增量语句){
  循环体
};
```

- 和Java中常用的for-i循环的结构一致
- 需要注意的是，for语句也算是一条语句，因此最后需要添加分号[;]结尾
- 举例

```shell
sum=0;
for(i=0;i<10;i=${i}+1){
  if(${i}%2==0){
    sum=${sum}+${i};
  };
};
${sum};
```

- 在这个例子中，就展示了和Java中对应的for-i循环的使用方法

- while循环语句
- 定义

```shell
while(条件语句){
  循环体
};
```

- 和Java中的while循环的结构一致
- 需要注意的是，while语句也算是一条语句，因此最后需要添加分号[;]结尾
- 举例

```shell
sum=0;
i=0;
while(${i}<10){
  if(${i}%2==0){
    sum=${sum}+${i};
  };
  i=${i}+1;
};
${sum};
```

- 在这个例子中，就展示了while循环的使用方法

- return语句
- 定义

```shell
return;
return 变量;
```

- return语句可以不返回值，也可以返回一个值
- 区别，不带返回值，则整个脚本的返回值为null
- 带了返回值，则整个脚本的返回值为被返回的值
- 举例

```shell
return;
return 1;
return ${sum};
```

- break/continue语句
- 用于在循环(foreach/for/while)中进行控制
- 属于关键字
- 定义

```shell
break;
continue;
```

- 具名参数函数
- 和常规函数调用一样，只不过，这种调用，适用于一些特殊的场景
- 常常将参数名和值最终合并为一个Map对象调用目标方法
- 定义

```shell
函数名(具名参数列表)
```

- 具名参数定义

```shell
参数名 : 参数值
```

- 举例

```shell
render(str: "123",regex:"\\d+",replacement:"true")
```

- 这种，一般适用于这样的转换目标函数

```shell
Object render(Map<String,Object> params);
params.put("str","123");
params.put("regex","\\d+");
params.put("replacement","true");
```

- 或者也可以用于一些自定义参数，却不是Java类函数的场景中
- 总之，适用于需要自己解析这样的Map作为函数参数的场景
- DefaultTinyScriptResolver.beforeFunctionCall
- 根据需要重写此函数
- 使用Reference.of()方法正确的返回值，如果处理不了
- 则使用Reference.nop()表示无法处理

## 特别注意

- 本脚本不支持自然意义上的数学运算优先级
- 也就是先乘除后加减的规则，以及从左向右计算的规则
- 如果涉及到这种情况，请使用括号表达式进行限制运算顺序

## 综合使用案例

- 简单用法示例

```shell
num=1+1.125;
num2=${num}+10L;
tmp=new String("@@@");
str=${str}+1;
sadd=${str};
svl=String.valueOf(1L);
slen=${str}.length();
srptlen=${str}.repeat(2).length();

complex=[{
 username: "123",
 roles: ["admin","log"],
 status: true,
 age: 12,
 image: ${str},
 len: String.length(),
 token: null
}];

streq=${str}==${sadd};
strneq=${str}==${tmp};
numeeq=${num}>=${slen};

if(${num}>4){
    ok=3;
}else if(${num}>3){
    ok=2;
}else{
    ok=1;
};

```

- 运行输入上下文，准备参数如下

```java
Map<String, Object> context = new HashMap<>();
context.put("str", "1,2,3 4-5-6  7  8  9");
Object ret = TinyScript.script(formula, context);
System.out.println(ret);
System.out.println(context);
```

- 运行输出结果如下

```shell
1
{num=2.125, srptlen=42, svl=1, str=1,2,3 4-5-6  7  8  91, numeeq=false, tmp=@@@, streq=true, slen=21, complex=[{username=123, roles=[admin, log], status=true, age=12, image=1,2,3 4-5-6  7  8  91, len=0, token=null}], sadd=1,2,3 4-5-6  7  8  91, strneq=false, ok=1, num2=12.125}

```

- 复杂使用案例
    - 此案例仅说明复杂的场景，不具有实际运行性

```shell
user.status=new BigDecimal(${role.perm}.lrtim(Func.rtrim(replace(${user.name}.isEmpty("user",true).length().size(),"s+",12.125f,0x56l,0t27,0b101L,true),";"),",")).intValue()
```