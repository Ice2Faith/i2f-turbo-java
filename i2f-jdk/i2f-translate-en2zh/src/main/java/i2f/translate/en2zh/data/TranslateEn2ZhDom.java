package i2f.translate.en2zh.data;

import i2f.annotations.db.Primary;
import i2f.annotations.db.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/11/30 18:51
 */
@Data
@NoArgsConstructor
@Table("translate_en2zh")
public class TranslateEn2ZhDom {
    @Primary
    protected Long id;
    protected String word;
    protected String transPos;
    protected String transCn;
}
