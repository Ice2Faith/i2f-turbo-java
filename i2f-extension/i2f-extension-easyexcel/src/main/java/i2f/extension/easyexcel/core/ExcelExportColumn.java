package i2f.extension.easyexcel.core;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/7/1 16:51
 */
@Data
@NoArgsConstructor
public class ExcelExportColumn {
    protected String title;
    protected String prop;

    public ExcelExportColumn(String title, String prop) {
        this.title = title;
        this.prop = prop;
    }


}
