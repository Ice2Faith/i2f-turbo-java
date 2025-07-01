package i2f.extension.fastexcel.core;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/1/29 10:03
 * @desc
 */
@Data
public class ExcelExportPage {
    protected int index;
    protected int size;

    public ExcelExportPage() {
        this.index = 0;
        this.size = 100;
    }

    public ExcelExportPage(int index, int size) {
        this.index = index;
        this.size = size;
    }


}
