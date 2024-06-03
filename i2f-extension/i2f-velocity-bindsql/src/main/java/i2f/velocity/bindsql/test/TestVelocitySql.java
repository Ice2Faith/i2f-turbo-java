package i2f.velocity.bindsql.test;

import i2f.bindsql.BindSql;
import i2f.velocity.GeneratorTool;
import i2f.velocity.bindsql.VelocitySqlGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/5/31 23:00
 * @desc
 */
public class TestVelocitySql {
    public static void main(String[] args) throws Exception {
        String content= GeneratorTool.readFile(".\\i2f-extension\\i2f-velocity-bindsql\\src\\main\\java\\i2f\\velocity\\bindsql\\test\\test.sql.vm","utf-8");
        Map<String,Object> params = new HashMap<>();
        params.put("table", "sys_user");
        Map<String,Object> map=new HashMap<>();
        map.put("id", 1);
        map.put("username","zhang");
        map.put("age",12);
        params.put("map",map);

        String[] templates = content.split(";");

        for (String template : templates) {
            System.out.println("=============================================");
            BindSql bql = VelocitySqlGenerator.renderSql(template, params);
            System.out.println(template);
            System.out.println(">>>>>>>>>>>>>>>");
            System.out.println(bql);
        }


        System.out.println("ok");
    }

}
