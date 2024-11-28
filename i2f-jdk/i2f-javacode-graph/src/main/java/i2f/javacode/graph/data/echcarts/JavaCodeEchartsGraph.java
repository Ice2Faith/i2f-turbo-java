package i2f.javacode.graph.data.echcarts;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/27 22:00
 * @desc
 */
@Data
public class JavaCodeEchartsGraph {
    protected List<EchartsGraphCategories> categories = new ArrayList<>();
    protected List<EchartsGraphLinks> links = new ArrayList<>();
    protected List<EchartsGraphNodes> nodes = new ArrayList<>();
}
