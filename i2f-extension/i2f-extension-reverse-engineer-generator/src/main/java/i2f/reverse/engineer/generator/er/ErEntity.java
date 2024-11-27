package i2f.reverse.engineer.generator.er;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
        ret.id = CodeUtil.makeUUID();
        ret.width = 160;
        ret.height = 80;
        ret.text = meta.getRemark();
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
