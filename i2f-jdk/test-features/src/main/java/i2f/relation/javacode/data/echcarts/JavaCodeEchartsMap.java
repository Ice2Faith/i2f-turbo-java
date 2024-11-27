package i2f.relation.javacode.data.echcarts;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/27 22:00
 * @desc
 */
@Data
public class JavaCodeEchartsMap {
    protected List<EchartsMapCategories> categories = new ArrayList<>();
    protected List<EchartsMapLinks> links = new ArrayList<>();
    protected List<EchartsMapNodes> nodes = new ArrayList<>();
}
