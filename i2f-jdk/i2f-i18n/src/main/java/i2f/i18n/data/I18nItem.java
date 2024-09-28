package i2f.i18n.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/9/28 14:30
 */
@Data
@NoArgsConstructor
public class I18nItem {
    protected String lang;
    protected String name;
    protected String value;

    public I18nItem(String lang, String name, String value) {
        this.lang = lang;
        this.name = name;
        this.value = value;
    }
}
