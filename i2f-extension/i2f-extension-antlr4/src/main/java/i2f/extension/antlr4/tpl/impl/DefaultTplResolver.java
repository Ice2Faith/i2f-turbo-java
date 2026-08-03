package i2f.extension.antlr4.tpl.impl;

import i2f.reflect.vistor.Visitor;
import i2f.convert.obj.ObjectConvertor;
import i2f.extension.antlr4.script.funic.lang.Funic;
import i2f.extension.antlr4.tpl.grammar.TplParser;
import i2f.extension.antlr4.tpl.grammar.TplVisitor;
import i2f.extension.antlr4.tpl.impl.debugger.TplDebugBridgeReporter;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplBreakException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplContinueException;
import i2f.extension.antlr4.tpl.impl.exception.impl.TplEvaluateException;
import i2f.extension.antlr4.tpl.impl.value.ParameterValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/7/31 16:17
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultTplResolver implements TplResolver {
    public static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
    protected final AtomicBoolean debug = new AtomicBoolean(true);

    protected final ConcurrentHashMap<String, TplBlockHandler> blockHandlers = new ConcurrentHashMap<>();

    {
        initBlockHandlers();
    }

    @Override
    public void debug(boolean enable) {
        this.debug.set(enable);
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
        if (debug.get()) {
            System.out.println(String.format("%s [%5s] [%15s] : %s",
                    LOG_TIME_FORMATTER.format(LocalDateTime.now()),
                    "DEBUG",
                    "tpl",
                    String.valueOf(supplier.get())
            ));
        }
    }

    @Override
    public void debugBridge(String fileName, int lineNumber, Supplier<Map<String, Object>> variableMapSupplier) {
        TplDebugBridgeReporter.proxy(fileName, lineNumber, variableMapSupplier);
    }

    @Override
    public void openDebugger(Object context, String tag, String conditionExpression) {
        if (debug.get()) {
            System.out.println("debugger [" + tag + "] [" + conditionExpression + "] wait for input line to continue.");
            System.out.println("continue.");
        }
    }

    @Override
    public Object concat(Object obj, Object append) {
        if (obj == null) {
            return append;
        }
        if (append == null) {
            return null;
        }
        return obj + "" + append;
    }

    protected void initBlockHandlers() {
        blockHandlers.put("sharp", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx != null) {
                throw new TplEvaluateException("sharp block not require body!");
            }
            int count=1;

            if(!parameterList.isEmpty()) {
                ParameterValue parameter = parameterList.get(0);
                Object cnt = parameter(parameter.getExpression(), context, visitor);
                count=(Integer) ObjectConvertor.tryConvertAsType(cnt,Integer.class);
            }

            String ret="";
            for (int i = 0; i < count; i++) {
                ret+="#";
            }
            return ret;
        });

        blockHandlers.put("dollar", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx != null) {
                throw new TplEvaluateException("dollar block not require body!");
            }
            int count=1;

            if(!parameterList.isEmpty()) {
                ParameterValue parameter = parameterList.get(0);
                Object cnt = parameter(parameter.getExpression(), context, visitor);
                count=(Integer) ObjectConvertor.tryConvertAsType(cnt,Integer.class);
            }

            String ret="";
            for (int i = 0; i < count; i++) {
                ret+="$";
            }
            return ret;
        });

        blockHandlers.put("foreach", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx == null) {
                throw new TplEvaluateException("foreach block require body!");
            }
            Object ret = null;
            try {
                ParameterValue iterNameParameter = parameterList.get(0);
                ParameterValue collectionParameter = parameterList.get(1);

                Object iterName = parameter(iterNameParameter.getExpression(), context, visitor);
                Object collection = parameter(collectionParameter.getExpression(), context, visitor);

                String name = iterName == null ? "item" : String.valueOf(iterName);
                if (collection != null) {
                    Object bakItem = valueGet(context, name);
                    try {
                        if (collection instanceof Iterable) {
                            Iterable<?> iterable = (Iterable<?>) collection;
                            for (Object item : iterable) {
                                valueSet(context, name, item);
                                try {
                                    Object nextValue = visitor.visitBlockBody(bodyCtx);
                                    ret = concat(ret, nextValue);
                                } catch (TplContinueException e) {
                                    continue;
                                }
                            }
                        } else if (collection instanceof Iterator) {
                            Iterator<?> iterator = (Iterator<?>) collection;
                            while (iterator.hasNext()) {
                                Object item = iterator.next();
                                valueSet(context, name, item);
                                try {
                                    Object nextValue = visitor.visitBlockBody(bodyCtx);
                                    ret = concat(ret, nextValue);
                                } catch (TplContinueException e) {
                                    continue;
                                }
                            }
                        } else if (collection.getClass().isArray()) {
                            int len = Array.getLength(collection);
                            int i = 0;
                            while (i < len) {
                                Object item = Array.get(collection, i++);
                                valueSet(context, name, item);
                                try {
                                    Object nextValue = visitor.visitBlockBody(bodyCtx);
                                    ret = concat(ret, nextValue);
                                } catch (TplContinueException e) {
                                    continue;
                                }
                            }
                        } else {
                            throw new TplEvaluateException("foreach block require iterable object, but found type: " + collection.getClass());
                        }
                    } finally {
                        valueSet(context, name, bakItem);
                    }
                }
            } catch (TplBreakException e) {

            }
            return ret;
        });

        blockHandlers.put("for", (parameterList, bodyCtx, context, visitor) ->{
            if (bodyCtx == null) {
                throw new TplEvaluateException("for block require body!");
            }
            ParameterValue beginParameter = parameterList.get(0);
            ParameterValue condParameter = parameterList.get(1);
            ParameterValue incrParameter = parameterList.get(2);
            Object begin = parameter(beginParameter.getExpression(), context, visitor);

            Object ret=null;
            while(true){
                Object cond = parameter(condParameter.getExpression(), context, visitor);
                if(!toBoolean(cond)){
                    break;
                }
                Object next = visitor.visitBlockBody(bodyCtx);
                ret=concat(ret,next);

                Object incr=parameter(incrParameter.getExpression(), context,visitor);
            }

            return ret;
        });

        blockHandlers.put("while", (parameterList, bodyCtx, context, visitor) ->{
            if (bodyCtx == null) {
                throw new TplEvaluateException("while block require body!");
            }

            ParameterValue condParameter = parameterList.get(0);

            Object ret=null;
            while(true){
                Object cond = parameter(condParameter.getExpression(), context, visitor);
                if(!toBoolean(cond)){
                    break;
                }
                Object next = visitor.visitBlockBody(bodyCtx);
                ret=concat(ret,next);

            }

            return ret;
        });

        blockHandlers.put("bind", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx != null) {
                throw new TplEvaluateException("set block not require body!");
            }
            ParameterValue nameObjParameter = parameterList.get(0);
            ParameterValue valueParameter = parameterList.get(1);
            Object nameObj = parameter(nameObjParameter.getExpression(), context, visitor);
            Object value = parameter(valueParameter.getExpression(), context, visitor);
            valueSet(context, nameObj == null ? null : String.valueOf(nameObj), value);
            return null;
        });

        blockHandlers.put("set", blockHandlers.get("bind"));

        /**
         * trim 语句，因为参数较多，且都是可选的，因此使用命名参数指定
         * prefixOverrides,suffixOverrides 为去除的前置或后置内容正则表达式
         * prefix,suffix 为去除完，若内容不为空，则添加的前后缀
         * #trim(prefix=${" where "},suffix=null,prefixOverrides=${"and|or"},suffixOverrides=null)
         *
         * #end
         */
        blockHandlers.put("trim", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx == null) {
                throw new TplEvaluateException("trim block require body!");
            }
            ParameterValue prefixParameter=null;
            ParameterValue suffixParameter=null;
            ParameterValue prefixOverridesParameter=null;
            ParameterValue suffixOverridesParameter=null;
            for (ParameterValue value : parameterList) {
                if("prefix".equals(value.getName())){
                    prefixParameter=value;
                }
                if("suffix".equals(value.getName())){
                    suffixParameter=value;
                }
                if("prefixOverrides".equals(value.getName())){
                    prefixOverridesParameter=value;
                }
                if("suffixOverrides".equals(value.getName())){
                    suffixOverridesParameter=value;
                }
            }

            Object ret = visitor.visitBlockBody(bodyCtx);
            if (ret != null) {
                String text = String.valueOf(ret);
                String trim = text.trim();

                if(prefixOverridesParameter!=null){
                    Object prefixOverrides = parameter(prefixOverridesParameter.getExpression(), context, visitor);
                    if(prefixOverrides!=null) {
                        trim = trim.replaceFirst("(?i)^" +prefixOverrides,"");
                    }
                }

                if(suffixOverridesParameter!=null){
                    Object suffixOverrides = parameter(suffixOverridesParameter.getExpression(), context, visitor);
                    if(suffixOverrides!=null) {
                        trim = trim.replaceFirst("(?i)" +suffixOverrides+"$","");
                    }
                }

                if(!trim.isEmpty()){
                    if(prefixParameter!=null){
                        Object prefix = parameter(prefixParameter.getExpression(), context, visitor);
                        if(prefix!=null){
                            trim=prefix+trim;
                        }
                    }
                    if(suffixParameter!=null){
                        Object suffix = parameter(suffixParameter.getExpression(), context, visitor);
                        if(suffix!=null){
                            trim=trim+suffix;
                        }
                    }
                }
                return trim;
            }
            return null;
        });

        blockHandlers.put("break", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx != null) {
                throw new TplEvaluateException("break block not require body!");
            }
            throw new TplBreakException();
        });

        blockHandlers.put("continue", (parameterList, bodyCtx, context, visitor) -> {
            if (bodyCtx != null) {
                throw new TplEvaluateException("continue block not require body!");
            }
            throw new TplContinueException();
        });

    }

    @Override
    public Object block(String blockName, List<ParameterValue> parameterList, TplParser.BlockBodyContext bodyCtx, Object context, TplVisitor<Object> visitor) {
        TplBlockHandler handler = blockHandlers.get(blockName);
        if (handler != null) {
            return handler.block(parameterList, bodyCtx, context, visitor);
        }
        throw new TplEvaluateException("un-support block type [" + blockName + "]");
    }

    @Override
    public Object parameter(String expression, Object context, TplVisitor<Object> visitor) {
        if (expression.startsWith("$")
                || expression.startsWith("#")) {
            return value(true, expression, context, visitor);
        } else {
            return eval(expression, context, visitor);
        }
    }

    @Override
    public Object value(String expression, Object context, TplVisitor<Object> visitor) {
        return value(false, expression, context, visitor);
    }

    public Object value(boolean isParameter, String expression, Object context, TplVisitor<Object> visitor) {
        boolean isDollar = false;
        boolean isNull2Empty = false;

        int idx = expression.indexOf("{");
        String expr = expression.substring(idx + 1, expression.length() - 1);
        String prefix = expression.substring(0, idx);
        if (prefix.startsWith("$")) {
            isDollar = true;
        }
        if (prefix.contains("!")) {
            isNull2Empty = true;
        }

        Object val = eval(expr, context, visitor);

        if (isNull2Empty && val == null) {
            val = "";
        }

        if (isParameter) {
            return val;
        }

        return postProcessValue(val, isDollar, expression, context, visitor);
    }

    protected Object postProcessValue(Object ret, boolean isDollar, String expression, Object context, TplVisitor<Object> visitor) {
        return ret;
    }

    public String unescape(String text) {
        if (text == null) {
            return null;
        }
        text = text.replace("\\r", "\r");
        text = text.replace("\\n", "\n");
        text = text.replace("\\t", "\t");
        text = text.replace("\\b", "\b");
        text = text.replace("\\'", "'");
        text = text.replace("\\\"", "\"");
        text = text.replace("\\$", "$");
        text = text.replace("\\#", "#");
        text = text.replace("\\{", "{");
        text = text.replace("\\}", "}");
        text = text.replace("\\\\", "\\");
        return text;
    }

    @Override
    public boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() > 0;
        }
        if (value instanceof Map) {
            return !((Map) value).isEmpty();
        }
        if (value instanceof Collection) {
            return !((Collection) value).isEmpty();
        }
        return true;
    }

    public void valueSet(Object context, String key, Object value) {
        Visitor.visit(key, context).set(value);
    }

    public Object valueGet(Object context, String key) {
        return Visitor.visit(key, context).get();
    }

    public Object eval(String expression, Object context, TplVisitor<Object> visitor) {
        if (expression == null) {
            return null;
        }
        expression = expression.trim();
        if ("null".equals(expression)) {
            return null;
        } else if ("true".equals(expression)) {
            return true;
        } else if ("false".equals(expression)) {
            return false;
        } else {
            try {
                return Integer.parseInt(expression);
            } catch (Exception e) {
            }
            try {
                return Double.parseDouble(expression);
            } catch (Exception e) {
            }
            try {
                return new BigInteger(expression);
            } catch (Exception e) {
            }
            try {
                return new BigDecimal(expression);
            } catch (Exception e) {
            }
            // 字符串字面值
            boolean isRawText = false;
            if ((expression.startsWith("'") && expression.endsWith("'"))
                    || (expression.startsWith("\"") && expression.endsWith("\""))) {
                expression = expression.substring(1, expression.length() - 1);
                isRawText = true;
            }

            expression = unescape(expression);

            if (isRawText) {
                return expression;
            }

            Object ret = Funic.script(expression, context);
            return ret;
        }
    }
}
