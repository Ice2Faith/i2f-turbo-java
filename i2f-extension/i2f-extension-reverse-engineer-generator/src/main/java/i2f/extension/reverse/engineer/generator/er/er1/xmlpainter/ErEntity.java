package i2f.extension.reverse.engineer.generator.er.er1.xmlpainter;

import i2f.database.metadata.data.TableMeta;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ltb
 * @date 2022/6/15 20:38
 * @desc
 */
@Data
public class ErEntity {
    private String id;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String text;
    private List<ErAttribute> attrs;
    private List<ErLine> lines;

    public static ErEntity parse(TableMeta meta) {
        ErEntity ret = new ErEntity();
        ret.id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        ret.width = 160;
        ret.height = 80;
        ret.text = meta.getComment();
        if (ret.text == null || ret.text.isEmpty()) {
            ret.text = meta.getName();
        }
        ret.attrs = ErAttribute.parse(meta.getColumns());
        ErLine.parse(ret);

        return ret;
    }

    public static List<ErEntity> parse(List<TableMeta> metas) {
        List<ErEntity> ret = new ArrayList<>();
        for (TableMeta item : metas) {
            ErEntity ety = parse(item);
            ret.add(ety);
        }
        return ret;
    }
}
