package i2f.annotations.ext.wrap;

import i2f.annotations.core.doc.Comment;
import i2f.enums.api.IDict;

/**
 * @author Ice2Faith
 * @date 2024/2/21 14:53
 * @desc
 */
@Comment({
        "字符串填充模式"
})
public enum PaddingMode implements IDict {
    LEFT(-1, "LEFT", "左", null),
    CENTER(0, "CENTER", "中", null),
    RIGHT(1, "RIGHT", "右", null),
    ;

    private int code;
    private String key;
    private String text;
    private String remark;

    PaddingMode(int code, String key, String text, String remark) {
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
