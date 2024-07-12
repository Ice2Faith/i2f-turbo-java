package i2f.swl.data;

import lombok.Data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:23
 * @desc
 */
@Data
public class SwlData {
    private SwlHeader header;
    private List<String> parts;
    private List<String> attaches;
    private SwlContext context;
}
