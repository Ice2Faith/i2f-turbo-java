package i2f.velocity.bindsql;

import i2f.bindsql.BindSql;
import i2f.match.regex.RegexUtil;
import i2f.velocity.VelocityGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/1 16:30
 * @desc
 */
public class VelocitySqlGenerator {

    public static BindSql renderSql(String template, Map<String,Object> params) throws IOException {
        ValueWrapper wrapper = new ValueWrapper();
        params.put("__sql", wrapper);
        template="#macro(sql $value)$__sql.wrap($value)#end\n"+template;

        String rs = VelocityGenerator.render(template, params);

        List<Object> list=new ArrayList<>();
        String sql= RegexUtil.replace(rs,"\\$\\{\\d+\\}",(s, i)->{
            Object val = wrapper.getMap().get(s);
            list.add(val);
            return "?";
        });

        sql=sql.replaceAll("\\s+"," ");
        return new BindSql(sql,list);
    }
}
