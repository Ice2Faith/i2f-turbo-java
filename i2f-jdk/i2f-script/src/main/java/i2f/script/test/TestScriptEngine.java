package i2f.script.test;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author Ice2Faith
 * @date 2024/8/4 13:56
 * @desc
 */
public class TestScriptEngine {
    public static void main(String[] args) throws Exception {
        // 创建 ScriptEngineManager 对象
        ScriptEngineManager manager = new ScriptEngineManager();

        // 获取 JavaScript 引擎
        ScriptEngine engine = manager.getEngineByName("JavaScript");//ECMAScript

        // 执行脚本语句
        engine.eval("print('Hello, World!')");

        if (engine instanceof Invocable) {
            engine.eval("function add(a,b){return a+b;}");
            Invocable invocable = (Invocable) engine;
            Object ret = invocable.invokeFunction("add", 1, 2);
            System.out.println(ret);

            Object ivk = engine.eval("var obj={sub:function(a,b){return a-b;}};function objSub(a,b){return obj.sub(a,b);};");

            Object obj = invocable.invokeFunction("objSub", 2, 1);
            System.out.println(obj);
        }

        if (engine instanceof Compilable) {
            Compilable compilable = (Compilable) engine;
            compilable.compile("");
        }

        // 传递参数给脚本
        String name = "John";
        engine.put("name", name);
        engine.eval("var upperCaseName = name.toUpperCase();");

        // 从脚本获取返回值
        engine.eval("var number = 123; number;");
        Object result = engine.get("number");
        System.out.println(result);

    }
}
