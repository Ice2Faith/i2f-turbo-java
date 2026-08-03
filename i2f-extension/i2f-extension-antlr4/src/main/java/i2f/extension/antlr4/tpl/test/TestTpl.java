package i2f.extension.antlr4.tpl.test;

import i2f.extension.antlr4.tpl.impl.BindSqlTplResolver;
import i2f.extension.antlr4.tpl.impl.Tpl;
import i2f.extension.antlr4.tpl.impl.TplResolver;

import java.util.HashMap;

/**
 * @author Ice2Faith
 * @date 2026/8/3 8:50
 * @desc
 */
public class TestTpl {
    public static void main(String[] args){
        HashMap<Object, Object> map = new HashMap<>();
        map.put("username","zhang");
        map.put("age",12);
        map.put("status",true);
        String formula = "username=#{username}, age=${age}, status=${status?\"正常\":\"禁用\"}";
        formula="username=#{username}, age=#{age},#(end),#) status=#{status?\"正常\":\"禁用\"}\n" +
                "#if(username)\n" +
                "    #foreach(item,${username.toCharArray()})\n" +
                "        #{item+'55'},\n" +
                "    ##\n" +
                "##";
        formula="username=${username}, age=${age},#(end),#) status=${status?\"正常\":\"禁用\"}\n" +
                "\n" +
                "#if(cond:username)\n" +
                "    #foreach(item:item,coll:${username.toCharArray()})\n" +
                "        ${item},#sharp()\n" +
                "    ##aaa\n" +
                "    bbb\n" +
                "#else(user)\n" +
                "    ccc\n" +
                "#else()\n" +
                "    ddd\n" +
                "##";
        TplResolver resolver=new BindSqlTplResolver();
        Object ret = Tpl.render(formula, map,resolver);
        System.out.println(ret);
    }
}
