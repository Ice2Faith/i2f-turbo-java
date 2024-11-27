package i2f.relation.javacode.data.echcarts;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/11/27 22:05
 * @desc
 */
@Data
public class EchartsMapNodes {
    protected int category;
    protected String id;
    protected String name;
    protected double symbolSize = 20;
    protected double value = 0;
    protected double x = 0;
    protected double y = 0;
}
