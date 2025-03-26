# jdbc-procedure 语法转换对照入门
- jdbc-procedure (也称 XProc4J, Xml Procedure for Java) 是为了进行去数据库过程而实现的Java框架
- 目的是为了取代数据库过程的依赖性，将过程转换为Java代码实现
- 但是由于Java代码繁荣的语法和对代码中字符串字面值的处理不友好等特点
- 因此采用XML为主体，参考Mybatis的XML标签写法，构建以XML+JDBC+脚本引擎+模板引擎为基础技术支持的存储过程方案
- 用来替换数据库中的一切过程体，包括存储过程（procedure）和函数(function)
- 因此理想情况下，是可以做到不再需要数据库端的存储过程和函数了
- 但是，有些SQL语句中可能会使用到函数的情况，这也可以保留一部分函数在数据库中，不进行转换
- 同时，为了进行多数据库类型的支持，也支持了dialect方言性质，用来为不同的数据库写不同的代码语句
- 运行时根据数据库连接选择不同的dialect方言的语句进行执行
- 另外，由于实际处理中，还存在从不同的数据源之间进行交叉操作的情况，因此也可以使用datasource指定数据源进行执行语句
- 因此，在同一个过程中，是允许不同的数据源之间进行操作的，比如ETL操作

## 重要概念
- 将每个存储过程视为一个XML文件
- 按照XML格式的顺序进行执行语句
- 每个XML执行时，运行在一个Map对象的上下文中
- 这个对象包含了，变量的读取，全局变量，JDBC连接等信息
- 默认情况下，每个数据源在过程中，都是用同一个Connection连接
- 即使在发生过程内部调用其他过程时，也是同一个连接
- 除非显式指定了使用新的连接

## 技术实现
- 技术上，以解析XML文件为主，为了保持XML文件中的属性顺序和报错日志定位，内部使用SAX解析
- 参考Mybatis(成熟的ORM框架)的XML语法特点，进行顺序化的语句调用的XML定义过程
- 因为Mybatis使用OGNL(对象图导航语言)作为默认的表达式引擎，应用在取值和test属性上等地方
- 因此，为了给使用Mybatis熟练的人员使用，此类表达式默认情况下也采用OGNL(visit/test)
- 虽然，大部分语法和Mybatis相同，但是使用上略有一些差异
- 主要的差异在于，底层并不是基于Mybatis构建的，而是直接基于JDBC实现，没有用Mybatis的依赖库
- 其次引入JavaScript，Groovy，TinyScript,Java等语言，作为脚本引擎(eval)，用来处理一些XML标签不方便处理的内容
- 其中，TinyScript是为了解决JAVA语法冗繁的书写规则、从上下文中读写值麻烦、嵌套函数调用等问题，专门设计的可拓展语言
- 设计的初衷就是为了解决转换存储过程时的困难度和简易度
- 由于存储过程中，存在大量的字符串拼接等的处理场景
- 因此引入Velocity(模板引擎)来进行字符串的拼接和渲染(render)操作
- 上面这一段描述中，提到了几个关键词eval,visit,test,render
- 这是比较核心的几个内容
- eval: 表示进行表达式或者脚本执行，主要是用于复杂表达式的执行，实际可以使用Java(eval-java),JavaScript(eval-js),Groovy(eval-groovy),TinyScript(eval-ts)
- visit: 表示从Map上下文中访问值,实现上优先使用反射获取值，其次使用OGNL表达式获取值
- test: 表示计算表达式的布尔逻辑值，当表达式的结果不是boolean类型时，会隐式转换为boolean类型，默认使用OGNL引擎进行表达式运算
- render: 表示进行字符串渲染，使用Velocity模板引擎实现

## 存储过程转换案例讲解
- 下面以Oracle语法的存储过程转换对照进行说明
- 其他SQL语言的可以进行参考转换

## 转换注意事项
- 由于大部分数据库对于变量时忽略大小写的
- 但是在Java环境中，变量是区分大小写的
- 因此，转换时应该根据自身数据库是否忽略大小写
- 决定对变量如何处理
- 比如，在Oracle中，不区分大小写，但是Oracle都喜欢使用大写来命名
- 因此，可以考虑将所有变量名都大写进行处理

## 转换对照介绍

### 前置知识
- 可以使用哪些XML节点，节点应该怎么使用?
- 查看procedure.xml中的节点注释描述

### 存储过程定义
- 本框架的目的就是进行过程的转换操作
- 因此这也是必要的一部分
- 先看下原来的定义
```sql
PROCEDURE SP_PREDICATE_COND(IN_CITY_CODE      NUMBER,
                           IN_SUM_MONTH      NUMBER,
                            IN_COND_ID          NUMBER,
                           O_MSG          OUT VARCHAR2,
                           O_CODE            OUT NUMBER)
```
- 再看一下转换后的定义
- 转换方式1
- 使用XML进行转换，这也是本框架核心的部分
- 编写一个文件
- 下面是文件名和文件路径
- 文件路径是默认的扫描路径
- 文件名则推荐和要转换的过程名称一致
```shell
resources/procedure/SP_PREDICATE_COND.xml
```
- 下面就是定义的文件内容部分
- 注意几个点
- 根节点名为procedure，使用id属性指定过程的名称
- 这个id需要在项目中是唯一的，因为在发生调用时，使用的就是这个id
- 另外，为了方便他人调用时明确的直到过程需要使用哪些参数
- 以及参数的类型要求，则使用其他属性指定形参名称，使用.来标注类型
- 特别的，建议使用.out来标注出参，这样调用方才能知道有哪些是出参
- 当然，也可以使用.in来标注为入参，但是一般不用，默认认为都是入参
- 因此，调用方需要指定这些属性名的参数进行调用
- 同时，调用完毕之后，可以取到标有.out名称属性的输出参数
- 这是一个约定
```xml
<procedure id="SP_PREDICATE_COND"
                IN_CITY_CODE.int=""
                IN_SUM_MONTH.int=""
                IN_COND_ID.int=""
                O_MSG.string.out=""
                O_CODE.int.out="">

</procedure>
```
- 另外，某些情况下，转换更适合使用Java代码来编写
- 使用XML来编写反而更加麻烦
- 这种情况下，也可以选择使用java-bean的方式来进行转换
- 下面是转换的内容
- 使用@Component注册为一个spring-bean对象
- 使用@JdbcProcedure注解指定过程名称value,使用arguments指定需要的参数及其类型
- 写法和XML类似
- 但是，因为是存储过程，因此一般返回值都为null
- 框架将会将入参params作为结果返回给调用者
- 同时，由于是java类，返回值也会被设置到结果的params的return键上
- 这一点和函数的转换上，是一个区别点，需要注意
- 可以使用executor.call调用，得到返回的结果params
- 实际上返回的就是入参的params对象
```java
@Component
@JdbcProcedure(
        value="SP_PREDICATE_COND",
        arguments = {
                "IN_CITY_CODE.int",
                "IN_SUM_MONTH.int",
                "IN_COND_ID.int",
                "O_MSG.string.out",
                "O_CODE.int.out
        }
)
public class SpPredicateCondJavaCaller implements JdbcProcedureJavaCaller {
    @Override
    public Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        String inCityCode = executor.visitAs("IN_CITY_CODE",params);
        
        return null;
    }
}
```

### 函数定义
- 函数其实上，在本框架中，是一个特例
- 本质上也看做一种过程，只不过有一些特点
- 先来看一下原来的定义
```sql
FUNCTION F_IS_TEST(IN_CITY_CODE      NUMBER,
                          IN_SUM_MONTH      NUMBER,
                          IN_LOG_ID        NUMBER) RETURN NUMBER
```
- 再来看一下转换后的XML定义
- 和存储过程的定义是基本一样的
- 只不过，区别在于返回值的描述
- 这个是固定的约定，约定return名称的属性就是对返回值的描述
- 其他的和存储过程时一致的
```xml
<procedure id="F_IS_TEST"
        IN_CITY_CODE.int=""
        IN_SUM_MONTH.int=""
        IN_LOG_ID.int=""
        return.int="">

</procedure>
```
- 当然，也可以使用java类来实现
- 下面就是java定义
- 需要注意几个点
- 和存储过程基本一致
- 但是，区别点是返回值，会被当做结果params的return属性
- 也就是说，return值是有意义的
- 如果在代码中设置了return属性，也将会被代码的return值覆盖
- 这点在存储过程的转换也是一样的
- 可以使用executor.invoke进行调用，得到返回值
- 当然，也可以使用executor.call调用得到结果params，然后自行从params中取出return键的值即为return的值
```java
@Component
@JdbcProcedure(
        value="F_IS_TEST",
        arguments = {
                "IN_CITY_CODE.int",
                "IN_SUM_MONTH.int",
                "IN_LOG_ID.int",
                "return.int"
        }
)
public class FuncIsTestJavaCaller implements JdbcProcedureJavaCaller {
    @Override
    public Object exec(JdbcProcedureExecutor executor, Map<String, Object> params) throws Throwable {
        String inCityCode = executor.visitAs("IN_CITY_CODE",params);
        
        return 1;
    }
}
```

### 变量定义
- 在存储过程中通常会在一开头定义要使用到的变量
- 但是，因为转换后使用的是Map保存变量，所以只要变量不需要有初始值
- 也就是变量初始值为null的情况下，可以不定义变量
- 原始语句
```sql
v_begin_time            DATE;
V_CITY_CODE  VARCHAR2(64) := '101010';
```
- 转换方式1
- 直接使用XML标签进行赋值
```xml
<lang-set result="V_BEGIN_TIME" value.null=""/>
<lang-set result="V_CITY_CODE" value.string="101010"/>
```
- 转换方式2
- 使用TinyScript进行转换
```xml
<lang-eval-ts>
    V_BEGIN_TIME=null; // 此句实际上没什么意义
    V_CITY_CODE='101010';
    // TinyScript默认会返回最后一条语句执行的结果
    // 因此可以不用return语句
</lang-eval-ts>
```
- 转换方式3
- 使用Java进行转换
```xml
<lang-eval-java>
    params.put("V_BEGIN_TIME",null);
    params.put("V_CITY_CODE","101010");
    return null; 
    // 因为Java脚本实际上会被编译为一个Class运行，这一段会作为函数体
    // 因此需要使用return语句进行返回
</lang-eval-java>
```
- 转换方式4
- 使用groovy进行转换
```xml
<lang-eval-groovy>
    params.V_BEGIN_TIME=null;
    params.V_CITY_CODE="101010";
    // groovy脚本默认会返回最后执行的一条语句的结果，因此可以不用return语句
</lang-eval-groovy>
```
- 转换方式5
- 使用JavaScript进行转换，需要注意的是，JavaScript调用Java也需要像Java一样进行访问
```xml
<lang-eval-javascript>
    params.put('V_BEGIN_TIME',null);
    params.put("V_CITY_CODE",'101010');
</lang-eval-javascript>
```
- 当出现大量的赋值语句的时候，我更偏向于使用TinyScript进行转换

### 变量赋值
- 其实在变量赋值是，转换方式和变量定义差不多
- 这里就少写几种转换方式了
```sql
v_begin_time:=sysdate;
V_CITY_CODE:=IN_CITY_CODE||'00'; -- 这里
```
- 转换方式1
- 直接使用XML标签进行转换
```xml
<lang-set result="V_BEGIN_TIME" value.date-now=""/>
<lang-set result="V_CITY_CODE" value.render="${IN_CITY_CODE}00"/>
<!-- 这里字符串拼接，使用render修饰符进行字符串模板渲染 -->
```
- 转换方式2
- 使用TinyScript进行转换
```xml
<lang-eval-ts>
    V_BEGIN_TIME=new Date(); 
    V_CITY_CODE=${IN_CITY_CODE}+'00'; // 字符串拼接可以直接使用+号连接，取变量则使用${}包裹
    // V_CITY_CODE=R"${IN_CITY_CODE}00"; // 或者也可以使用模板字符串语法
</lang-eval-ts>
```

### 单分支条件语句-if
- if语句的情况，在转换时比较复杂
- 因此分作两种情况讨论，第一种是只有一个if分支的
- 也就是本节讨论的内容
- 另一种是多个分支的，也就是if-else的情况
- 现在先来看一下原来的语句
```sql
if V_LINK_OPER = 'OR' and v_cond_type=0 and v_role_key in ('admin','logger') and v_ogran_key like 'sys%' then
   O_MSG:='OK';
end if;
```
- 现在来看下对应的转换方式
- 第一种，使用XML进行转换
- 需要注意，SQL中的等于判定使用一个等号，而XML中的判定使用双等号
- 其次，XML中使用的是OGNL表达式，因此，字符串使用双引号
- in操作符在OGNL中是使用{}花括号表示的
- 针对like这种场景，则使用java的方法进行转换，startsWith,endsWith,contains进行表达
```xml
<lang-if test='V_LINK_OPER == "OR" and v_cond_type==0 and v_role_key in {"admin","logger"} and v_ogran_key.startsWith("sys")'>
    <lang-set result="O_MSG" value.string="OK"/>
</lang-if>
```
- 另外针对这种条件内部处理比较简单的
- 也可以考虑使用TinyScript进行转换
- 需要注意，TinyScript的表达式中，和OGNL中接近，但有区别
- 第一个就是变量引用得使用${}进行包裹
- 第二个就是字符串不区分单双引号，都认为是字符串
- in操作符，是和JSON一样的表达，使用[]中括号进行表示的
- 对于like的处理，和OGNL的表达一样，直接使用java的方法进行表示
```xml
<lang-eval-ts>
    if(${V_LINK_OPER} == 'OR' 
        and ${v_cond_type}==0 
        and ${v_role_key} in ['admin','logger'] 
        and ${v_ogran_key}.startsWith("sys")){
        O_MSG='OK';
    };
</lang-eval-ts>
```

### 多分支条件语句if-else
- 针对多分支语句时，XML中则需要额外的标签来处理
- 但是总体和Mybatis中对多分支的处理一致
- 直接来看例子吧
- 原来的语句如下
```sql
if v_score >= 90 then
   v_grade:='A';
elseif v_score >=80 then
    v_grade:='B';
elseif v_score >= 60 then
    v_grade:='C';
else
    v_grade:='D';
end if;
```
- 先来看使用XML方式转换吧
- 需要注意，因为是在XML中编写
- 因此，需要注意一些符号的转义的问题
- 例如如下符号
```shell
< &lt;
> &gt;
& &amp;
```
- 直接来看转换的XML结果
```xml
<lang-choose>
    <lang-when test="v_score >= 90">
        <lang-set result="v_grade" value.string="A"/>
    </lang-when>
    <lang-when test="v_score >= 80">
        <lang-set result="v_grade" value.string="B"/>
    </lang-when>
    <lang-when test="v_score >= 60">
        <lang-set result="v_grade" value.string="C"/>
    </lang-when>
    <lang-otherwise>
        <lang-set result="v_grade" value.string="D"/>
    </lang-otherwise>
</lang-choose>
```
- 这种情况，其实使用TinyScript就比较合适
- 不用那么繁琐了
- 需要注意的是if语句最后的分号记得加
```xml
<lang-eval-ts>
    if(${v_score} >= 90){
        v_grade='A';
    }else if(${v_score} >= 80){
        v_grade='B';
    }else if(${v_score} >= 60){
        v_grade='C';
    }else{
        v_grade='D';
    };
</lang-eval-ts>
```
- 当然使用其他脚本语言也可以
- 比如使用groovy，JavaScript，java都是可以的

### 函数调用
- 在存储过程中，很多地方都可能使用了数据库内建的函数，例如rtrim/replace等
- 也会使用自定义的UDF函数
- 转换之后，则有几种选择，一个就是使用Java将自rtrim/replace等内建函数进行实现，好处是不用考虑数据库类型，坏处是有的函数可能不好实现
- 第二种是使用SQL查询实现，这种方式的局限性就是数据库要兼容
```sql
COND.CONTENT:=rtrim(COND.CONTENT,';');
COND.CONTENT:=replace(COND.CONTENT,'1=1','1 = 1 ');
v_f_cnt:=LENGTH(COND.CONTENT) - LENGTH(REPLACE(COND.CONTENT, ';', ''))+1;
```
- 转换方式1
- 使用XML标签进行转换，这种函数的大量大量或者拼接
- 不适合使用XML标签进行描述
```xml
<lang-invoke result="COND.CONTENT" method="rtrim" target="COND.CONTENT" arg0.string=";"/>
<lang-invoke result="COND.CONTENT" method="replace" target="COND.CONTENT" arg0.string="1=1" arg1.string="1 = 1 "/>
<lang-invoke result="tmp_len" method="length" target="COND.CONTENT" />
<lang-invoke result="tmp_str" method="replace" target="COND.CONTENT" arg0.string=";" arg1.string=""/>
<lang-invoke result="tmp_str_len" method="length" target="tmp_str" />
<lang-eval result="V_F_CNT">
    tmp_len-tmp_str_len+1
</lang-eval>
```
- 转换方式2
- 使用数据库自己的函数，通过查询的方式进行转换
```xml
<sql-query-object result="COND.CONTENT" result-type="string">
    select rtrim(#{COND.CONTENT},';') as v1 from dual
</sql-query-object>

<sql-query-object result="COND.CONTENT" result-type="string">
    select replace(#{COND.CONTENT},'1=1','1 = 1 ') as v1 from dual
</sql-query-object>

<sql-query-object result="V_F_CNT" result-type="int">
    select LENGTH(#{COND.CONTENT}) - LENGTH(REPLACE(#{COND.CONTENT}, ';', ''))+1 as v1 from dual
</sql-query-object>
```
- 转换方式3
- 使用Java进行转换
```xml
 <lang-eval-java>
        String content = executor.visitAs("COND.CONTENT", params);
        content=ContextFunctions.trim(content);
        content=content.replace("1=1","1 = 1 ");
        executor.visitSet(params,"COND.CONTENT",content);
        int len=content.length()-content.replace(";","").length()+1;
        executor.visitSet(params,"V_F_CNT",len);
        return null;
</lang-eval-java>
```
- 转换方式4
- 使用TinyScript进行转换
```xml
<lang-eval-ts>
    COND.CONTENT=rtrim(${COND.CONTENT},';');
    COND.CONTENT=replace(${COND.CONTENT},'1=1','1 = 1 ');
    V_F_CNT=length(${COND.CONTENT}) - length(replace(${COND.CONTENT}, ';', ''))+1;
</lang-eval-ts>
```
- 在这种场景下，使用XML标签或者使用其他eval脚本语言
- 都比原来的语句复杂的多
- 因为TinyScript设计就是为了解决这个问题的，所以更偏向于使用TinyScript进行转换

### UDF自定义函数调用
- UDF用户自定义函数常常也会出现在存储过程中
- 现在，这种情况比较复杂，我们以一个例子来说明
- 先看原来函数的定义
```sql
FUNCTION F_IS_TEST(IN_CITY_CODE      NUMBER,
                          IN_SUM_MONTH      NUMBER,
                          IN_LOG_ID        NUMBER) RETURN NUMBER
```
- 那么如果这个函数转换为XML来写应该是这样的
- 虽然定义一个过程，只需要指定ID就行，但是应该写上函数需要的参数，以及参数的类型
- 以及返回值的类型
```xml
<procedure id="F_IS_TEST"
        IN_CITY_CODE.int=""
        IN_SUM_MONTH.int=""
        IN_LOG_ID.int=""
        return.int="">

</procedure>
```
- 那再来看一下原来是怎么调用的
```sql
V_IS_TEST:=F_IS_TEST(101010,V_SUM_MONTH,V_LOG_ID);
```
- 也或者是这样的调用
```sql
v_sql:='select F_IS_TEST('||101010||','||V_SUM_MONTH||','||V_LOG_ID||') from dual';

execute immediate v_sql into V_IS_TASK_TEST;
```
- 那么一下都是可以的转换方式
- 转换方式1
- 使用XML标签进行调用，则属性名就是目标的参数名，属性值就是传递的值
- 即属性名为形参，属性值为实参
```xml
<function-call refid="F_IS_TEST"
           result="V_IS_TEST"
           IN_CITY_CODE.int="101010"
           IN_SUM_MONTH="V_SUM_MONTH"
           IN_LOG_ID="V_LOG_ID"/>
```
- 转换方式2
- 还是使用XML标签，但是使用存储过程的方式，自行提取返回值
```xml
<procedure-call refid="F_IS_TEST"
               result="callParams"
               IN_CITY_CODE.int="101010"
               IN_SUM_MONTH="V_SUM_MONTH"
               IN_LOG_ID="V_LOG_ID"/>
<lang-set result="V_IS_TEST" value="callParams.return"/>
<!-- 如果是使用procedure-call调用函数，那么在result这个Map中的return键存的就是返回值，所以先提取出来，方便后续处理 -->
```
- 转换方式3
- 使用Java代码调用
```xml
<lang-eval-java>
        int V_SUM_MONTH = executor.visitAs("V_SUM_MONTH", params);
        int IN_LOG_ID = executor.visitAs("IN_LOG_ID", params);

        int ret=executor.invoke("F_IS_TEST", executor.mapBuilder()
                .put("IN_CITY_CODE", 101010)
                .put("IN_SUM_MONTH", V_SUM_MONTH)
                .put("IN_LOG_ID", IN_LOG_ID)
                .get()
        );
        executor.visitSet(params,"V_IS_TEST",ret);
        return null;
</lang-eval-java>
```
- 转换方式4
- 和上面一样，但是因为lang-eval-java支持result属性来指定返回值
- 因此，也可以配合使用
```xml
<lang-eval-java result="V_IS_TEST">
        return executor.invoke("F_IS_TEST", executor.mapBuilder()
                .put("IN_CITY_CODE", 101010)
                .put("IN_SUM_MONTH", executor.visit("V_SUM_MONTH", params))
                .put("IN_LOG_ID", executor.visit("IN_LOG_ID", params))
                .get()
        );
</lang-eval-java>
```
- 转换方式5
- 使用TinyScript具名参数调用,这样就不用考虑参数顺序问题，只考虑参数名对应就行
```xml
<lang-eval-ts>
    V_IS_TEST=F_IS_TEST(
        IN_CITY_CODE:101010,
        IN_SUM_MONTH:${V_SUM_MONTH},
        IN_LOG_ID:${V_LOG_ID}
        );
</lang-eval-ts>
```
- 转换方式6
- 使用TinyScript参数顺序调用，这就要求顺序和XML中属性的顺序一致
```xml
<lang-eval-ts>
    V_IS_TEST=F_IS_TEST(101010,${V_SUM_MONTH},${V_LOG_ID});
</lang-eval-ts>
```
- 上面这几种方式中，使用TinyScript是最好最方便快捷的

### Proc存储过程调用
- 其实函数掉调用时从存储过程的调用简化来的
- 因此，很多部分和存储过程时基本一样的
- 下面来看一下这个存储过程的定义
```sql
PROCEDURE SP_PREDICATE_COND(IN_CITY_CODE      NUMBER,
                           IN_SUM_MONTH      NUMBER,
                            IN_COND_ID          NUMBER,
                           O_MSG          OUT VARCHAR2,
                           O_CODE            OUT NUMBER)
```
- 再看一下转换后的定义
```xml
<procedure id="SP_PREDICATE_COND"
                IN_CITY_CODE.int=""
                IN_SUM_MONTH.int=""
                IN_COND_ID.int=""
                O_MSG.string.out=""
                O_CODE.int.out="">

</procedure>
```
- 看一下原来怎么调用的
```sql
v_sql := ' begin SP_PREDICATE_COND(:1,:2,:3,:4,:5) ; end ; ';
execute immediate v_sql
    using in V_CITY_CODE, in V_SUM_MONTH, in V_COND_ID, out V_MSG, out  V_CODE;
```
- 转换方式1
- 使用XML标签进行转换
- 因为O_MSG和O_CODE都是输出变量，因此也可以不写
- 另外使用lang-set将变量提取出来，以和原来过程中的变量相兼容
- 可以避免一些转换过程中变量名的使用错误问题
```xml
<procedure-call refid="SP_PREDICATE_COND"
                result="callParams"
                IN_CITY_CODE="V_CITY_CODE"
                IN_SUM_MONTH="V_SUM_MONTH"
                IN_COND_ID="V_COND_ID"
                O_MSG="V_MSG"
                O_CODE="V_CODE"/>
<lang-set result="V_MSG" value="callParams.O_MSG"/>
<lang-set result="V_CODE" value="callParams.O_CODE"/>
```
- 因为说了输出参数可以不写，因此这样也是可以的
```xml
<procedure-call refid="SP_PREDICATE_COND"
                result="callParams"
                IN_CITY_CODE="V_CITY_CODE"
                IN_SUM_MONTH="V_SUM_MONTH"
                IN_COND_ID="V_COND_ID"/>
```
- 转换方式2
- 使用Java代码转换
```xml
<lang-eval-java>
    Map ret=executor.call("SP_PREDICATE_COND",executor.mapBuilder()
            .put("IN_CITY_CODE",executor.visit("V_CITY_CODE",params))
            .put("IN_SUM_MONTH",executor.visit("V_SUM_MONTH",params))
            .put("IN_COND_ID",executor.visit("V_COND_ID",params))
            .get()
    );
    executor.visitSet(params,"V_MSG",executor.visit("O_MSG",ret));
    executor.visitSet(params,"V_CODE",executor.visit("O_CODE",ret));
    return null;
</lang-eval-java>
```
- 转换方式3
- 使用groovy代码转换
```xml
<lang-eval-groovy>
    def  ret=executor.call("SP_PREDICATE_COND",[
            IN_CITY_CODE:params.V_CITY_CODE,
            IN_SUM_MONTH:params.V_SUM_MONTH,
            IN_COND_ID:params.V_COND_ID
        ]
    );
    params.V_MSG=ret.O_MSG;
    params.V_CODE=ret.O_CODE;
</lang-eval-groovy>
```
- 转换方式4
- 使用TinyScript脚本转换
```xml
<lang-eval-ts>
    callParams=SP_PREDICATE_COND(
        IN_CITY_CODE:${V_CITY_CODE},
        IN_SUM_MONTH:${V_SUM_MONTH},
        IN_COND_ID:${V_COND_ID}
    );
    V_MSG=${callParams.O_MSG};
    V_CODE=${callParams.O_CODE};
</lang-eval-ts>
```

### Java程序调用XML过程
- 这部分，直接使用上面内部调用的例子进行演示
- Java程序调用，也就是在Java程序中调用XML的过程的方式
- 也就是程序如何运行，程序的运行入口
- 在与springboot集成的环境starter中，会自动从springboot环境中获取数据源等相关信息注入到执行环境中
- 这部分是自动装配实现的，因此一般不考虑
- 而是直接进行使用即可
- 调用方式1，使用静态工具类进行调用
- 这种调用方式将是比较常见的，也是常规调用方式
- 内部屏蔽了一些不必要的细节
- 函数调用
```java
int vIsTest=JdbcProcedureHelper.invoke("F_IS_TEST", (map)->
                map.put("IN_CITY_CODE", 101010)
                .put("IN_SUM_MONTH", 202501)
                .put("IN_LOG_ID", 1)
        );
```
- 过程调用
```java
Map<String,Object> ret=JdbcProcedureHelper.call("SP_PREDICATE_COND",(map)->
                map.put("IN_CITY_CODE",101010)
                .put("IN_SUM_MONTH",202501)
                .put("IN_COND_ID",6666)
                .get()
        );
String oMsg = (String)ret.get("O_MSG");
Integer oCode=(Integer)ret.get("O_CODE");
```
- 当然，你也可以选择将executor注入进来
- 然后使用executor进行调用
- 那就和上面的基本一样了
- 注入executor
```java
@Autowired
private JdbcProcedureExecutor executor;
```
- 也或者通过静态方法获取
```java
JdbcProcedureExecutor executor=JdbcProcedureHelper.getExecutor();
```
- 然后和上面一样的调用即可
- 只不过，因为是使用executor，executor提供了一些其他的辅助方法可以使用
- 在某些场景下会比较实用
- 函数调用
```java
int vIsTest=executor.invoke("F_IS_TEST", (map)->
                map.put("IN_CITY_CODE", 101010)
                .put("IN_SUM_MONTH", 202501)
                .put("IN_LOG_ID", 1)
        );
```
- 过程调用
```java
Map<String,Object> ret=executor.call("SP_PREDICATE_COND",(map)->
                map.put("IN_CITY_CODE",101010)
                .put("IN_SUM_MONTH",202501)
                .put("IN_COND_ID",6666)
        );
String oMsg = executor.visitAs(ret,"O_MSG");
Integer oCode=executor.visitAs(ret,"O_CODE");
```

### 游标转换
- 游标cursor也是一个比较常见的现象
- 现在看一下原来的语句
- 这个语句比较简单，只是一个实例，没有实际的意义
```sql
cur_obj  sys_refcursor; -- 声明游标
-- 游标的语句
v_sql:='select a.USER_NAME,a.nick_name from '||V_SCHEMA_PREFIX||'SYS_USER a where a.STATUS='||V_USER_SATUS||' and a.DEL_FLAG='||V_DEL_FLAG;
OPEN cur_obj FOR v_sql; -- 打开游标

LOOP
FETCH cur_obj INTO v_user_name,v_nick_name ; -- 将游标的结果保存到变量中
            EXIT WHEN cur_obj%NOTFOUND; -- 循环游标，直到没有数据为止

            -- 游标执行的操作
            update sys_user
            set role_id=V_ROLE_ID
            where USER_NAME=v_user_name
            ;

END LOOP;
```
- 针对这种情况，目前提供的转换方式是使用XML
- 这样对于SQL语句的编辑更方便
```xml
<sql-cursor item="tmpRowMap"> <!-- 游标返回的必定是Map对象，指定Map对象存放的键 -->
    <sql-query-list> <!-- 游标指定的语句 -->
        select a.USER_NAME,a.nick_name
        from ${V_SCHEMA_PREFIX}SYS_USER a
       where a.STATUS=#{V_USER_SATUS}
         and a.DEL_FLAG=#{V_DEL_FLAG}
    </sql-query-list>
    <lang-body>
        <!-- 将游标的变量提取出来，后续则可以不用变更变量名 -->
        <lang-set result="v_user_name" value="tmpRowMap.USER_NAME"/>
        <lang-set result="v_nick_name" value="tmpRowMap.NICK_NAME"/>

        <!-- 游标指定的操作 -->
        <sql-update>
            update sys_user
            set role_id=#{V_ROLE_ID}
            where USER_NAME=#{v_user_name}
        </sql-update>
    </lang-body>
</sql-cursor>
```

### for循环转换
- 有时候，也会存在for春环变量的使用
- 也就是for...in的语法
- 这种其实本质上可以理解为一种游标
- 因此也可以使用游标的转换方式
- 看一下原来的语句
```sql
FOR c_dict IN (SELECT * FROM SYS_DICT t WHERE DICT_KEY=V_USER_GROUP_KEY   And T.STATUS  =1  )
LOOP
      
    delete from SYS_USER
    where USER_GOUP=c_dict.DICT_VALUE;

END LOOP;
```
- 直接使用XML转换即可
```xml
<sql-cursor item="c_dict"> <!-- 因为语句返回的就是一个对象，所以直接使用原来的名称 -->
    <sql-query-list> <!-- for的语句 -->
        SELECT * FROM SYS_DICT t
        WHERE DICT_KEY=#{V_USER_GROUP_KEY}   
          And T.STATUS  =1
    </sql-query-list>
    <lang-body>

        <sql-update>
            delete from SYS_USER
            where USER_GOUP=#{c_dict.DICT_VALUE}
        </sql-update>
    </lang-body>
</sql-cursor>
```

### for-i型循环
- 这种其实就是for-i语句
- 直接进行转换即可
- 原始语句
```sql
v_size:=0;
for v_i in 2..v_count loop
    v_size:=v_size+v_i;
end loop;
```
- 这种语句，只是为了演示怎么进行转换
- 实际的情况，一般循环内部不会这么简单
- 因此先说，一般的转换方式
- 直接使用XML进行转换
- 使用begin指定初始值，使用end指定结束值，使用incr指定增量/步长
```xml
<lang-set result="v_size" value.int="0"/>
<lang-fori item="v_i" begin.int="2" end="v_count" incr.int="1">
    <lang-set result="v_size" value.eval="v_size+v_i"/>
</lang-fori>
```
- 当然，因为举例非常简单
- 可以使用嵌入脚本的方式
- 下面使用TinyScript来转换
```shell
<lang-eval-ts>
    v_size=0;
    for(v_i=2;${v_i} &lt; ${v_count}; v_i=${v_i}+1){
        v_size=${v_size}+${v_i};
    };
</lang-eval-ts>
```

### while型循环
- 这种语句其实也比较常见
- 直接转换即可
- 原始语句
```sql
v_size:=0;
v_i:=0;
while v_i < v_count loop
      v_size:=v_size+v_i;
      v_i=v_i+1;
end loop;
```
- 一般使用XML直接转换
```xml
<lang-set result="v_size" value.int="0"/>
<lang-set result="v_i" value.int="0"/>
<lang-while test="v_i &lt; v_count">
    <lang-set result="v_size" value.eval="v_size+v_i"/>
    <lang-set result="v_i" value.eval="v_i+1"/>
</lang-while>
```
- 当然示例比较简单
- 也可以使用TinyScript直接进行转换
```xml
<lang-eval-ts>
    v_size=0;
    v_i=0;
    while (${v_i} &lt; ${v_count} ) {
        v_size=${v_size}+${v_i};
        v_i=${v_i}+1;
    };
</lang-eval-ts>
```

### SQL语句拼接与执行
- SQL语句拼接是一个比较常见的场景
- 下面将介绍进行语句拼接，并执行拼接之后的语句的场景
- 先来看源语句
```sql
v_sql:='insert into sys_user (name,age';
if v_all = 1 then
   v_sql:=v_sql||',status,del_flag)';
else 
    v_sql:=v_sql||')';
end if;

if v_thrid=1 then
   v_sql:=v_sql||' select user_name,user_age from o_sys_user ';
else
    v_sql:=v_sql||' select username,age from syn_user ';
end if;

v_sql:=v_sql||' where 1=1 ';
     
if v_inc_date is not null then
   v_sql:=v_sql||' and modify_time > '||v_inc_date;
end if;

execute immediate v_sql;
```
- 这个案例比较简单
- 下面那直接进行转换
- 在这次的转换之中，我们使用多种方式组合来进行转换
- 目的只是为了表示，不是只有一种方法能够进行转换
- 灵活结合各种方法进行转换也是一个不错的选择
- 当然，这个案例比较简单，可以有更简单的转换方式
- 这个转换实例是在使用比较复杂的场景中常用的手段
```xml

<lang-set result="v_sql" value.string="insert into sys_user (name,age"/>

<lang-choose>
    <lang-when test="v_all == 1">
        <lang-render result="v_sql" _lang="sql">
            ${v_sql},status,del_flag)
        </lang-render>
    </lang-when>
    <lang-otherwise>
        <lang-render result="v_sql" _lang="sql">
            ${v_sql})
        </lang-render>
    </lang-otherwise>
</lang-choose>

<lang-choose>
    <lang-when test="v_thrid == 1">
        <lang-render result="v_sql" _lang="sql">
            ${v_sql} select user_name,user_age from o_sys_user
        </lang-render>
    </lang-when>
    <lang-otherwise>
        <lang-render result="v_sql" _lang="sql">
            ${v_sql} select username,age from syn_user
        </lang-render>
    </lang-otherwise>
</lang-choose>

<lang-set result="v_sql" value.render="${v_sql} where 1=1 "/>

<lang-eval-ts>
    if (${v_inc_date}!= null) {
        v_sql=${v_sql}+' and modify_time > '+v_inc_date;
    };
</lang-eval-ts>

<sql-update script="v_sql"/>
```
- 直接进行执行动态脚本即可
- 也就是直接按照Mybatis的方式组合就行
- 这种方式适合比较简单或者能够在转换时比较容易看出整体结构得场景
```xml
<sql-update>
    insert into sys_user (name,age
    <choose>
        <when test="v_all==1">
            ,status,del_flag)
        </when>
        <otherwise>
            )
        </otherwise>
    </choose>
    <choose>
        <when test="v_thrid==1">
            select user_name,user_age from o_sys_user
        </when>
        <otherwise>
            select username,age from syn_user
        </otherwise>
    </choose>
    <where>
        <if test="v_inc_date!=null">
            and modify_time > #{v_inc_date}
        </if>
    </where>
</sql-update>
```
- 当然由于这个例子的特殊性
- 针对字符串拼接的部分
- 可以直接使用eval-ts来即可
- 这种方式适合于语句较短，但是拼接工具量大的场景
```xml
<lang-eval-ts>
    v_sql='insert into sys_user (name,age';
    if (${v_all} == 1 ){
        v_sql=${v_sql}+',status,del_flag)';
    }else{
        v_sql=${v_sql}+')';
    };
    
    if (${v_thrid}==1 ){
        v_sql=${v_sql}+' select user_name,user_age from o_sys_user ';
    }else{
        v_sql=${v_sql}+' select username,age from syn_user ';
    };
    
    v_sql=${v_sql}+' where 1=1 ';
    
    if( ${v_inc_date} != null ){
        v_sql=${v_sql}+' and modify_time > '+${v_inc_date};
    };

</lang-eval-ts>

<sql-update script="v_sql"/>
```
- 当然执行SQL，不一定非要用sql-update标签
- 根据实际场景，选择合适的sql-标签进行使用

### 异常处理的转换
- 异常处理的转换比较复杂
- 同时有一部分异常，因为是基于JDBC的，并不能得到和原始的写法一样的异常类型
- 也就是说，大多数情况下，你都只能拿到一个SQLException异常
- 并且由于各个jdbc-driver实现（Oracle，Mysql）等的驱动抛出的异常类型不一致
- 因此处理也比较麻烦
- 因此，本节将只会介绍一些能够进行区分的异常类型的转换过程
- 未提及的，需要根据实际情况决定如何转换

#### 普通一般性异常（exception when others then）
- 这种异常在不同的数据库中可能表达方式不一样
- 主要的含义就是处理一切发生的异常
- 对应Java中，也就是处理Throwable类型的异常
- 先来看示例，这种用于在整个过程中发生异常的处理方式
- 一般书写在整个过程的末尾
- 下面是原始语句
```sql
PROCEDURE SP_TEST( O_CODE      OUT NUMBER,
                   O_MSG      OUT VARCHAR2,
                   IN_SUM_DATE     IN NUMBER
                  )
AS
    v_sql varchar2(4000);
BEGIN
    v_sql:='delete from xxx where '; -- 错误的语句
    execute immediate v_sql;

    -- 执行成功设置正常执行返回值
    O_CODE:=0;
    O_MSG:='ok';
    
EXCEPTION
  WHEN OTHERS THEN -- 发生其他异常
    -- 执行失败设置错误执行返回值
    O_CODE:=-1;
    O_MSG:='error: code='||SQLCODE||', msg='||SQLERRM;
end SP_TEST;
```
- 下面直接来看转换结果吧
```xml
<procedure id="SP_TEST"
               O_CODE.int=""
               O_MSG.string=""
               IN_SUM_DATE.int="">
    <lang-try> <!-- 通过try-catch语句块包裹，自行决定需要catch哪些异常，catch块可以支持多个，和Java一样，按顺序捕获 -->
        <lang-body>
            <lang-render result="v_sql" _lang="sql">
                delete from xxx where
            </lang-render> <!-- 错误的语句 -->
            <sql-update script="v_sql"/>

            <!-- 执行成功设置正常执行返回值 -->
            <lang-set result="O_CODE" value.int="0"/>
            <lang-set result="O_MSG" value.string="ok"/>
        </lang-body>
        <lang-catch type="Throwable|java.sql.SQLException" e="e"> <!-- 发生其他异常，捕获所有异常类Throwable或SQLException，不写type的时候，默认也是Throwable -->
            <lang-eval-ts>
                error_msg=${e}.getClass().getName()+':'+${e}.getMessage();
            </lang-eval-ts>
            <!-- 执行失败设置错误执行返回值 -->
            <lang-set result="O_CODE" value.int="-1"/>
            <lang-set result="O_MSG" value.render="error:${error_msg}"/>
        </lang-catch>
    </lang-try>
</procedure>
```
- 还有一种情况，内部的异常块
- 也就是过程内部的一小段异常处理块
- 不是过程级别的情况
- 直接看原始语句
```sql
PROCEDURE SP_TEST( O_CODE      OUT NUMBER,
                   O_MSG      OUT VARCHAR2,
                   IN_SUM_DATE     IN NUMBER
                  )
AS
    v_sql varchar2(4000);
BEGIN
    v_sql:='delete from xxx where '; -- 错误的语句
         
    -- 内部语句块
    begin
        execute immediate v_sql;
    EXCEPTION
      WHEN OTHERS THEN -- 发生其他异常
        -- 执行失败也无妨，不影响后续执行
        -- 在此处打印日志
        SP_LOG('SP_TEST',IN_SUM_DATE,'execute delete error: code='||SQLCODE||', msg='||SQLERRM);
    end;
    
    -- 执行其他后续处理
    
    
    -- 执行成功设置正常执行返回值
    O_CODE:=0;
    O_MSG:='ok';
    
end SP_TEST;
```
- 直接看转换结果
```xml
<procedure id="SP_TEST"
                   O_CODE.int=""
                   O_MSG.string=""
                   IN_SUM_DATE.int="">
    <lang-render result="v_sql" _lang="sql">
        delete from xxx where
    </lang-render> <!-- 错误的语句 -->
    <lang-try>
        <lang-body>

            <sql-update script="v_sql"/>

        </lang-body>
        <lang-catch type="Throwable" e="e"> <!-- 发生其他异常 -->
            <!-- 执行失败也无妨，不影响后续执行 -->
            <!-- 在此处打印日志 -->
            <lang-eval-ts>
                error_msg=${e}.getClass().getName()+':'+${e}.getMessage();
            </lang-eval-ts>
            <procedure-call refid="SP_LOG"
                            IN_PROCEDURE_NAME.string="SP_TEST"
                            IN_SUM_DATE="IN_SUM_DATE"
                            IN_MSG.body-text.trim.render="">
                execute delete error: ${error_msg}
            </procedure-call>
        </lang-catch>
    </lang-try>
    <!-- 执行其他后续处理 -->

    <!-- 执行成功设置正常执行返回值 -->
    <lang-set result="O_CODE" value.int="0"/>
    <lang-set result="O_MSG" value.string="ok"/>
</procedure>
```

#### 无数据返回异常(exception when NO_DATA_FOUND then)
- 这种异常发生在进行查询时
- 查询没有数据，一般来说，这是一种正常情况
- 不属于异常的定义范围
- 因此，转换之后，也不按照异常进行catch处理
- 而是对结果进行判空处理
- 直接看原始语句
- 这个示例比较单件，只是单纯的进行查询
- 查询得到，就直接返回结果
- 查询不到返回NULL
```sql
v_sql:='select * from sys_user where id='||v_user_id;
     
begin
    execute immediate v_sql into v_user;
    O_USER:=V_USER;
exception 
    when NO_DATA_FOUND then
    O_USER:=null;
end;
```
- 下面直接进行转换
- 也就是将NOT_DATA_FOUND处理为判空
```xml
<lang-render result="v_sql" _lang="sql">
    select * from sys_user where id=${v_user_id}
</lang-render>

<sql-query-row script="v_sql" result="v_user" result-type="Map"/>

<lang-choose>
    <lang-when test="v_user==null">
        <lang-set result="O_USER" value.null=""/>
    </lang-when>
    <lang-otherwise>
        <lang-set result="O_USER" value="v_user"/>
    </lang-otherwise>
</lang-choose>
```

## 内建变量
- 这部分主要提供一些调试跟踪的手段或者变量

### XML文件名 trace.location 
- 最后一次访问的XML节点所在文件名
- 这个变量将会在节点执行前进行赋值
- 因此可以在节点执行时，获取到当前节点所在的XML文件名称
- 使用方法例如
```xml
<lang-render result="RUN_INFO">
    current step is 1, at ${trace.location}
</lang-render>
```

### XML标签行号 trace.line
- 最后一次访问的XML节点所在的行号
- 这个变量将会在节点执行前进行赋值
- 因此可以在节点执行时，获取到当前节点所在的行号
- 使用方法例如
```xml
<lang-render result="RUN_INFO">
    current step is 1, at ${trace.line}
</lang-render>
```

### 最后一次异常的错误信息 trace.errmsg
- 能够获取最后一次执行异常时的错误信息
- 一般可用于catch语句块中直接获取错误信息
- 而不在调用异常对象的方法获取异常信息e.getMessage()
- 使用方法例如
```xml
<lang-try>
    <lang-body>
        
    </lang-body>
    <lang-catch>
        <lang-render result="RUN_INFO">
            run error, at ${trace.location}:${trace.line} msg: ${trace.errmsg}
        </lang-render>
    </lang-catch>
</lang-try>
```


### 获取当前节点元数据 trace.node
- 能够获取最后一次执行的XML节点的元数据信息，返回的是XmlNode对象
- 需要注意，请不要轻易的修改对象的值
- 除非你深刻的评估过了
- 一般情况下只建议进行读取操作
- 使用方法例如
```xml
<lang-render result="RUN_INFO">
    current step is 1, on tag: ${trace.node.tagName} , with attrs: ${trace.node.tagAttrMap}
    at ${trace.node.locationFile}:${trace.nodelocationLineNumber}
</lang-render>
```

## 问题解答

### 怎么将主数据切换为其他数据源来执行过程？
- 默认情况下，框架自动检测primary,master,main,default,leader这些数据源作为主数据源primary
- 在多数据源场景中，就是按照这个顺序进行检测的，检测到为止，则不再继续检测
- 要运行过程，如果过程中都没有明确指定数据源
- 则需要具有primary这样一个数据源
- 但是，有的情况下，代码是写在一起，但是实际上却需要再不同的数据源中进行执行
- 例如，某一类存过要在ods层的数据库执行，另一类存过要在ads层的数据库执行
- 这样的情况下，程序如果默认的primary是ods层的数据库
- 则旧导致ads层的存过调用起来比较费劲个，需要手动调整ads作为主数据源primary
- 因此，内部进行了设计上的调整，添加了datasourcesMapping数据源映射的特性
- 定义如下
```java
datasourcesMapping=new HashMap<String,String>();
```
- 键为映射的名称，值为被映射的名称
- 默认情况下，这个映射是空的，也就是不进行任何映射
- 下面直接来看这种场景怎么调用
```java
Map<String, Object> ret = JdbcProcedureHelper.call("SP_ODS_MAIN", (map) -> {
    map.put("O_CODE", "")
            .put("O_MSG", "")
            .put("IN_SUM_MON", "202501")
            .put(ParamsConsts.DATASOURCES_MAPPING, // 添加datasourcesMapping数据源映射
                    new MapBuilder<>(new HashMap<>(),String.class,String.class)
                            .put(ParamsConsts.DEFAULT_DATASOURCE,"ods") // 将默认数据卷primary指向ods数据源
                            .get()
            );
});
```
- 也就是在调用参数上加上datasourcesMapping这个映射Map
- 并且指定了默认数据源指向ods数据源
- 这样，在调用这个存过时，默认数据源就是ods数据源
- 而不再是程序配置中默认的主数据源
- 需要注意的是，这种操作只建议在入口的地方进行设置映射
- 不建议在过程内部也调整映射
- 因为在内部调整之后，可能在子过程或者内部过程调用时，发生事务控制混乱以及主数据源混乱的情况

### 上下文中有哪些固定的参数？
- 内置了一些固定的运行常量
- 这些常量可以根据需要进行使用和调整值
- 这些固定的参数，基本上在发生内部嵌套调用时，保持了这些参数的共享性
- 这些参数始终都进行共享
- 在使用上，应该避免对这些值进行赋值
- 比如，常用的global,trace,executor参数
- 更多详细的固定变量，可以在如下的函数中进行查看
- 未来可能会发生一些变化
```shell
BasicJdbcProcedureExecutor.createParams()
```
- 下面简单介绍一下这些固定变量的设计初衷和目的
- context 上下文，主要用于能够结合其他框架的上下文，以使用一些其他框架特有的能力，默认对接springboot中时为ApplicationContext对象
- env 环境变量，主要用于提供一些环境变量的配置，以便能够进行环境变量的读取操作，默认对接springboot中时为Environment对象
- beans 类型Map<String,Object>用于提供一些具名的对象实例，主要是配合固定的框架使用，能够读取到框架中的bean对象，进行调用bean方法，默认对接springboot中定义的所有bean对象
- datasources 类型Map<String,DataSource>,用于提供可用的数据源，不管是否是什么框架，只要执行SQL都应该具有数据源，至少一个primary数据源，默认对接springboot中时自动读取配置的所有数据源
- datasourcesMapping 类型Map<String,String>,用于提供数据源名称映射功能，在需要使用key的数据源时，替换为使用对应的value指向的数据源，默认是空配置，不做映射
- connections 类型Map<String,Connection>,用于保存过程执行期间的所使用的每个数据源的实际连接，只有在发生对应数据源的SQL执行操作时，即使发生了内部过程嵌套调用，也是用的是同一个连接，除非手动指定使用新连接，才会初始化连接到其中，默认是空的
- global 类型Map<String,Object>,用于保存全局变量，在发生过程内部嵌套调用时，依旧能够保持全局变量的共享和传递，默认是空的
- trace 类型Map<String,Object>,用户保存运行时的跟踪信息，方便进行日志跟踪或者源码位置溯源等操作，配合调试等功能，一般对于用户来说只读即可
- executor 类型JdbcProcedureExecutor,用于提供executor的操作能力，方便在一些场景中，能够获取executor的操作能力
- 关于这些参数是如何在发生内部调用时进行传递的，请看下一节

### 发生内部嵌套调用时上下文是怎么传递的？
- 针对固定参数是使用直接引用赋值的方式进行传递的
- 始终保持引用不变
- 具体可参看如下代码
```java
BasicJdbcProcedureExecutor.newParams()
```

### 是怎么注入了上下文的固定参数的？
- 用户调用古城，一般只需要传递过程声明的形式参数
- 但是固定参数，用户一般都不需要显式的进行设置
- 那这是在什么时候添加的固定参数？
- 具体可查看如下代码
```java
BasicJdbcProcedureExecutor.prepareParams()
```

## 拓展自己的TinyScript自定义函数
- 一些函数是TinyScript内建的函数
- 能够提供基本和数据库内建函数一样的能力
- 但是，也会有一些情况，你想要定义自己的函数
- 可能用户实现数据库的内建函数，可能用户某种场景下的简化操作
- 不管怎样，你都可能有这样的需求
- 那怎么样实现自己的函数，并注册到TinyScript内建函数库呢
- 下面开始教程
- 如果你想要添加自己的内建函数
- 也就是不适用类名.方法名的方式调用
- 直接使用方法名的方式调用
- 那么就需要注册内建函数

### 内建函数的保存
- 内建函数是通过静态变量实现的
- 变量位置如下
```java
public class TinyScript {
    public static final ConcurrentHashMap<String, CopyOnWriteArrayList<IMethod>> BUILTIN_METHOD = new ConcurrentHashMap<>();

}
```
- 并且提供了一系列的registryBuiltinMethod方法用了进行注册函数
```java
public class TinyScript {
    // 将Java静态函数注册为内建函数
    public static void registryBuiltinMethod(Method method) {
        
    }
    // 可以自行封装实现内建函数声明
    // 这种情况下允许没有实际的Java方法定义即可
    // 实际的实现允许是任意的，比如执行脚本等都可以
    public static void registryBuiltinMethod(IMethod method) {
        
    }
    // 将类中的所有静态方法都注册为内建函数
    public static void registryBuiltMethodByStaticMethod(Class<?> clazz) {
        
    }
    // 将类中所有的静态方法都注册为内建函数
    // 允许使用filter来过滤需要注册为内建函数的函数
    public static void registryBuiltMethodByStaticMethod(Class<?> clazz, Predicate<Method> filter) {
        
    }
    // 将实例对象的所有public公开方法（实例函数和静态函数）注册为内建函数，函数调用的对象就是传入的对象
    public static void registryBuiltMethodByInstanceMethod(Object object) {
        
    }
    // 将实例对象的所有public公开方法（实例函数和静态函数）注册为内建函数，函数调用的对象就是传入的对象
    // 允许使用filter来过滤需要注册为内建函数的函数
    public static void registryBuiltMethodByInstanceMethod(Object object, Predicate<Method> filter) {
        
    }
}
```
- 因此，你只需要在使用tiny-script之前，调用以上的注册方法进行注册内建函数即可
- 一般的情况，自己编写一个工具类，编写一些需要注册为内建函数的静态方法
- 例如
```java
public class MySqlFunctions {
    public static Object ifnull(Object val,Object defVal){
        if(val==null){
            return defVal;
        }
        return val;
    }
}
```

- 然后将这个类注册为内建函数

```java
TinyScript.registryBuiltMethodByStaticMethod(MySqlFunctions.class);
```

- 这样就可以在TinyScript脚本中使用这个内建函数了
- 例如
```xml
<lang-eval-ts>
    v_sql=null;
    v_sql=ifnull(${v_sql},"");
</lang-eval-ts>
```

### TinyScript默认内建函数
- 除此之外，TinyScript默认就将一部分Java类函数注册为内建函数了
- 具体可以查看如下的位置
- 静态构造代码块
```java
public class TinyScript {
    static {
        // 静态构造代码块
    }
}
```

### Jdbc-Procedure集成内建函数
- 当然，因为TInyScript被Jdbc-Procedure集成了
- 因此也可以使用集成的方式
- 这种方式，不但TInyScript可以进行使用
- 在lang-invoke中也可以进行使用
- 同样也是通过静态变量维护内建函数的
```java
public class ContextHolder {
    // 用于静态直接根据方法名在这个集合类中查找同名的方法，使用于LangInvokeNode中，方法需要为public的，不限制是否为static的方法
    public static final ConcurrentHashMap<String, List<IMethod>> INVOKE_METHOD_MAP = new ConcurrentHashMap<>();

}
```
- 同样通过静态方法进行注册
```java
public class ContextHolder {
    // 将所有公开方法进行注册
    public static void registryAllInvokeMethods(Class<?>... classes) {

    }
    // 将所有公开方法进行注册
    public static void registryAllInvokeMethods(Method... methods) {
        
    }
}
```

## 拓展自己的XML节点
- 目前内置的节点主要分为两类lang-(逻辑控制类)和sql-(数据库操作类)两类
- 如果有需求增加其他的控制，也可以编写自己的xml-node节点来处理
- 比如，想要实现命令执行的节点

### 定义节点规则（非必要）
- 定义如下
```xml
<!--
执行shell命令
可以使用Script指定使用的脚本变量
如果Script没有指定脚本变量，则默认使用xml内部文本作为执行脚本
await指定是否需要等待命令执行结束
如果指定了await，则可以使用result指定接受命令执行的标准输出结果
-->
<cmd-exec script="" await="" result="">
    
</cmd-exec>
```
- 好了，节点的规范定义好了

### 定义节点DTD语法约束（非必要）
- 那么，就可以添加对应节点定义的DTD约束了
```xml
<!ELEMENT cmd-exec (ANY|EMPTY)>
<!ATTLIST cmd-exec script CDATA #IMPLIED>
<!ATTLIST cmd-exec await CDATA #IMPLIED>
<!ATTLIST cmd-exec result CDATA #IMPLIED>
```
- 将新增加的节点加到合适的DTD节点节点中去
- 一般都是添加到根节点中去即可
```xml
<!ELEMENT procedure (
                debugger*
                |cmd-exec*
        )>
```
- 这样dtd文件规则就编写好了

### 定义节点处理逻辑（必要）
- 接下来，就需要实现自己的节点处理逻辑了
- 首先需要实现ExecutorNode接口
- 或者继承默认的抽象类AbstractExecutorNode
- 一般选择继承抽象类，抽象类中已经提供了一些必要的逻辑
- 因此选择抽象类即可
- 编写自己的实现类
```java
public class CmdExecNode extends AbstractExecutorNode{
    @Override
    public boolean support(XmlNode node) {
        // 判断节点是否支持进行处理
        // 一般就是判断节点名称是否满足
        return false;
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        // 非必要实现的
        // 用于检查节点的语法是否满足
        // 比如检查必要的节点属性是否填写
        // 属性的值是否正常等
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        // 处理你的节点逻辑
    }
    
}
```
- 接着，完成这个类即可
- 下面展示一般的逻辑
- 因为这个节点的定义，是没有子节点需要处理的
- 因此，就不用考虑子节点的事情了
```java
public class CmdExecNode extends AbstractExecutorNode{
    public static final String TAG_NAME="cmd-exec";
    @Override
    public boolean support(XmlNode node) {
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        // 由于本节点，有三个属性，script/await/result
        // 都不是必须得
        // 只需要script属性有值或者body有值即可
        // 所以就检查这个即可
        String script = node.getTagAttrMap().get(AttrConsts.SCRIPT);
        String body = node.getTextBody().trim();
        if((script==null || script.isEmpty())
        &&(body.isEmpty())){
            warnPoster.accept(TAG_NAME+" must have script attribute or not empty body");
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        // 首先进行获取属性值
        String script = (String) executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context);
        Boolean await = (Boolean) executor.attrValue(AttrConsts.AWAIT, FeatureConsts.BOOLEAN, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String body = node.getTextBody().trim();
        if(script==null || script.isEmpty()){
            script=body;
        }
        if(await==null){
            await=false;
        }

        try {
            // 执行命令
            String val=execCmd(script,await);

            // 当有需要结果时，才设定结果
            if(result!=null && !result.isEmpty()){
                // 设置结果
                Object res = executor.resultValue(val, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                executor.visitSet(context, result, res);
            }
        } catch (Exception e) {
            throw new ThrowSignalException(e.getMessage(),e);
        }
    }

    public static String execCmd(String cmd,boolean await) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        if(!await){
            return null;
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        try(InputStream is = process.getInputStream()){
            byte[] buff=new byte[4096];
            int len=0;
            while((len=is.read(buff))>0){
                bos.write(buff,0,len);
            }
        }
        process.waitFor();
        // 这里可能需要根据操作系统的字符集，设置正确的字符集
        return new String(bos.toByteArray());
    }
}
```
- 接下来，就需要将这个处理节点注册到执行环境中了
- 有以下几种方式
- 方式1，直接添加到executor的初始化代码中
```java
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor {
    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        // ... 之前的代码
        ret.add(new TextNode());
        // 添加自己的节点实现
        ret.add(new CmdExecNode());

        return ret;
    }
}
```
- 方式2，在子类实现或者直接修改添加节点
```java
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor {
    protected void addNodes(){
        this.nodes.add(new CmdExecNode());
    }

}
```
- 方式3，通过注册为SPI对象实现
- 这种方式不需要修改源码
- 首先编写SPI配置文件到自己的resources路径中
- 获取到ExecutorNode类的完整类名
- 比如
```shell
xxx.jdbc.procedure.node.ExecutorNode
```
- 然后创建这样的路径
```shell
resources/META-INF/services/
```
- 在这个路径下新建一个和类名一致的文件
- 注意文件没有后缀，就是完整的类名
```shell
xxx.jdbc.procedure.node.ExecutorNode
```
- 完整的文件路径如下
```shell
resources/META-INF/services/xxx.jdbc.procedure.node.ExecutorNode
```
- 编辑这个文件，将需要添加的节点的完整类名都添加到这个文件中
- 每个类名独占一行
- 比如
```shell
com.test.xpro4j.node.CmdExecNode
com.test.xpro4j.node.HttpRequestNode
```
- 这样即可

## 和其他框架集成
- 默认情况下，是和springboot框架进行了继承
- 如果需要和其他框架进行继承
- 则继承DefaultJdbcProcedureExecutor类，覆盖特定框架的特性
- 具体可以参考SpringContextJdbcProcedureExecutor的实现
- 以及SpringContextJdbcProcedureExecutorAutoConfiguration类的实现组合原理
- 这部分比较简单，不再进行展开讲解，根据源码进行查看即可

## 直接独立使用，不与springboot集成
- 这部分，请参考TestProcedureExecutor类以及所在包的测试代码
- 结合SpringContextJdbcProcedureExecutorAutoConfiguration配置
- 即可能够进行直接使用