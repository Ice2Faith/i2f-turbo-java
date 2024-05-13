package i2f.natives.windows.easyx.types;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/13 14:38
 * @desc
 */
@Data
public class FillStyle {
    public int style;
    public int hatch;
    public ImagePtr ppattern;
}
