package i2f.extension.fastexcel.style;


import cn.idev.excel.annotation.ExcelProperty;
import i2f.extension.fastexcel.annotation.ExcelCellStyle;

import java.lang.reflect.Field;

/**
 * @author Ice2Faith
 * @date 2022/10/13 10:07
 * @desc
 */
public class ExcelStyleCallbackMeta {
    public Field field;
    public int index;
    public int order;
    public ExcelProperty property;
    public ExcelCellStyle[] style;

    // for spel
    public SpelEnhancer tool = new SpelEnhancer();

    // args
    public Object record;
    public Object val;
    public Integer row;
    public Integer col;

}
