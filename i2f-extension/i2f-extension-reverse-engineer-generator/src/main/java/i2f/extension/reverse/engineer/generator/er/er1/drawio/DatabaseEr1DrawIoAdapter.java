package i2f.extension.reverse.engineer.generator.er.er1.drawio;


import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.TableMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2023/5/13 17:16
 * @desc
 */
public class DatabaseEr1DrawIoAdapter {

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static List<DrawIoErElem> parseEr(List<TableMeta> tables) {

        int y = 0;
        List<DrawIoErElem> elems = new LinkedList<>();
        for (TableMeta table : tables) {
            int x = 0;
            List<ColumnMeta> columns = table.getColumns();
            DrawIoErElem entity = new DrawIoErElem();
            entity.setId(getId());
            entity.setText(table.getComment());
            if (entity.getText() == null || entity.getText().isEmpty()) {
                entity.setText(table.getName());
            }
            entity.setType("entity");
            entity.setX(x);
            entity.setY(y);
            entity.setWidth(120);
            entity.setHeight(60);
            elems.add(entity);

            y += 200;
            for (ColumnMeta column : columns) {
                DrawIoErElem attribute = new DrawIoErElem();
                attribute.setId(getId());
                attribute.setText(column.getComment());
                if (attribute.getText() == null || attribute.getText().isEmpty()) {
                    attribute.setText(column.getName());
                }
                attribute.setType("attribute");
                attribute.setX(x);
                attribute.setY(y);
                attribute.setWidth(120);
                attribute.setHeight(60);
                elems.add(attribute);

                DrawIoErElem link = new DrawIoErElem();
                link.setId(getId());
                link.setText("");
                link.setType("link");
                link.setWidth(50);
                link.setHeight(50);
                link.setStartId(entity.getId());
                link.setEndId(attribute.getId());
                elems.add(link);

                x += 100;
            }

            y += 200;
        }

        return elems;
    }
}
