package i2f.extension.reverse.engineer.generator.er.er2.drawio;

import i2f.database.metadata.data.TableMeta;
import i2f.extension.velocity.VelocityGenerator;
import i2f.resources.ResourceUtil;
import i2f.serialize.str.xml.impl.Xml2;

import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/12/1 19:29
 */
public class DatabaseEr2DrawIoGenerator {
    public static String render(List<TableMeta> metas) throws IOException {
        List<DatabaseEr2TableContext> list = new ArrayList<>();
        String tpl = ResourceUtil.getClasspathResourceAsString("tpl/er/er2/drawio/er-2-drawio-value.html.vm", "UTF-8");
        int x = 0;
        int y = 0;
        for (TableMeta item : metas) {
            Map<String, Object> param = new HashMap<>();
            param.put("table", item);
            String str = VelocityGenerator.render(tpl, param);
            str = str.replaceAll("\\s+", " ");
            str = Xml2.toXmlString(str);
            DatabaseEr2TableContext vo = new DatabaseEr2TableContext();
            vo.setMeta(item);
            vo.setValue(str);
            vo.setId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
            vo.setX(10 + (x * 320));
            vo.setY(100 + (y * 320));
            vo.setWidth(300);
            vo.setHeight(50 + 25 * item.getColumns().size());
            list.add(vo);

            x++;
            if (x == 5) {
                x = 0;
                y++;
            }
        }

        tpl = ResourceUtil.getClasspathResourceAsString("tpl/er/er2/drawio/er-2.xml.drawio.vm", "UTF-8");
        Map<String, Object> param = new HashMap<>();
        param.put("tables", list);
        return VelocityGenerator.render(tpl, param);
    }
}
