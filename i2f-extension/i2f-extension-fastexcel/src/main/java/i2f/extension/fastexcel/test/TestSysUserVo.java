package i2f.extension.fastexcel.test;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import i2f.extension.fastexcel.annotation.ExcelCellStyle;
import i2f.extension.fastexcel.annotation.ExcelTag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * @author Ice2Faith
 * @date 2024/6/24 11:28
 * @desc
 */
@ExcelCellStyle(head = true,
        backgroundColor = IndexedColors.LIGHT_YELLOW,
        fontBold = ExcelCellStyle.Bool.TRUE,
        alignHorizontal = HorizontalAlignment.CENTER,
        alignVertical = VerticalAlignment.CENTER
)
@ExcelIgnoreUnannotated
@Data
@NoArgsConstructor
public class TestSysUserVo {
    @ExcelProperty(value = "ID")
    private Long id;


    @ExcelCellStyle(hyperLink = true, fontUnderline = true, fontColor = IndexedColors.BLUE)
    @ExcelCellStyle(colWidth = 30, commentSpel = true, comment = "#{\"年龄为：\"+record.age}")
    @ExcelProperty(value = "用户名")
    private String username;

    @ExcelCellStyle(colWidth = 20)
    @ExcelTag({"no-export"})
    @ExcelProperty(value = "密码")
    private String password;

    @ExcelCellStyle(colWidth = 10, comment = "年龄大于30时为蓝色，否则为红色")
    @ExcelCellStyle(spel = "#{val > 30}", fontColor = IndexedColors.RED)
    @ExcelCellStyle(spel = "#{val <= 30}", fontColor = IndexedColors.BLUE)
    @ExcelProperty(value = "年龄")
    private int age;

    @ExcelCellStyle(selection = {"正常", "禁用"})
    @ExcelProperty(value = "状态")
    private String status;
}
