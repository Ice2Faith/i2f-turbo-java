package i2f.extension.document.formats.excel;


import com.aspose.cells.SaveFormat;

/**
 * @author Ice2Faith
 * @date 2024/11/30 12:13
 */
public enum SheetFormat {
    CSV(1),
    XLSX(6),
    XLSM(7),
    XLTX(8),
    XLTM(9),
    XLAM(10),
    TAB_DELIMITED(11),
    HTML(12),
    M_HTML(17),
    ODS(14),
    EXCEL_97_TO_2003(5),
    SPREADSHEET_ML(15),
    XLSB(16),
    AUTO(0),
    UNKNOWN(255),
    PDF(13),
    XPS(20),
    TIFF(21),
    SVG(22),
    DIF(30),
    ;

    public static Class<?> REFER_CLASS = SaveFormat.class;

    private int code;

    private SheetFormat(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
