package i2f.reflect.vistor.impl;

import i2f.lru.LruMap;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;


/**
 * @author Ice2Faith
 * @date 2024/3/1 8:54
 * @desc 一个支持上下文参数的简易表达式访问器
 * 目的：
 * 提供一个类似js的访问方式，访问多层级嵌套的对象
 * 例如：
 * $root.user.name
 * $root.user.roles[0].name
 * user.name
 * user.roles.@toString()
 * 定义：
 * 需要三个基本量
 * expression 表达式
 * rootObj 根元素，也就是表达式中的值，默认从根元素开始查找
 * paramObj 根参数，也就是表达式中，可能会使用到其他的参数，则以此为根元素开始查找
 * 每个层级使用.英文句号分隔
 * 也就是访问此对象的方法或者属性
 * 比如：user.name
 * ：user.age
 * 这些表达式的值，将会从根元素开始查找
 * 最终结果分别对应：rootObj.user.name ; rootObj.user.age
 * 使用[]包含一个索引或者键
 * 比如：[0]
 * ：[name]
 * 适用于Object访问，Array访问，List访问，Map访问
 * 对于Map访问时，就是键
 * 使用#{}包含一个根参数的值
 * 比如：#{gender}
 * ：#{cond.minAge}
 * 这些表达式的值，将从根参数开始查找
 * 最终结果分别对应：paramObj.gender;paramObj.cond.minAge
 * 使用@引导一个符号
 * 比如：@isEmpty()
 * ：@age
 * ：@Math.PI
 * ：@String.valueOf()
 * ：@String.valueOf(user.age)
 * ：@isValid(user.age,#{cond.minAge})
 * ：@java.util.Date.from()
 * 也就是@之后跟一个方法isEmpty()、一个属性age、一个全限定方法String.valueOf()、一个静态变量Math.PI都是可以的
 * 而且，针对常用的Java包的类可以不用指定全限定名称，例如：String.valueOf()就不用写成java.lang.String.valueOf()
 * 怎么区别方法和属性？通过是否带有参数括号进行区分，@Math.PI不带括号，认为是属性，@String.valueOf()带括号，认为是方法
 * 同时方法支持传入参数
 * 例如：@String.valueOf(user.age) 就传入了一个参数，这个参数user.name就是从根元素中获取的
 * ：@isValid(user.age,#{cond.minAge}) 就传入了两个参数，第二个参数则是从参数中获取的
 * 其他的一些特殊常量
 * $true 就是 true
 * $false 就是 false
 * $root 就是入参 rootObj
 * $param 就是入参 paramObj
 * $node 就是当前节点的值
 * 例如表达式： user.role.@addKey($node.@getKey()).size()
 * 当识别到@addKey函数调用时，此时的$node的值就是user.role
 * 因此可以理解为调用者的调用方
 * 数值组
 * $引导 $123 就是 Integer 类型 123
 * $l引导 $l123 就是 Long 类型 123
 * $12.125 就是 Double 类型 12.125
 * $f引导 $f12.125 就是 Float 类型 12.125
 * $0x引导 $0x123 就是 Integer 的 16 进制 123
 * $0b引导 $0b101 就是 Integer 的 2 进制 101
 * $0引导(0开头) $0123 就是 Integer 的 8 进制 123
 * $l 0x 引导 $l0x123 就是 Long 的 16 进制 123
 * $l 0b 引导 $l0b101 就是 Long 的 2 进制 101
 * $l 0(0开头) 引导 $l0123 就是 Long 的 8 进制 123
 * 知道这些后，下面给出几个示例
 * <p>
 * ---------------------------------
 * paramObj={
 * name: '超级管理员',
 * perm: [{
 * perms: ['add','delete'],
 * get(index){
 * return this.perms[index]
 * }
 * }],
 * category:{
 * charIdx: 0
 * },
 * requires:{
 * permIdx: 1
 * }
 * }
 * <p>
 * rootObj={
 * user:{
 * roles: [{
 * name: '超级管理员',
 * key: 'root',
 * perms: ['add','delete','edit'],
 * getKeys(name,perm){
 * if(name!=this.name){
 * return ['view']
 * }
 * if(!this.perms.contains(perm)){
 * return ['view']
 * }
 * return [perm,'view']
 * }
 * },{
 * name: '管理员',
 * key: 'admin',
 * perms: ['add','edit']
 * }]
 * }
 * }
 * <p>
 * expression= user.roles[0].@getKeys(#{name},#{age}[0].@get(#{current.key}))[0].@charAt(#{category.charIdx})
 * ret= 'd'
 * <p>
 * expression= user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@isEmpty()
 * ret= false
 * <p>
 * expression= @String.valueOf($23.565)
 * ret= "23.565"
 * <p>
 * expression= @Math.PI
 * ret= 3.14159...
 */
public class VisitorParser {
    public static final String PROPERTY_CACHE_SPLIT_TOKENS_SIZE="lru.visitor.parser.cache-split-tokens-size";
    protected static final LruMap<String, List<String>> CACHE_SPLIT_TOKENS = new LruMap<>(LruMap.getSysPropMaxSize(PROPERTY_CACHE_SPLIT_TOKENS_SIZE,4096));
    public static final String PROPERTY_CACHE_SPLIT_PARAMETERS_SIZE="lru.visitor.parser.cache-split-parameters-size";
    protected static final LruMap<String, List<String>> CACHE_SPLIT_PARAMETERS = new LruMap<>(LruMap.getSysPropMaxSize(PROPERTY_CACHE_SPLIT_PARAMETERS_SIZE,4096));
    public static final String PROPERTY_CACHE_PATTERN_SIZE="lru.visitor.parser.cache-pattern-size";
    protected static final LruMap<String, Pattern> CACHE_PATTERN = new LruMap<>(LruMap.getSysPropMaxSize(PROPERTY_CACHE_PATTERN_SIZE,1024));

    public static Pattern getPattern(String regex) {
        return CACHE_PATTERN.computeIfAbsent(regex, k -> Pattern.compile(k));
    }

    public static boolean matches(String str, String regex) {
        return getPattern(regex).matcher(str).matches();
    }

    public static Visitor visit(String expression, Object rootObj) {
        return visit(expression, rootObj, rootObj, rootObj);
    }

    public static Visitor visit(String expression, Object rootObj, Object paramObj) {
        return visit(expression, rootObj, rootObj, paramObj);
    }

    private static Visitor visit(String expression, Object rootObj, Object nodeObj, Object paramObj) {
        if (expression != null) {
            expression = expression.trim();
        }
        if (expression == null || expression.isEmpty()) {
            return new ConstVisitor(null);
        }
        if (nodeObj instanceof Map) {
            Map map = (Map) nodeObj;
            try {
                if (map.containsKey(expression)) {
                    return new MapVisitor(map, expression);
                }
            } catch (Exception e) {

            }
        }
        Visitor ret = new ReadonlyVisitor(nodeObj, nodeObj);
        List<String> tokens = splitTokens(expression);

        for (String token : tokens) {
            // 优先处理Map存在键的情况，因为visitor的目的主要就是处理多层或者直接使用Map的情况
            if (ret != null) {
                Object currRet = ret.get();
                if (currRet instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) currRet;
                    Function<String,Object> keyConvertor=foundMapKeyConvertor(map);
                    Object realKey=token;
                    if(keyConvertor!=null){
                        try {
                            realKey=keyConvertor.apply(token);
                        } catch (Exception e) {

                        }
                    }
                    if(map.containsKey(realKey)){
                        ret=new MapVisitor(map,realKey);
                        continue;
                    }
                }
            }
            // 处理内建常量
            if (token.startsWith("__")) {
                boolean resolved = true;
                if ("__root".equals(token)) {
                    ret = new ConstVisitor(rootObj);
                } else if ("__param".equals(token)) {
                    ret = new ConstVisitor(paramObj);
                } else if ("__node".equals(token)) {
                    ret = new ConstVisitor(nodeObj);
                } else {
                    resolved = false;
                }
                if (resolved) {
                    continue;
                }
            }
            if (token.startsWith("$")) {
                boolean resolved = true;
                if ("$root".equals(token) || "__root".equals(token)) {
                    ret = new ConstVisitor(rootObj);
                } else if ("$param".equals(token) || "__param".equals(token)) {
                    ret = new ConstVisitor(paramObj);
                } else if ("$node".equals(token) || "__node".equals(token)) {
                    ret = new ConstVisitor(nodeObj);
                } else if ("$true".equals(token)) {
                    ret = new ConstVisitor(true);
                } else if ("$false".equals(token)) {
                    ret = new ConstVisitor(false);
                } else if ("$null".equals(token)) {
                    ret = new ConstVisitor(null);
                } else if ("$length".equals(token) && ret != null && ReflectResolver.isArray(ret.get())) {
                    ret = new ConstVisitor(Array.getLength(ret.get()));
                } else if ("$length".equals(token) && ret != null && (ret.get() instanceof CharSequence)) {
                    ret = new ConstVisitor(((CharSequence) (ret.get())).length());
                } else if ("$size".equals(token) && ret != null && (ret.get() instanceof Map)) {
                    ret = new ConstVisitor(((Map) (ret.get())).size());
                } else if ("$size".equals(token) && ret != null && (ret.get() instanceof Collection)) {
                    ret = new ConstVisitor(((Collection) (ret.get())).size());
                } else if (matches(token, "\\$\\d+\\.\\d+")) {
                    ret = new ConstVisitor(Double.parseDouble(token.substring(1)));
                } else if (token.startsWith("$f")
                        && matches(token, "\\$f\\d+\\.\\d+")) {
                    ret = new ConstVisitor(Float.parseFloat(token.substring(2)));
                } else if ((token.startsWith("$l0x") || token.startsWith("$l0X"))
                        && matches(token, "\\$l(0x|0X)[a-fA-F0-9]+")) {
                    ret = new ConstVisitor(Long.parseLong(token.substring(4), 16));
                } else if ((token.startsWith("$l0b") || token.startsWith("$l0B"))
                        && matches(token, "\\$l(0b|0B)[0-1]+")) {
                    ret = new ConstVisitor(Long.parseLong(token.substring(4), 2));
                } else if (token.startsWith("$l0")
                        && matches(token, "\\$l0([0-9]+)?")) {
                    ret = new ConstVisitor(Long.parseLong(token.substring(3), 8));
                } else if (token.startsWith("$l")
                        && matches(token, "\\$l[1-9]([0-9]+)?")) {
                    ret = new ConstVisitor(Long.parseLong(token.substring(2), 10));
                } else if (matches(token, "\\$[1-9]([0-9]+)?")) {
                    ret = new ConstVisitor(Integer.parseInt(token.substring(1), 10));
                } else if ((token.startsWith("$0x") || token.startsWith("$0X"))
                        && matches(token, "\\$(0x|0X)[a-fA-F0-9]+")) {
                    ret = new ConstVisitor(Integer.parseInt(token.substring(3), 16));
                } else if ((token.startsWith("$0b") || token.startsWith("$0B"))
                        && matches(token, "\\$(0b|0B)[0-1]+")) {
                    ret = new ConstVisitor(Integer.parseInt(token.substring(3), 2));
                } else if (token.startsWith("$0")
                        && matches(token, "\\$0([0-9]+)?")) {
                    ret = new ConstVisitor(Integer.parseInt(token.substring(2), 8));
                } else {
                    resolved = false;
                }
                if (resolved) {
                    continue;
                }
            }
            if (token.startsWith("#")) {
                String nextExpression = token.substring(2, token.length() - 1);
                ret = visit(nextExpression, rootObj, paramObj, paramObj);
            } else if (token.startsWith("[")) {
                Object currRet = ret.get();
                if (currRet == null) {
                    return null;
                }
                String nextExpression = token.substring(1, token.length() - 1);
                ret = visit(nextExpression, rootObj, currRet, paramObj);
            } else if (token.startsWith("@")) {
                String nextExpression = token.substring(1);
                String methodName = nextFullNaming(nextExpression, new AtomicInteger(0));
                String paramExpression = token.substring(1 + methodName.length());
                Class<?> staticClass = null;

                int dotIdx = methodName.lastIndexOf(".");
                if (dotIdx >= 0) {
                    String className = methodName.substring(0, dotIdx);
                    methodName = methodName.substring(dotIdx + 1);
                    staticClass = ReflectResolver.loadClassWithJdk(className);
                }

                if (!paramExpression.isEmpty()) {
                    paramExpression = paramExpression.substring(1, paramExpression.length() - 1);
                    List<String> parameters = splitParameters(paramExpression);
                    Object[] args = new Object[parameters.size()];
                    int i = 0;
                    for (String parameter : parameters) {
                        if (parameter.startsWith("#{")) {
                            args[i] = visit(parameter, rootObj, paramObj, paramObj).get();
                        } else {
                            Object currRet = ret.get();
                            if (currRet == null) {
                                return null;
                            }
                            args[i] = visit(parameter, rootObj, currRet, paramObj).get();
                        }
                        i++;
                    }

                    if (staticClass == null) {
                        Object currRet = ret.get();
                        if (currRet == null) {
                            return null;
                        }
                        try {

                            Object val = ReflectResolver.invokeMethod(currRet, methodName, args);
                            ret = new ReadonlyVisitor(val, currRet);
                        } catch (Throwable e) {
                            throw new IllegalStateException("invoke method [" + methodName + "] error in class [" + currRet.getClass().getName() + "] at token : " + token, e);
                        }
                    } else {
                        try {
                            Object val = ReflectResolver.invokeStaticMethod(staticClass, methodName, args);
                            ret = new ReadonlyVisitor(val, staticClass);
                        } catch (Throwable e) {
                            throw new IllegalStateException("invoke static method [" + methodName + "] error in class [" + staticClass + "] at token : " + token, e);
                        }
                    }
                } else {
                    if (staticClass == null) {
                        Object currRet = ret.get();
                        if (currRet == null) {
                            return null;
                        }
                        ret = new FieldVisitor(currRet, methodName);
                    } else {
                        ret = new StaticFieldVisitor(staticClass, methodName);
                    }
                }

            } else {
                Object currRet = ret.get();
                if (currRet == null) {
                    return null;
                }
                if (currRet instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) currRet;
                    Function<String,Object> keyConvertor=foundMapKeyConvertor(map);
                    Object realKey=token;
                    if(keyConvertor!=null){
                        try {
                            realKey=keyConvertor.apply(token);
                        } catch (Exception e) {

                        }
                    }
                    ret = new MapVisitor((Map) currRet, realKey);
                } else if (ReflectResolver.isArray(currRet)
                        && "length".equals(token)) {
                    int len = Array.getLength(currRet);
                    return new ConstVisitor(len);
                } else if (ReflectResolver.isArray(currRet)) {
                    Integer idx = Integer.valueOf(token);
                    return new ArrayVisitor(currRet, idx);
                } else if (currRet instanceof List) {
                    Integer idx = Integer.valueOf(token);
                    return new ListVisitor((List) currRet, idx);
                } else if (currRet instanceof Iterable) {
                    Iterable col = (Iterable) ret;
                    Iterator iterator = col.iterator();
                    Integer idx = Integer.valueOf(token);
                    int i = 0;
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        if (idx == i) {
                            ret = new ReadonlyVisitor(next, col);
                            break;
                        }
                        i++;
                    }
                } else {
                    try {
                        ReflectResolver.valueGet(currRet, token);
                        ret = new FieldVisitor(currRet, token);
                    } catch (Throwable e) {
                        throw new IllegalStateException("bean field [" + token + "] in class [" + ret.getClass().getName() + "] get value error", e);
                    }
                }
            }

        }

        return ret;
    }

    public static Function<String,Object> foundMapKeyConvertor(Map map){
        if(map==null){
            return null;
        }
        Function<String,Object> keyConvertor=null;
        // 尝试推断Map的Key类型
        for (Object key : map.keySet()) {
            if(key==null){
                continue;
            }
            try {
                if (key instanceof CharSequence) {
                    keyConvertor = String::valueOf;
                    break;
                }
            } catch (Exception e) {

            }
            try {
                if (key instanceof Integer) {
                    keyConvertor = Integer::parseInt;
                    break;
                }
            } catch (Exception e) {

            }
            try {
                if (key instanceof Long) {
                    keyConvertor = Long::parseLong;
                    break;
                }
            } catch (Exception e) {

            }
            try {
                if (key instanceof Short) {
                    keyConvertor = Short::parseShort;
                    break;
                }
            } catch (Exception e) {

            }
            try {
                if (key instanceof Float) {
                    keyConvertor = Float::parseFloat;
                    break;
                }
            } catch (Exception e) {

            }
            try {
                if (key instanceof Double) {
                    keyConvertor = Double::parseDouble;
                    break;
                }
            } catch (Exception e) {

            }
            break;
        }
        return keyConvertor;
    }

    public static List<String> splitParameters(String expression) {
        if (expression == null) {
            return null;
        }
        // 纯token字符的输入不含参数分隔符，结果必然为自身单个参数，无需进入缓存
        if (isPlainToken(expression)) {
            List<String> ret = new ArrayList<>(1);
            ret.add(expression);
            return ret;
        }
        List<String> ret = CACHE_SPLIT_PARAMETERS.computeIfAbsent(expression, VisitorParser::splitParameters0);
        return new ArrayList<>(ret);
    }

    public static List<String> splitParameters0(String expression) {
        // 局部int索引+StringBuilder累积，避免逐字符原子操作与字符串反复拼接
        StringBuilder curr = new StringBuilder();
        List<String> tokens = new ArrayList<>();
        int length = expression.length();
        // 只以括号层级判定顶层逗号，括号内的逗号属于内层调用的参数分隔符，必须原样保留
        int depth = 0;
        for (int index = 0; index < length; index++) {
            char ch = expression.charAt(index);
            if (ch == '(') {
                curr.append(ch);
                depth++;
            } else if (ch == ')') {
                if (depth == 0) {
                    throw new EmptyStackException();
                }
                curr.append(ch);
                depth--;
            } else if (ch == ',') {
                if (depth == 0) {
                    tokens.add(curr.toString());
                    curr.setLength(0);
                } else {
                    curr.append(ch);
                }
            } else {
                curr.append(ch);
            }
        }

        if (curr.length() > 0) {
            tokens.add(curr.toString());
        }

        return tokens;
    }

    /**
     * 是否是token内的合法字符，切分逻辑与此判定共用同一份字符集
     */
    public static boolean isTokenChar(char ch) {
        return (ch >= 'a' && ch <= 'z')
                || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9')
                || ch == '_'
                || ch == '$';
    }

    /**
     * 是否是一个纯token字符组成的输入，不含[、@、#、.等任何切分引导符
     */
    public static boolean isPlainToken(CharSequence expression) {
        if (expression == null) {
            return false;
        }
        int len = expression.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!isTokenChar(expression.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<String> splitTokens(String expression) {
        if (expression == null) {
            return null;
        }
        // 纯token字符的输入必然切分为自身单个token，结果可平凡推导，无需进入缓存
        if (isPlainToken(expression)) {
            List<String> ret = new ArrayList<>(1);
            ret.add(expression);
            return ret;
        }
        List<String> ret = CACHE_SPLIT_TOKENS.computeIfAbsent(expression, VisitorParser::splitTokens0);
        return new ArrayList<>(ret);
    }

    public static List<String> splitTokens0(String expression) {
        List<String> tokens = new ArrayList<>();
        // 局部int索引+StringBuilder累积，避免逐字符原子操作与字符串反复拼接
        StringBuilder curr = new StringBuilder();
        int length = expression.length();
        int index = 0;
        while (index < length) {
            char ch = expression.charAt(index);
            if (isTokenChar(ch)) {
                curr.append(ch);
                index++;
            } else if (ch == '[') {
                AtomicInteger nextIndex = new AtomicInteger(index);
                String str = nextEnclose(expression, nextIndex, '[', ']');
                if (str != null && !str.isEmpty()) {
                    if (curr.length() > 0) {
                        tokens.add(curr.toString());
                        curr.setLength(0);
                    }

                    curr.append(str);
                    index = nextIndex.get();
                } else {
                    throw new IllegalStateException("enclose [" + ch + "] not compared at index is " + index);
                }
            } else if (ch == '@') {
                AtomicInteger nextIndex = new AtomicInteger(index + 1);
                String str = nextFullNaming(expression, nextIndex);
                if (str != null && !str.isEmpty()) {
                    curr.append('@');
                    curr.append(str);
                    index = nextIndex.get();
                    if (index >= length) {
                        continue;
                    }
                    ch = expression.charAt(index);
                    if (ch == '(') {
                        nextIndex = new AtomicInteger(index);
                        str = nextEnclose(expression, nextIndex, '(', ')');
                        if (str != null && !str.isEmpty()) {
                            curr.append(str);
                            tokens.add(curr.toString());
                            curr.setLength(0);
                            index = nextIndex.get();
                        } else {
                            throw new IllegalStateException("enclose [" + ch + "] not compared at index is " + index);
                        }
                    } else {
                        if (curr.length() > 0) {
                            tokens.add(curr.toString());
                            curr.setLength(0);
                        }
                    }
                } else {
                    throw new IllegalStateException("method naming [" + ch + "] not compared at index is " + index);
                }
            } else if (ch == '#') {
                index++;
                ch = expression.charAt(index);
                if (ch == '{') {
                    curr.append('#');
                    AtomicInteger nextIndex = new AtomicInteger(index);
                    String str = nextEnclose(expression, nextIndex, '{', '}');
                    if (str != null && !str.isEmpty()) {
                        curr.append(str);
                        tokens.add(curr.toString());
                        curr.setLength(0);
                        index = nextIndex.get();
                    } else {
                        throw new IllegalStateException("enclose [" + ch + "] not compared at index is " + index);
                    }
                } else {
                    throw new IllegalStateException("context argument enclose [" + ch + "] not compared at index is " + index);
                }
            } else if (ch == '.') {
                String tmp = expression.substring(index - 1, index + 2);
                if (matches(tmp, "\\d\\.\\d") && curr.indexOf(".") < 0) {
                    curr.append('.');
                } else {
                    if (curr.length() > 0) {
                        tokens.add(curr.toString());
                        curr.setLength(0);
                    }
                }
                index++;
            } else {
                throw new IllegalStateException("char [" + ch + "] not recognized at index is " + index);
            }
        }
        if (curr.length() > 0) {
            tokens.add(curr.toString());
        }
        return tokens;
    }

    public static String nextFullNaming(String expression, AtomicInteger index) {
        // 局部int索引+StringBuilder累积，避免逐字符原子操作与字符串反复拼接
        StringBuilder ret = new StringBuilder();
        int length = expression.length();
        int pos = scanNaming(expression, index.get(), ret);
        if (ret.length() > 0) {
            while (pos < length && expression.charAt(pos) == '.') {
                int sepLen = ret.length();
                pos++;
                ret.append('.');
                pos = scanNaming(expression, pos, ret);
                if (ret.length() == sepLen + 1) {
                    // 段为空，撤销分隔符（既有行为：游标已越过小数点，且不再继续往后拼接）
                    ret.setLength(sepLen);
                    break;
                }
            }
        }

        index.set(pos);

        return ret.toString();
    }

    /**
     * 从start开始扫描一段命名并追加到out，返回扫描结束后的位置
     * 注：数字仅当段首时终止扫描，非段首的数字被跳过且不追加（延续既有行为）
     */
    protected static int scanNaming(String expression, int start, StringBuilder out) {
        int index = start;
        int length = expression.length();
        int base = out.length();
        while (index < length) {
            char ch = expression.charAt(index);
            if (ch >= '0' && ch <= '9') {
                if (out.length() == base) {
                    return index;
                }
            } else if (isTokenChar(ch)) {
                out.append(ch);
            } else {
                return index;
            }
            index++;
        }
        return index;
    }

    public static String nextNaming(String expression, AtomicInteger index) {
        StringBuilder ret = new StringBuilder();
        index.set(scanNaming(expression, index.get(), ret));
        return ret.toString();
    }

    public static String nextEnclose(String expression, AtomicInteger index, char left, char right) {
        // 只关心嵌套深度，用局部int计数代替Stack；局部int索引+StringBuilder累积
        StringBuilder ret = new StringBuilder();
        int length = expression.length();
        int depth = 0;
        int pos = index.get();
        while (pos < length) {
            char ch = expression.charAt(pos);
            if (ch == left) {
                depth++;
            } else if (ch == right) {
                if (depth == 0) {
                    index.set(pos);
                    return "";
                }
                depth--;
                if (depth == 0) {
                    ret.append(ch);
                    index.set(pos + 1);
                    return ret.toString();
                }
            }
            ret.append(ch);
            pos++;
        }
        index.set(pos);
        return "";
    }

}
