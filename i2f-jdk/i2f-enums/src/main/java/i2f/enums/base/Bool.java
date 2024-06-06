package i2f.enums.base;


import i2f.enums.api.IDict;

/**
 * @author Ice2Faith
 * @date 2024/2/21 14:53
 * @desc
 */
public enum Bool implements IDict {
    UNSET(-1, "UNSET", "未定义", null),
    TRUE(1, "TRUE", "真", null),
    FALSE(0, "FALSE", "假", null),
    ;

    private int code;
    private String key;
    private String text;
    private String remark;

    Bool(int code, String key, String text, String remark) {
        this.code = code;
        this.key = key;
        this.text = text;
        this.remark = remark;
    }


    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public String remark() {
        return this.remark;
    }
}
