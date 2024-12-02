package i2f.extension.reverse.engineer.generator.er.er0.drawio;

import i2f.database.metadata.data.TableMeta;
import i2f.extension.velocity.VelocityGenerator;
import i2f.resources.ResourceUtil;
import i2f.text.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/12/2 10:29
 */
public class DatabaseEr0DrawIoGenerator {
    public static String render(List<TableMeta> tableMetas) throws IOException {
        List<DatabaseEr0TableContext> list = new ArrayList<>();

        int x = 0;
        int y = 0;
        for (TableMeta tableMeta : tableMetas) {
            DatabaseEr0TableContext vo = new DatabaseEr0TableContext();
            vo.setMeta(tableMeta);
            vo.setValue(tableMeta.getComment());
            if (StringUtils.isEmpty(vo.getValue())) {
                vo.setValue(tableMeta.getName());
            }
            vo.setId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
            vo.setX(40 + (x * 140));
            vo.setY(120 + (y * 80));
            vo.setWidth(120);
            vo.setHeight(60);
            list.add(vo);

            x++;
            if (x == 8) {
                x = 0;
                y++;
            }
        }

        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/er/er0/drawio/er-0.xml.drawio.vm", "UTF-8");
        Map<String, Object> param = new HashMap<>();
        param.put("tables", list);
        return VelocityGenerator.render(tpl, param);
    }
}
