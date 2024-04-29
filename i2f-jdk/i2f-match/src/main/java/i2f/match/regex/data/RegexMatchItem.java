package i2f.match.regex.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2021/10/18
 */
@Data
@NoArgsConstructor
public class RegexMatchItem {
    public String srcStr;
    public String regexStr;
    public String matchStr;
    public int idxStart;
    public int idxEnd;
}
