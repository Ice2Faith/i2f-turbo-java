package i2f.extension.easyexcel.core;

/**
 * @author Ice2Faith
 * @date 2024/1/29 9:56
 * @desc
 */
public enum ExcelExportMode {
    PAGE(1, "仅当前分页"),
    ALL(2, "全部数据");
    private int code;
    private String text;

    ExcelExportMode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int code() {
        return this.code;
    }

    public String text() {
        return this.text;
    }

    public static ExcelExportMode parse(int code) {
        ExcelExportMode[] arr = ExcelExportMode.values();
        for (ExcelExportMode item : arr) {
            if (item.code() == code) {
                return item;
            }
        }
        return ExcelExportMode.ALL;
    }

}
