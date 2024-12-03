package i2f.translate.zh2pingyin.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/3 20:10
 * @desc
 */
@Data
@NoArgsConstructor
public class Zh2PinyinVo {
    private Long id;
    private String word;
    private String oldWord;
    private Integer strokeNum;
    private String pinYin;
    private String radicals;
}
