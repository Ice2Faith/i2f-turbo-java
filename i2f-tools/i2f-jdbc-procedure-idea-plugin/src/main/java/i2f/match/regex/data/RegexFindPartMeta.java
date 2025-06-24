package i2f.match.regex.data;


/**
 * @author Ice2Faith
 * @date 2021/11/3
 */
public class RegexFindPartMeta {
    public String part; //字符串部分
    public boolean isMatch; //指示是否是匹配的项

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }
}
