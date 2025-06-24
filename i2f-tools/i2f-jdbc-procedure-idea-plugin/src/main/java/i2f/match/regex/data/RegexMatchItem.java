package i2f.match.regex.data;

/**
 * @author Ice2Faith
 * @date 2021/10/18
 */
public class RegexMatchItem {
    public String srcStr;
    public String regexStr;
    public String matchStr;
    public int idxStart;
    public int idxEnd;

    public String getSrcStr() {
        return srcStr;
    }

    public void setSrcStr(String srcStr) {
        this.srcStr = srcStr;
    }

    public String getRegexStr() {
        return regexStr;
    }

    public void setRegexStr(String regexStr) {
        this.regexStr = regexStr;
    }

    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    public int getIdxStart() {
        return idxStart;
    }

    public void setIdxStart(int idxStart) {
        this.idxStart = idxStart;
    }

    public int getIdxEnd() {
        return idxEnd;
    }

    public void setIdxEnd(int idxEnd) {
        this.idxEnd = idxEnd;
    }
}
