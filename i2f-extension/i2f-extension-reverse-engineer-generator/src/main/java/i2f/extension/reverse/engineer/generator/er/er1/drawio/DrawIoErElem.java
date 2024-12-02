package i2f.extension.reverse.engineer.generator.er.er1.drawio;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/5/11 16:35
 * @desc
 */
@Data
@NoArgsConstructor
public class DrawIoErElem {
    private String id;
    private String text;
    // entity,attribute,link
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private String startId;
    private String endId;
}
