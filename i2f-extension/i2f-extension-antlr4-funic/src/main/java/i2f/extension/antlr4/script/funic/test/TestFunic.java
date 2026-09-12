package i2f.extension.antlr4.script.funic.test;

import i2f.extension.antlr4.script.funic.lang.Funic;

import java.util.HashMap;

/**
 * @author Ice2Faith
 * @date 2026/4/23 20:34
 * @desc
 */
public class TestFunic {
    public static void main(String[] args) {
        String formula = "b=1;b+'***'.length()+'*'";
        HashMap<Object, Object> map = new HashMap<>();
        map.put("a", 1);
        Object ret = Funic.script(formula, map);
        System.out.println(ret);
    }
}
