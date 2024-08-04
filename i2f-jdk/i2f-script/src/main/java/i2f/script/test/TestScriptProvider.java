package i2f.script.test;

import i2f.script.ScriptProvider;

/**
 * @author Ice2Faith
 * @date 2024/8/4 13:55
 * @desc
 */
public class TestScriptProvider {
    public static void main(String[] args) throws Exception {
        ScriptProvider engine = ScriptProvider.getJavaScriptInstance();
        // 执行脚本语句
        engine.eval("print('Hello, World!')");

        engine.eval("function add(a,b){return a+b;}");
        Object ret = engine.invokeFunction("add", 1, 2);
        System.out.println(ret);

        Object ivk = engine.eval("var obj={sub:function(a,b){return a-b;}};function objSub(a,b){return obj.sub(a,b);};");

        Object obj = engine.invokeFunction("objSub", 2, 1);
        System.out.println(obj);


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
