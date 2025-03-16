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