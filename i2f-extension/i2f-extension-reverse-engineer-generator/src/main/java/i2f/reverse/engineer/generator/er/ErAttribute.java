package i2f.reverse.engineer.generator.er;

import i2f.database.metadata.data.ColumnMeta;
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
public class ErAttribute {
    private String id;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String text;

    public static ErAttribute parse(ColumnMeta meta) {
        ErAttribute ret = new ErAttribute();
        ret.id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        ret.width = 100;
        ret.height = 60;
        ret.text = meta.getComment();
        return ret;
    }

    public static List<ErAttribute> parse(List<ColumnMeta> metas) {
        List<ErAttribute> ret = new ArrayList<>();
        for (ColumnMeta item : metas) {
            ErAttribute attr = parse(item);
            ret.add(attr);
        }
        return ret;
    }
}
