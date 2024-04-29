package i2f.match.regex.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2021/11/3
 */
@Data
@NoArgsConstructor
public class RegexFindPartMeta {
    public String part; //字符串部分
    public boolean isMatch; //指示是否是匹配的项
}
